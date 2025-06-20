package com.example.foodapp.Model;

public class CategoryItem {
    private String id;  // ✅ Thêm ID
    private String imagePath;
    private String name;
    private int backgroundColor;

    public CategoryItem() {
        // Firebase yêu cầu constructor rỗng
    }

    public CategoryItem(String id, String imagePath, String name, int backgroundColor) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.backgroundColor = backgroundColor;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
