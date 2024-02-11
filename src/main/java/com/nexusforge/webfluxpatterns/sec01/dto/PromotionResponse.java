package com.nexusforge.webfluxpatterns.sec01.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class PromotionResponse {
    private Integer id;
    private String type;
    private Double discount;
    private LocalDate endData;
}
