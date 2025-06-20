package com.example.foodapp.Model;

import java.util.List;

public class OrderModel {
    private String userId;
    private String address;
    private String phone;
    private String couponId;
    private long createdAt;
    private double total;
    private String status;
    private List<CartItemModel> items;

    public OrderModel() {}

    // Getter & Setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCouponId() { return couponId; }
    public void setCouponId(String couponId) { this.couponId = couponId; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<CartItemModel> getItems() { return items; }
    public void setItems(List<CartItemModel> items) { this.items = items; }
}

