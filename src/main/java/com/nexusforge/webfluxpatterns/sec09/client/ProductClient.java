package com.nexusforge.webfluxpatterns.sec09.client;

import com.nexusforge.webfluxpatterns.sec09.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(@Value("${sec09.product.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<Product> getProduct(Integer id) {
        return this.webClient
                .get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .bodyToMono(Product.class)
                .retryWhen(Retry.fixedDelay(5, Duration.ofMillis(500)))
                .timeout(Duration.ofMillis(300))
                .onErrorResume(ex -> Mono.empty());
    }
}
