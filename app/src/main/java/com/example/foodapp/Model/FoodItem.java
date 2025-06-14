package com.example.foodapp.Model;

public class FoodItem {
    private int imageResId;
    private String name;
    private String rating;
    private String deliveryTime;
    private String price;

    public FoodItem(int imageResId, String name, String rating, String deliveryTime, String price) {
        this.imageResId = imageResId;
        this.name = name;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.price = price;
    }

    // Getters
    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getPrice() {
        return price;
    }
}