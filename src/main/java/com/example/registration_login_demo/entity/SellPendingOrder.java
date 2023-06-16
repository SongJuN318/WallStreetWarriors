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
@Table(name = "sellpendingorder")
public class SellPendingOrder {

    @Id
    @Column(name = "order_id")
    private long OrderId;

    @Column(name = "buy_price")
    private double buyPrice;
    
    @ManyToOne
    @JoinColumn(name = "UID")
    private BuyUser user;

    @Column(name = "Symbol")
    private String symbol;

    @Column(name = "Lots")
    private int lots;

    @Column(name = "sell_price")
    private double SellPrice;

    private LocalDateTime SellPendingTime;

}
