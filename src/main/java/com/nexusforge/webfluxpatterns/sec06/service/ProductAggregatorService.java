package com.nexusforge.webfluxpatterns.sec06.service;

import com.nexusforge.webfluxpatterns.sec06.client.ProductClient;
import com.nexusforge.webfluxpatterns.sec06.client.ReviewClient;
import com.nexusforge.webfluxpatterns.sec06.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductAggregatorService {

    private final ProductClient productClient;

    private final ReviewClient reviewClient;

    public ProductAggregatorService(ProductClient productClient, ReviewClient reviewClient) {
        this.productClient = productClient;
        this.reviewClient = reviewClient;
    }

    public Mono<ProductAggregate> aggregate(Integer id) {
        return Mono.zip(
                        this.productClient.getProduct(id),
                        this.reviewClient.getReviews(id)
                )
                .map(t -> toDTo(t.getT1(), t.getT2()));
    }

    private ProductAggregate toDTo(Product product, List<Review> review) {


        return ProductAggregate.create(
                product.getId(),
                product.getCategory(),
                product.getDescription(),
                review
        );
    }
}
