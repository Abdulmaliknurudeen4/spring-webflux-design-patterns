package com.nexusforge.webfluxpatterns;

import com.nexusforge.webfluxpatterns.sec10.dto.ProductAggregate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BulkHeadTest {

    private WebClient client;

    @BeforeAll
    protected void setUp() {
        this.client = WebClient.builder()
                .baseUrl("http://localhost:8080/sec10/")
                .build();
    }

    @Test
    public void concurrentUserstest() {
        StepVerifier.create(Flux.merge(
                fibRequest(),
                productRequest())).verifyComplete();
    }

    private Mono<Void> fibRequest() {
        return Flux.range(1, 2)
                .flatMap(i -> this.client.get().uri("fib/46").retrieve().bodyToMono(Long.class))
                .doOnNext(this::print)
                .then();
    }

    private Mono<Void> productRequest() {
        return Mono.delay(Duration.ofMillis(100))
                .thenMany(Flux.range(1, 40))
                .flatMap(i -> this.client.get().uri("product/1").retrieve().bodyToMono(ProductAggregate.class))
                .map(ProductAggregate::getCategory)
                .doOnNext(this::print)
                .then();
    }

    private void print(Object o) {
        System.out.println(LocalDateTime.now() + " : " + o);
    }
}
