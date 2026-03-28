package com.lava.bakery.repository;

import com.lava.bakery.entity.DeliveryBoy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryBoyRepository extends JpaRepository<DeliveryBoy, Long> {

    Optional<DeliveryBoy> findByUsername(String username);


}