package com.nexusforge.webfluxpatterns.sec09.client;

import com.nexusforge.webfluxpatterns.sec09.dto.Review;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewClient {

    private final WebClient webClient;

    public ReviewClient(@Value("${sec09.review.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }


    @RateLimiter(name = "review-service", fallbackMethod = "fallback")
    public Mono<List<Review>> getReviews(Integer id) {
        //client service. reduce pricing paid to outside service
        // without overuse.
        return this.webClient
                .get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .bodyToFlux(Review.class).collectList()
                .doOnNext(c -> System.out.println("perform Caching"));
    }

    public Mono<List<Review>> fallback(Integer id, Throwable ex) {
        // return results as store in cache.
        return Mono.just(Collections.emptyList());
    }
}
