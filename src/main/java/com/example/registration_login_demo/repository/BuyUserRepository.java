package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.BuyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyUserRepository extends JpaRepository<BuyUser, String> {
    // Add custom query methods if needed
    List<BuyUser> findTopNByOrderByPointDesc(int limit);
}