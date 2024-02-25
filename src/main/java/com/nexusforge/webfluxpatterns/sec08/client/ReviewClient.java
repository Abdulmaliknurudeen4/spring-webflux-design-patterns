package com.nexusforge.webfluxpatterns.sec08.client;

import com.nexusforge.webfluxpatterns.sec08.dto.Review;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewClient {

    private final WebClient webClient;

    public ReviewClient(@Value("${sec08.review.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    @CircuitBreaker(name = "review-service", fallbackMethod = "fallBackReview")
    public Mono<List<Review>> getReviews(Integer id) {
        return this.webClient
                .get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .bodyToFlux(Review.class).collectList()
                .retry(5)
                .timeout(Duration.ofMillis(300)); // timeout exception will be thrown here.
    }

    public Mono<List<Review>> fallBackReview(Integer id, Throwable ex) {
        // gives you the record exception details.
        System.out.println("Fallback Reviews Called --- " + ex.getMessage());
        return Mono.just(Collections.emptyList());
    }

}
