package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;  // Cambiado a String porque la API usa IDs como "5LyQpt"
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private String availability; // "InStock" o "OutOfStock"

    // Constructor vacío
    public Product() {
        this.availability = "InStock";
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}


