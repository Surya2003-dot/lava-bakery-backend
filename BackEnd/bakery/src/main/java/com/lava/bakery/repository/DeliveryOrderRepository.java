package com.lava.bakery.repository;

import com.lava.bakery.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

    List<DeliveryOrder> findByDeliveryBoyId(Long deliveryBoyId);

}