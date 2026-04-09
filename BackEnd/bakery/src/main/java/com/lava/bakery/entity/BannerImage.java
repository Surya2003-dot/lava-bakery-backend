package com.lava.bakery.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "banner_images")
public class BannerImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private BannerType type;

    private int position;

    private boolean active = true;

    private String link; //  redirect URL

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public BannerType getType() { return type; }
    public void setType(BannerType type) { this.type = type; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}