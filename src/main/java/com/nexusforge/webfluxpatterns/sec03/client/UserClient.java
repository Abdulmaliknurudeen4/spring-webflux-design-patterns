package com.nexusforge.webfluxpatterns.sec03.client;

import com.nexusforge.webfluxpatterns.sec03.dto.PaymentRequest;
import com.nexusforge.webfluxpatterns.sec03.dto.PaymentResponse;
import com.nexusforge.webfluxpatterns.sec03.dto.Status;
import com.nexusforge.webfluxpatterns.sec03.util.DebugUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserClient {
    private final WebClient webClient;
    private static final String DEDUCT = "deduct";
    private static final String REFUND = "refund";

    public UserClient(@Value("${sec03.user.service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<PaymentResponse> deduct(PaymentRequest request) {
        return this.callUserService(DEDUCT, request);
    }

    public Mono<PaymentResponse> refund(PaymentRequest request) {
        return this.callUserService(REFUND, request);
    }

    private Mono<PaymentResponse> callUserService(String endpoint, PaymentRequest request) {
        return this.webClient
                .post()
                .uri(endpoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .onErrorReturn(this.buildErrorResponse(request));
    }

    private PaymentResponse buildErrorResponse(PaymentRequest request) {
        return PaymentResponse.create(request.getUserId(), null, request.getAmount(), Status.FAILED);
    }
}
