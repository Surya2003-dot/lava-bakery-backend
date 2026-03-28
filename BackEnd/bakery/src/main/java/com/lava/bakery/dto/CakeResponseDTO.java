package com.lava.bakery.dto;

import jakarta.persistence.Column;

public class CakeResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String flavour;
    private String eggType;

    private double pricePerKg;
    private double oldPrice;
    private int offerPercentage;

    private String badge;
    private int preparationTimeHours;
    private boolean available;
    private String imageUrl;
    private boolean featured;

    private double rating;

    private int reviewCount;

    @Column(length = 100)
    private String weights;

    @Column(length = 100)
    private String shapes;
    private Double dailyCapacityKg;
    private Double minOrderKg;
    private Double maxOrderKg;
    public CakeResponseDTO(Long id,
                           String name,
                           String description,
                           String category,
                           String flavour,
                           String eggType,
                           double pricePerKg,
                           double oldPrice,
                           int offerPercentage,
                           String badge,
                           boolean available,
                           String imageUrl,
                           boolean featured,
                           int preparationTimeHours,
                           double rating,
                           int reviewCount,
                           String weights,
                           String shapes,
                           Double dailyCapacityKg,
                           Double minOrderKg,
                           Double maxOrderKg) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.flavour = flavour;
        this.eggType = eggType;
        this.pricePerKg = pricePerKg;
        this.oldPrice = oldPrice;
        this.offerPercentage = offerPercentage;
        this.badge = badge;
        this.available = available;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.preparationTimeHours =preparationTimeHours;
        this.rating = rating;
        this.reviewCount =reviewCount;
        this.weights = weights;
        this.shapes = shapes;
        this.dailyCapacityKg = dailyCapacityKg;
        this.minOrderKg = minOrderKg;
        this.maxOrderKg = maxOrderKg;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getFlavour() {
        return flavour;
    }

    public String getEggType() {
        return eggType;
    }

    public int getPreparationTimeHours() {
        return preparationTimeHours;
    }

    public void setPreparationTimeHours(int preparationTimeHours) {
        this.preparationTimeHours = preparationTimeHours;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public int getOfferPercentage() {
        return offerPercentage;
    }

    public String getBadge() {
        return badge;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFeatured() {
        return featured;
    }

    public double getRating() {
        return rating;
    }

    public Double getDailyCapacityKg() {
        return dailyCapacityKg;
    }

    public Double getMinOrderKg() {
        return minOrderKg;
    }

    public Double getMaxOrderKg() {
        return maxOrderKg;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getWeights() {
        return weights;
    }

    public String getShapes() {
        return shapes;
    }
}