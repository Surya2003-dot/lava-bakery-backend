package com.lava.bakery.dto;
public class CartResponseDTO {

    private Long id;
    private Long cakeId;
    private String name;
    private double weight;
    private double price;
    private String image;
    private double rating;
    private int reviewCount;
    private String shapes;
    private String badge;
    private int preparationTimeHours;
    public CartResponseDTO(Long id, Long cakeId, String name,
                           double weight, double price, String image, double rating,
                           int reviewCount, String shapes, String badge, int preparationTimeHours){

        this.id = id;
        this.cakeId = cakeId;
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.image = image;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.shapes = shapes;
        this.badge = badge;
        this.preparationTimeHours = preparationTimeHours;

    }

    public Long getId() {
        return id;
    }

    public Long getCakeId() {
        return cakeId;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getShapes() {
        return shapes;
    }

    public String getBadge() {
        return badge;
    }

    public int getPreparationTimeHours() {
        return preparationTimeHours;
    }

    public String getImage() {
        return image;
    }
}