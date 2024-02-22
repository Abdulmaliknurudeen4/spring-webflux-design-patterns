package com.nexusforge.webfluxpatterns.sec03.util;

import com.nexusforge.webfluxpatterns.sec03.dto.InventoryRequest;
import com.nexusforge.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import com.nexusforge.webfluxpatterns.sec03.dto.PaymentRequest;
import com.nexusforge.webfluxpatterns.sec03.dto.ShippingRequest;

public class OrchestrationUtil {

    public static void buildRequestContext(OrchestrationRequestContext ctx) {
        buildPaymentRequest(ctx);
        buildInventoryRequest(ctx);
        buildShipppingRequest(ctx);
    }

    //Build the payment Request to send to the Userpayment Client
    private static void buildPaymentRequest(OrchestrationRequestContext ctx) {
        var paymentRequest = PaymentRequest.create(
                ctx.getOrderRequest().getUserId(),
                (ctx.getProductPrice() * ctx.getOrderRequest().getQuantity()),
                ctx.getOrderId()
        );
        ctx.setPaymentRequest(paymentRequest);
    }

    private static void buildInventoryRequest(OrchestrationRequestContext ctx) {
        var inventoryRequest = InventoryRequest.create(
                ctx.getOrderId(),
                ctx.getOrderRequest().getProductId(),
                ctx.getOrderRequest().getQuantity()
        );
        ctx.setInventoryRequest(inventoryRequest);
    }

    private static void buildShipppingRequest(OrchestrationRequestContext ctx) {
        var shippingRequest = ShippingRequest.create(
                ctx.getOrderRequest().getQuantity(),
                ctx.getOrderRequest().getUserId(),
                ctx.getOrderId()
        );
        ctx.setShippingRequest(shippingRequest);
    }


}
