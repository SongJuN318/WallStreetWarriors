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
@Table(name = "buy")
public class Buy {

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

    // Constructors, getters, and setters
    // Getters and setters
}