
package com.example.registration_login_demo.repository;

import com.example.registration_login_demo.entity.Buy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyRepository extends JpaRepository<Buy, Integer> {
    // Add any additional methods for custom query or operations if needed
}