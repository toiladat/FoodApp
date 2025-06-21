package com.example.foodapp.Model;

import java.io.Serializable;

public class CartItemModel implements Serializable {
    private String foodId;
    private double price;
    private int quantity;

    public CartItemModel() {}  // Constructor rỗng bắt buộc

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
