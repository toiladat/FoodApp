package com.example.foodapp.models;

public class CategoryItem {
    private int iconResId;
    private String name;
    private int backgroundColor; // Để lưu màu nền cho icon

    public CategoryItem(int iconResId, String name, int backgroundColor) {
        this.iconResId = iconResId;
        this.name = name;
        this.backgroundColor = backgroundColor;
    }

    // Getters
    public int getIconResId() {
        return iconResId;
    }

    public String getName() {
        return name;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}