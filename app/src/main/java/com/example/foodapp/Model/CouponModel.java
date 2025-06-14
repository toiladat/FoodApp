package com.example.foodapp.Model;

public class CouponModel {
    private String code;
    private String description;
    private long discountValue;
    private long startDate;
    private long endDate;
    private boolean isActive;

    public CouponModel() {}

    // Getter & Setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getDiscountValue() { return discountValue; }
    public void setDiscountValue(long discountValue) { this.discountValue = discountValue; }

    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }

    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
