package com.nexusforge.webfluxpatterns.sec05.service;

import com.nexusforge.webfluxpatterns.sec05.client.CarClient;
import com.nexusforge.webfluxpatterns.sec05.dto.*;
import com.nexusforge.webfluxpatterns.sec05.utils.ReservationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CarReservationHandler extends ReservationHandler {

    @Autowired
    private CarClient client;

    @Override
    protected ReservationType getType() {
        return ReservationType.CAR;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(ReservationUtils::toCarRequest)
                .transform(this.client::reserve)
                .map(this::toResponse);
    }

    private ReservationItemResponse toResponse(CarReservationResponse response) {
        return ReservationItemResponse.create(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getPickup(),
                response.getDrop(),
                response.getPrice()
        );
    }


}
