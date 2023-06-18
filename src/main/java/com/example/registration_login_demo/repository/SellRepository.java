package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell, Integer> {
    List<Sell> findByUser(BuyUser user);
}
