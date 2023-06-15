package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellRepository extends JpaRepository<Sell, Integer> {

}
