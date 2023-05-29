package com.example.registration_login_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDetailDto {
    private String stockCode;
    private String change;
    private String percentageChange;
    private String volume;
    private String buyVolume;
    private String buy;
    private String sell;
    private String sellVolume;
    private String lacp;
    private String open;
    private String high;
    private String low;

}

