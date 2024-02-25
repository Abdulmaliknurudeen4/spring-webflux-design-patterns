package com.nexusforge.webfluxpatterns.sec05.utils;

import com.nexusforge.webfluxpatterns.sec05.dto.*;

public class ReservationUtils {
    public static CarReservationRequest toCarRequest(ReservationItemRequest request) {
        return CarReservationRequest.create(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }

    public static RoomReservationRequest toRoomRequest(ReservationItemRequest request) {
        return RoomReservationRequest.create(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }


}
