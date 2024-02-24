package com.nexusforge.webfluxpatterns.sec04.util;

import com.nexusforge.webfluxpatterns.sec04.dto.InventoryRequest;
import com.nexusforge.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import com.nexusforge.webfluxpatterns.sec04.dto.PaymentRequest;
import com.nexusforge.webfluxpatterns.sec04.dto.ShippingRequest;

public class OrchestrationUtil {


    //Build the payment Request to send to the Userpayment Client
    public static void buildPaymentRequest(OrchestrationRequestContext ctx) {
        var paymentRequest = PaymentRequest.create(
                ctx.getOrderRequest().getUserId(),
                (ctx.getProductPrice() * ctx.getOrderRequest().getQuantity()),
                ctx.getOrderId()
        );
        ctx.setPaymentRequest(paymentRequest);
    }

    public static void buildInventoryRequest(OrchestrationRequestContext ctx) {
        var inventoryRequest = InventoryRequest.create(
                ctx.getPaymentResponse().getPaymentId(),
                ctx.getOrderRequest().getProductId(),
                ctx.getOrderRequest().getQuantity()
        );
        ctx.setInventoryRequest(inventoryRequest);
    }

    public static void buildShipppingRequest(OrchestrationRequestContext ctx) {
        var shippingRequest = ShippingRequest.create(
                ctx.getOrderRequest().getQuantity(),
                ctx.getOrderRequest().getUserId(),
                ctx.getInventoryResponse().getInventoryId()
        );
        ctx.setShippingRequest(shippingRequest);
    }


}
