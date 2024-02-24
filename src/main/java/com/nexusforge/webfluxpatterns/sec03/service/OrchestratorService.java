package com.nexusforge.webfluxpatterns.sec03.service;

import com.nexusforge.webfluxpatterns.sec03.client.ProductClient;
import com.nexusforge.webfluxpatterns.sec03.dto.*;
import com.nexusforge.webfluxpatterns.sec03.util.DebugUtil;
import com.nexusforge.webfluxpatterns.sec03.util.OrchestrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorService {
    @Autowired
    private ProductClient productClient;
    @Autowired
    private OrderFulfillmentService fulfillmentService;
    @Autowired
    private OrderCancellationService cancellationService;

    public Mono<OrderResponse> placeOrder(Mono<OrderRequest> mono) {
        return mono.map(OrchestrationRequestContext::new)
                .flatMap(this::getProduct)
                .doOnNext(OrchestrationUtil::buildRequestContext)
                .flatMap(fulfillmentService::placeOrder)
                .doOnNext(this::doOrderPostProcessing)
                //.doOnNext(DebugUtil::print) // debug
                .map(this::toOrderResponse);
    }

    private void doOrderPostProcessing(OrchestrationRequestContext context) {
        if (Status.FAILED.equals(context.getStatus())) {
            this.cancellationService.cancelOrder(context);
        }
        // if the order is a success, perform post processing l
        // like sending notifications and bla bla bla
    }

    private Mono<OrchestrationRequestContext> getProduct(OrchestrationRequestContext ctx) {
        return this.productClient.getProduct(ctx.getOrderRequest().getProductId())
                .map(Product::getPrice)
                .doOnNext(ctx::setProductPrice)
                .map(i -> ctx);
    }

    private OrderResponse toOrderResponse(OrchestrationRequestContext ctx) {
        var isSuccess = Status.SUCCESS.equals(ctx.getStatus());
        var address = isSuccess ? ctx.getShippingResponse().getAddress() : null;
        var dateDelivery = isSuccess ? ctx.getShippingResponse().getExpectedDelivery() : null;
        return OrderResponse.create(
                ctx.getOrderRequest().getUserId(),
                ctx.getOrderRequest().getProductId(),
                ctx.getOrderId(),
                ctx.getStatus(),
                address,
                dateDelivery
        );
    }
}
