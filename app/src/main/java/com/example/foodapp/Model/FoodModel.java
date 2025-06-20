package com.example.foodapp.Model;

import java.io.Serializable;

public class FoodModel implements Serializable {
    private String foodId;
    private String Title;
    private String Description;
    private String ImagePath;
    private double Price;
    private double Star;
    private String CategoryId;
    private boolean BestFood;

    public FoodModel() {}

    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public String getTitle() { return Title; }
    public void setTitle(String title) { Title = title; }

    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }

    public String getImagePath() { return ImagePath; }
    public void setImagePath(String imagePath) { ImagePath = imagePath; }

    public double getPrice() { return Price; }
    public void setPrice(double price) { Price = price; }

    public double getStar() { return Star; }
    public void setStar(double star) { Star = star; }

    public String getCategoryId() { return CategoryId; }
    public void setCategoryId(String categoryId) { CategoryId = categoryId; }

    public boolean isBestFood() { return BestFood; }
    public void setBestFood(boolean bestFood) { BestFood = bestFood; }
}
