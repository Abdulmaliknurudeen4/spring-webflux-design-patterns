package com.nexusforge.webfluxpatterns.sec03.service;

import com.nexusforge.webfluxpatterns.sec03.client.UserClient;
import com.nexusforge.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import com.nexusforge.webfluxpatterns.sec03.dto.Status;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
public class PaymentOrchestrator extends Orchestrator {


    @Autowired
    private UserClient client;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext ctx) {
        return this.client.deduct(ctx.getPaymentRequest())
                .doOnNext(ctx::setPaymentResponse)
                .thenReturn(ctx);
    }

    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return ctx -> Status.SUCCESS.equals(ctx.getPaymentResponse().getStatus());
    }

    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        // refund process
        return ctx -> Mono.just(ctx)
                .filter(isSuccess())
                .map(OrchestrationRequestContext::getPaymentRequest)
                .flatMap(this.client::refund)
                .subscribe();
    }
}
