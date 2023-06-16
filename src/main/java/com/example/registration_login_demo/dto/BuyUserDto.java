package com.example.registration_login_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyUserDto {
    long id;
    double funds;
    double pnl;
    double point;
}
