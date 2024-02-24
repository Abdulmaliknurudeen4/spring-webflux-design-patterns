package com.nexusforge.webfluxpatterns.sec04.dto;

import lombok.*;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Getter
@Setter
public class PaymentResponse {
    private UUID paymentId;
    private Integer userId;
    private String name;
    private Integer balance;
    private Status status;
}
