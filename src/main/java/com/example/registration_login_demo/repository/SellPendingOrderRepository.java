package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.entity.SellPendingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellPendingOrderRepository extends JpaRepository<SellPendingOrder, Integer> {

    @Override
    <S extends SellPendingOrder> S save(S entity);

    List<SellPendingOrder> findByUser(BuyUser user);
}