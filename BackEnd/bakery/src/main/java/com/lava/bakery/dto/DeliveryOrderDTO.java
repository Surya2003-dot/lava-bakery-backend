package com.lava.bakery.dto;

import java.time.LocalDateTime;

public class DeliveryOrderDTO {

    private Long orderId;
    private String customerName;
    private String address;
    private Double totalAmount;
    private double cakePrice;
    private double deliveryCharge;
    public DeliveryOrderDTO(Long orderId, String customerName, String address, Double totalAmount, LocalDateTime deliveredAt, double cakePrice, double deliveryCharge) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.totalAmount = totalAmount;
        this.deliveredAt = deliveredAt;
        this.cakePrice = cakePrice;
        this.deliveryCharge = deliveryCharge;
    }

    public DeliveryOrderDTO() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    private LocalDateTime deliveredAt;

    public double getCakePrice() {
        return cakePrice;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }
}