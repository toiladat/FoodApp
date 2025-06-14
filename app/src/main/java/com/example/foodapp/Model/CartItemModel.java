package com.example.foodapp.Model;

public class CartItemModel {
    private FoodModel food;           // Thông tin món ăn
    private int quantity;             // Số lượng
    private CouponModel coupon;       // Mã giảm giá áp dụng (nếu có)


    public CartItemModel() {}

    // Getter & Setter cho FoodModel
    public FoodModel getFood() {
        return food;
    }

    public void setFood(FoodModel food) {
        this.food = food;
    }

    // Getter & Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter & Setter cho CouponModel
    public CouponModel getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponModel coupon) {
        this.coupon = coupon;
    }

    // Hàm tính tổng giá tiền (áp dụng giảm giá nếu có coupon)
    public double getTotalPrice() {
        double originalPrice = food.getPrice() * quantity;
        if (coupon != null && coupon.isActive()) {
            double discount = coupon.getDiscountValue();
            return Math.max(originalPrice - discount, 0); // tránh âm tiền
        }
        return originalPrice;
    }
}
