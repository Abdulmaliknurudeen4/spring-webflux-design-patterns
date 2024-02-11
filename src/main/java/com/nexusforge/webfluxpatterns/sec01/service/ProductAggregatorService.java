package com.nexusforge.webfluxpatterns.sec01.service;

import com.nexusforge.webfluxpatterns.sec01.client.ProductClient;
import com.nexusforge.webfluxpatterns.sec01.client.PromotionClient;
import com.nexusforge.webfluxpatterns.sec01.client.ReviewClient;
import com.nexusforge.webfluxpatterns.sec01.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductAggregatorService {

    private final ProductClient productClient;
    private final PromotionClient promotionClient;
    private final ReviewClient reviewClient;

    public ProductAggregatorService(ProductClient productClient, PromotionClient promotionClient, ReviewClient reviewClient) {
        this.productClient = productClient;
        this.promotionClient = promotionClient;
        this.reviewClient = reviewClient;
    }

    public Mono<ProductAggregate> aggregate(Integer id) {
        return Mono.zip(
                        this.productClient.getProduct(id),
                        this.promotionClient.getPromotion(id),
                        this.reviewClient.getReviews(id)
                )
                .map(t -> toDTo(t.getT1(), t.getT2(), t.getT3()));
    }

    private ProductAggregate toDTo(ProductResponse product,
                                   PromotionResponse promotion, List<Review> review) {

        var price = new Price();
        var amountSaved = product.getPrice() * promotion.getDiscount() / 100;
        var discountedPrice = product.getPrice() - amountSaved;
        price.setListPrice(product.getPrice());
        price.setAmountSaved(amountSaved);
        price.setDiscountedPrice(discountedPrice);
        price.setDiscount(promotion.getDiscount());
        price.setEndDate(promotion.getEndData());

        return ProductAggregate.create(
                product.getId(),
                product.getCategory(),
                product.getDescription(),
                price,
                review
        );
    }
}
