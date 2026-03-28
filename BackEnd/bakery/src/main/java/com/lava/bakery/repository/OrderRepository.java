package com.lava.bakery.repository;

import com.lava.bakery.entity.Order;
import com.lava.bakery.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserEmail(String userEmail, Pageable pageable);

    List<Order> findByUserEmail(String email);

    long countByStatus(OrderStatus status);

    int countByUserEmailAndStatusNot(String userEmail, OrderStatus status);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = com.lava.bakery.entity.OrderStatus.COMPLETED")
    Double getTotalRevenue();
}