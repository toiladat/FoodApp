package com.example.foodapp;

public class CartModel {
    private String name;
    private double price;
    private int quantity;
    private int imageResource;

    public CartModel() {
        // Firebase cần constructor rỗng
    }

    public CartModel(String name, double price, int quantity, int imageResource) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getImageResource() { return imageResource; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }
}
