package com.example.registration_login_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "tradinghistory")
public class TradingHistory {

    @Id
    @Column(name = "order_id")
    private int OrderId;

    @ManyToOne
    @JoinColumn(name = "UID")
    private BuyUser user;

    @Column(name = "Symbol")
    private String symbol;

    @Column(name = "Lots")
    private int lots;

    @Column(name = "buy_price")
    private double BuyPrice;

    @Column(name = "order_pending_time")
    private LocalDateTime orderPendingTime;

}