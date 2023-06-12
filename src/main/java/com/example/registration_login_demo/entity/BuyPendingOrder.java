package com.example.registration_login_demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "buypendingorder")
public class BuyPendingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private LocalDateTime OrderPendingTime;

}
