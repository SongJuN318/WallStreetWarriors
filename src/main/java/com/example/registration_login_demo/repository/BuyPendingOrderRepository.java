package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.BuyPendingOrder;
import com.example.registration_login_demo.entity.BuyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyPendingOrderRepository extends JpaRepository<BuyPendingOrder, Integer> {

    @Override
    <S extends BuyPendingOrder> S save(S entity);

    @Override
    void deleteById(Integer id);

    List<BuyPendingOrder> findByUser(BuyUser user);
}