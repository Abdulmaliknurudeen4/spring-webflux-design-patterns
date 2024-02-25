package com.nexusforge.webfluxpatterns.sec06.controller;

import com.nexusforge.webfluxpatterns.sec06.dto.ProductAggregate;
import com.nexusforge.webfluxpatterns.sec06.service.ProductAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("sec06")
public class ProductAggregatorController {

    @Autowired
    private ProductAggregatorService aggregator;

    @GetMapping("product/{id}")
    public Mono<ResponseEntity<ProductAggregate>> getProductAggregate(@PathVariable Integer id){
        return this.aggregator.aggregate(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
