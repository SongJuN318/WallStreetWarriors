package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.Buy;
import com.example.registration_login_demo.entity.BuyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyRepository extends JpaRepository<Buy, Long> {
    List<Buy> findByUser(BuyUser user);

    Buy findByOrderId(long id);

    Buy findByUserAndOrderId(BuyUser user, long id);
}