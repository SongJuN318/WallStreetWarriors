package com.example.registration_login_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyPendingOrderDTO {
    private final String type = "BUY";
    private long orderId;
    private long userId;
    private String symbol;
    private int lots;
    private double buyPrice;
}
