package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.entity.TradingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingHistoryRepository extends JpaRepository<TradingHistory, Long> {

    @Override
    <S extends TradingHistory> S save(S entity);

    List<TradingHistory> findByUser(BuyUser user);

    TradingHistory findByOrderId(long id);
}