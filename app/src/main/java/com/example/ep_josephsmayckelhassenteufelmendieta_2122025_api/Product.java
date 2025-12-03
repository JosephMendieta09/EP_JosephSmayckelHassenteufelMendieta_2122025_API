package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

public class Product {
    private String id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;

    public Product() {}

    public Product(String id, String title, double price, String description,
                   String category, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImage() { return image; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setImage(String image) { this.image = image; }
}

