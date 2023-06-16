package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.BuyPendingOrder;
import com.example.registration_login_demo.entity.TradingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingHistoryRepository extends JpaRepository<TradingHistory, Integer> {

    @Override
    <S extends TradingHistory> S save(S entity);

}