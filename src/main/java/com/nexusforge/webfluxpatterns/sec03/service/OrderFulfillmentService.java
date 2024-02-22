package com.nexusforge.webfluxpatterns.sec03.service;

import com.nexusforge.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import com.nexusforge.webfluxpatterns.sec03.dto.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderFulfillmentService {
    @Autowired
    private List<Orchestrator> orchestrators;

    public Mono<OrchestrationRequestContext> placeOrder(OrchestrationRequestContext ctx) {
        var list = orchestrators.stream()
                .map(o -> o.create(ctx))
                .toList();
        return Mono.zip(list, a -> a[0])
                .cast(OrchestrationRequestContext.class)
                .doOnNext(this::updateStatus);
    }

    private void updateStatus(OrchestrationRequestContext ctx) {
        var allSucces = this.orchestrators.stream().allMatch(o -> o.isSuccess().test(ctx));
        var status = allSucces ? Status.SUCCESS : Status.FAILED;
        ctx.setStatus(status);
    }
}
