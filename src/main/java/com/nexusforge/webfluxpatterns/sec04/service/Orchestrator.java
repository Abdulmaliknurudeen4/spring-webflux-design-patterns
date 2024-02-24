package com.nexusforge.webfluxpatterns.sec04.service;

import com.nexusforge.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Orchestrator {

    public abstract Mono<OrchestrationRequestContext> create(OrchestrationRequestContext ctx);

    public abstract Predicate<OrchestrationRequestContext> isSuccess();

    public abstract Consumer<OrchestrationRequestContext> cancel();

}
