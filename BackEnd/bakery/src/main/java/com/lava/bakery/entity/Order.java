package com.lava.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cake_id", nullable = false)
    private Cake cake;


    @Column(nullable = false)
    private double orderedKg;


    @Column(nullable = false)
    private double totalPrice;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;


    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String phoneNumber;


    @Column(name = "egg_type")
    private String eggType;


    @Column(name = "cake_message")
    private String cakeMessage;


    private LocalDate deliveryDate;


    private String deliverySlot;
    @Column(length = 50)
    private String cakeShape;
    @Column(nullable = true)
    private String customerName;
    @Column(nullable = false)
    private double cakePrice;

    @Column(nullable = false)
    private double deliveryCharge;
    private Long deliveryBoyId;
    private String deliveryBoyName;
    private String deliveryBoyPhone;
    public Order() {}

    public Order(String userEmail,
                 User user,
                 Cake cake,
                 double orderedKg,
                 double totalPrice,
                 OrderStatus status,
                 PaymentMethod paymentMethod,
                 String deliveryAddress,
                 String phoneNumber,
                 String eggType,
                 String cakeMessage,
                 LocalDate deliveryDate,
                 String deliverySlot,
                 String cakeShape,
                 double cakePrice,
                 double deliveryCharge
                ) {

        this.userEmail = userEmail;
        this.user = user;
        this.cake = cake;
        this.orderedKg = orderedKg;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
        this.eggType = eggType;
        this.cakeMessage = cakeMessage;
        this.deliveryDate = deliveryDate;
        this.deliverySlot = deliverySlot;
        this.cakeShape = cakeShape;
        this.cakePrice = cakePrice;
        this.deliveryCharge = deliveryCharge;




    }


    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Cake getCake() {
        return cake;
    }

    public double getOrderedKg() {
        return orderedKg;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEggType() {
        return eggType;
    }

    public String getCakeMessage() {
        return cakeMessage;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliverySlot() {
        return deliverySlot;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setEggType(String eggType) {
        this.eggType = eggType;
    }

    public void setCakeMessage(String cakeMessage) {
        this.cakeMessage = cakeMessage;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDeliverySlot(String deliverySlot) {
        this.deliverySlot = deliverySlot;
    }
    public String getCakeShape() {
        return cakeShape;
    }

    public void setCakeShape(String cakeShape) {
        this.cakeShape = cakeShape;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getCakePrice() {
        return cakePrice;
    }

    public void setCakePrice(double cakePrice) {
        this.cakePrice = cakePrice;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Long getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(Long deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public void setDeliveryBoyName(String deliveryBoyName) {
        this.deliveryBoyName = deliveryBoyName;
    }

    public String getDeliveryBoyPhone() {
        return deliveryBoyPhone;
    }

    public void setDeliveryBoyPhone(String deliveryBoyPhone) {
        this.deliveryBoyPhone = deliveryBoyPhone;
    }
}