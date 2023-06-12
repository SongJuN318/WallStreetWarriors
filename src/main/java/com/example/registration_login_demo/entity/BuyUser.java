
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
@Table(name = "user")
public class BuyUser {

    @Id
    @Column(name = "Id")
    private int id;

    @Column(name = "Email")
    private String email;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "current_fund")
    private Double currentFund;

    @Column(name = "PnL")
    private Double pnl;

    @Column(name = "Point")
    private Double point;

    // Constructors, getters, and setters
}