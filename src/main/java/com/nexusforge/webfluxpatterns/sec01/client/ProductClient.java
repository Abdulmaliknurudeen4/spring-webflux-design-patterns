package com.nexusforge.webfluxpatterns.sec01.client;

import com.nexusforge.webfluxpatterns.sec01.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(@Value("${sec01.product.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<ProductResponse> getProduct(Integer id) {
        return this.webClient
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .onErrorResume(ex -> Mono.empty());
    }
}
