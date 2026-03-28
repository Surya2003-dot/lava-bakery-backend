package com.lava.bakery.entity;

import jakarta.persistence.*;

@Entity
@Table(name="cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @ManyToOne
    @JoinColumn(name="cake_id")
    private Cake cake;

    private double weight;

    private double price;

    private int quantity;

    public Cart(){}

    public Long getId(){ return id; }

    public String getUserEmail(){ return userEmail; }

    public Cake getCake(){ return cake; }

    public double getWeight(){ return weight; }

    public double getPrice(){ return price; }

    public int getQuantity(){ return quantity; }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public void setCake(Cake cake){
        this.cake = cake;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}