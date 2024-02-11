package com.nexusforge.webfluxpatterns.sec01.client;

import com.nexusforge.webfluxpatterns.sec01.dto.ProductResponse;
import com.nexusforge.webfluxpatterns.sec01.dto.PromotionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class PromotionClient {

    private final PromotionResponse noPromotion = PromotionResponse.create(-1, "No promotion", 0.0, LocalDate.now());

    private final WebClient webClient;

    public PromotionClient(@Value("${sec01.promotion.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<PromotionResponse> getPromotion(Integer id) {
        return this.webClient
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(PromotionResponse.class)
                .onErrorReturn(noPromotion);
    }
}
