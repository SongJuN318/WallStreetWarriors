package com.example.registration_login_demo.entity;

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
@Table(name = "sell")
public class Sell {

    @Id
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "Id")  // Assuming the column name in the table is "Id"
    private BuyUser user;  // Assuming the User entity is correctly defined

    @Column(name = "Symbol")
    private String symbol;

    @Column(name = "Lots")
    private int lots;

    @Column(name = "buy_price")
    private double buyPrice;

    @Column(name = "sell_price")
    private double sellPrice;

    @Column(name = "profit_lost")
    private double profitLost;

}
