package com.example.foodapp.Model;

public class FoodItem {
    private String imageUrl;       // <-- dùng URL thay vì int resource
    private String name;
    private String rating;
    private String deliveryTime;
    private String price;

    // Constructor mặc định (Firebase yêu cầu)
    public FoodItem() {
    }

    // Constructor đầy đủ
    public FoodItem(String imageUrl, String name, String rating, String deliveryTime, String price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.price = price;
    }

    // Getters
    public String getImageUrl() {
        return imageUrl;
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

    // Setters (cần thiết cho Firebase)
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
