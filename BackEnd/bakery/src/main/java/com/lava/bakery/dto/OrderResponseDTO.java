package com.lava.bakery.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderResponseDTO {

    private Long orderId;
    private String customerName;
    private String cakeName;
    private double orderedKg;
    private double totalPrice;
    private String status;
    private String paymentMethod;
    private String deliveryAddress;
    private String phoneNumber;
    private LocalDateTime orderDate;
    private String eggType;
    private LocalDate deliveryDate;
    private String deliverySlot;
    private String imageUrl;
    private String cakeMessage;
    private String cakeShape;
     private double cakePrice;
    private double deliveryCharge;
    private Long deliveryBoyId;
    private String deliveryBoyName;
    private String deliveryBoyPhone;
    public OrderResponseDTO(Long orderId,
                            String cakeName,
                            double orderedKg,
                            double totalPrice,
                            String status,
                            String paymentMethod,
                            String deliveryAddress,
                            String phoneNumber,
                            LocalDateTime orderDate,
                            String eggType,
                            LocalDate deliveryDate,
                            String deliverySlot,
                            String imageUrl,
                            String cakeMessage,
                            String cakeShape,
                            String customerName,
                            double cakePrice,
                            double deliveryCharge,
                            Long deliveryBoyId,
                            String deliveryBoyName,
                            String deliveryBoyPhone) {

        this.orderId = orderId;

        this.cakeName = cakeName;
        this.orderedKg = orderedKg;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
        this.orderDate = orderDate;
        this.eggType = eggType;

        this.deliveryDate = deliveryDate;
        this.deliverySlot = deliverySlot;
        this.imageUrl = imageUrl;
        this.cakeMessage = cakeMessage;
        this.cakeShape = cakeShape;
        this.customerName = customerName;
        this.cakePrice = cakePrice;
        this.deliveryCharge = deliveryCharge;
        this.deliveryBoyId = deliveryBoyId;
        this.deliveryBoyName = deliveryBoyName;
        this.deliveryBoyPhone = deliveryBoyPhone;
    }

    public Long getOrderId() { return orderId; }
    public String getCakeName() { return cakeName; }
    public double getOrderedKg() { return orderedKg; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getEggType() { return eggType; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public String getDeliverySlot() { return deliverySlot; }
    public String getImageUrl() { return imageUrl; }

    public String getCakeMessage() {
        return cakeMessage;
    }

    public double getCakePrice() {
        return cakePrice;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public String getCakeShape() {
        return cakeShape;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Long getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public String getDeliveryBoyPhone() {
        return deliveryBoyPhone;
    }
}