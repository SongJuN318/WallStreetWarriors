
package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.BuyPendingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyPendingOrderRepository extends JpaRepository<BuyPendingOrder, Integer> {
    // Add any additional methods for custom query or operations if needed

    @Override
    <S extends BuyPendingOrder> S save(S entity);

    @Override
    void deleteById(Integer id);
}