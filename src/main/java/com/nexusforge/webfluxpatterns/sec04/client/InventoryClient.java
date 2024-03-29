package com.nexusforge.webfluxpatterns.sec04.client;

import com.nexusforge.webfluxpatterns.sec04.dto.InventoryRequest;
import com.nexusforge.webfluxpatterns.sec04.dto.InventoryResponse;
import com.nexusforge.webfluxpatterns.sec04.dto.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class InventoryClient {
    private final WebClient webClient;
    private static final String DEDUCT = "deduct";
    private static final String RESTORE = "restore";

    public InventoryClient(@Value("${sec04.inventory.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<InventoryResponse> deduct(InventoryRequest request) {
        return this.callInventoryService(DEDUCT, request);
    }

    public Mono<InventoryResponse> restore(InventoryRequest request) {
        return this.callInventoryService(RESTORE, request);
    }

    private Mono<InventoryResponse> callInventoryService(String endpoint, InventoryRequest request) {
        return this.webClient
                .post()
                .uri(endpoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .onErrorReturn(this.buildErrorResponse(request));
    }

    private InventoryResponse buildErrorResponse(InventoryRequest request) {
        return InventoryResponse.create(null, request.getProductId(),
                request.getQuantity(), null, Status.FAILED);
    }


}
