package com.nexusforge.webfluxpatterns.sec03.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Getter
@Setter
public class PaymentResponse {
    private Integer userId;
    private String name;
    private Integer balance;
    private Status status;
}
