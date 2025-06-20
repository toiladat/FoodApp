package com.example.foodapp.Model;

import java.io.Serializable;

public class CartItemModel implements Serializable {
    private FoodModel food;
    private int quantity;
    private CouponModel coupon;

    public CartItemModel() {}

    public FoodModel getFood() { return food; }
    public void setFood(FoodModel food) { this.food = food; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public CouponModel getCoupon() { return coupon; }
    public void setCoupon(CouponModel coupon) { this.coupon = coupon; }

    public double getTotalPrice() {
        double originalPrice = food.getPrice() * quantity;
        if (coupon != null && coupon.isActive()) {
            double discount = coupon.getDiscountValue();
            return Math.max(originalPrice - discount, 0);
        }
        return originalPrice;
    }
}
