package com.nexusforge.webfluxpatterns.sec10.client;

import com.nexusforge.webfluxpatterns.sec10.dto.Review;
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

    public ReviewClient(@Value("${sec10.review.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }


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

}
