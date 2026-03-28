package com.lava.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long deliveryBoyId;

    private LocalDateTime deliveredAt;

    public DeliveryOrder(Long id, Long orderId, Long deliveryBoyId, LocalDateTime deliveredAt) {
        this.id = id;
        this.orderId = orderId;
        this.deliveryBoyId = deliveryBoyId;
        this.deliveredAt = deliveredAt;
    }

    public DeliveryOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(Long deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}