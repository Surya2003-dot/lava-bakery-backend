package com.lava.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cakes")
public class Cake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String flavour;

    @Column(nullable = false)
    private String eggType;

    @Column(nullable = false)
    private double pricePerKg;


    private double oldPrice;

    private int offerPercentage;

    private String badge;

    private double minOrderKg;
    private double maxOrderKg;

    private boolean available = true;

    private int preparationTimeHours;
    private double dailyCapacityKg;
    @Column(length = 500)
    private String imageUrl;

    private boolean featured = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private double rating = 0;
    private int reviewCount = 0;

    @Column(length = 100)
    private String weights;

    @Column(length = 100)
    private String shapes;
    public Cake() {}

    public Cake(String name,
                String description,
                String category,
                String flavour,
                String eggType,
                double pricePerKg,
                double oldPrice,
                int offerPercentage,
                String badge,
                double minOrderKg,
                double maxOrderKg,
                boolean available,
                int preparationTimeHours,
                double dailyCapacityKg,
                String imageUrl,
                boolean featured,
                double rating,
                int reviewCount,
                String weights,
                String shapes) {

        this.name = name;
        this.description = description;
        this.category = category;
        this.flavour = flavour;
        this.eggType = eggType;
        this.pricePerKg = pricePerKg;
        this.oldPrice = oldPrice;
        this.offerPercentage = offerPercentage;
        this.badge = badge;
        this.minOrderKg = minOrderKg;
        this.maxOrderKg = maxOrderKg;
        this.available = available;
        this.preparationTimeHours = preparationTimeHours;
        this.dailyCapacityKg = dailyCapacityKg;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.rating =rating;
        this.reviewCount = reviewCount;
        this.weights = weights;
        this.shapes = shapes;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getFlavour() { return flavour; }
    public void setFlavour(String flavour) { this.flavour = flavour; }

    public String getEggType() { return eggType; }
    public void setEggType(String eggType) { this.eggType = eggType; }

    public double getPricePerKg() { return pricePerKg; }
    public void setPricePerKg(double pricePerKg) { this.pricePerKg = pricePerKg; }

    public double getOldPrice() { return oldPrice; }
    public void setOldPrice(double oldPrice) { this.oldPrice = oldPrice; }

    public int getOfferPercentage() { return offerPercentage; }
    public void setOfferPercentage(int offerPercentage) { this.offerPercentage = offerPercentage; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public double getMinOrderKg() { return minOrderKg; }
    public void setMinOrderKg(double minOrderKg) { this.minOrderKg = minOrderKg; }

    public double getMaxOrderKg() { return maxOrderKg; }
    public void setMaxOrderKg(double maxOrderKg) { this.maxOrderKg = maxOrderKg; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public int getPreparationTimeHours() { return preparationTimeHours; }
    public void setPreparationTimeHours(int preparationTimeHours) { this.preparationTimeHours = preparationTimeHours; }

    public double getDailyCapacityKg() { return dailyCapacityKg; }
    public void setDailyCapacityKg(double dailyCapacityKg) { this.dailyCapacityKg = dailyCapacityKg; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getShapes() {
        return shapes;
    }

    public void setShapes(String shapes) {
        this.shapes = shapes;
    }
}