package com.nexusforge.webfluxpatterns.sec05.service;

import com.nexusforge.webfluxpatterns.sec05.client.RoomClient;
import com.nexusforge.webfluxpatterns.sec05.dto.ReservationItemRequest;
import com.nexusforge.webfluxpatterns.sec05.dto.ReservationItemResponse;
import com.nexusforge.webfluxpatterns.sec05.dto.ReservationType;
import com.nexusforge.webfluxpatterns.sec05.dto.RoomReservationResponse;
import com.nexusforge.webfluxpatterns.sec05.utils.ReservationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RoomReservationHandler extends ReservationHandler {

    @Autowired
    private RoomClient client;

    @Override
    protected ReservationType getType() {
        return ReservationType.ROOM;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(ReservationUtils::toRoomRequest)
                .transform(this.client::reserve)
                .map(this::toResponse);
    }

    private ReservationItemResponse toResponse(RoomReservationResponse response) {
        return ReservationItemResponse.create(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getCheckIn(),
                response.getCheckOut(),
                response.getPrice()
        );
    }


}
