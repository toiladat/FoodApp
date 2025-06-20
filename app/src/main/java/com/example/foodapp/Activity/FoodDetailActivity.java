package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.foodapp.Model.CartItemModel;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView imgFood;
    private TextView tvTitle, tvPrice, tvDescription, tvTotalPrice, tvQuantity;
    private RatingBar ratingBar;
    private ImageButton btnMinus, btnPlus;
    private Button btnAddToCart;

    private int quantity = 1;
    private double pricePerUnit = 0;
    private String foodId;
    private FoodModel food;

    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Ánh xạ View
        imgFood = findViewById(R.id.imgFood);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        ratingBar = findViewById(R.id.ratingBar);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);

        // Nhận ID món ăn từ intent
        foodId = getIntent().getStringExtra("foodId");

        if (foodId == null || foodId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy món ăn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadFoodDetail(foodId);

        // Xử lý tăng giảm số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndPrice();
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndPrice();
        });

        btnAddToCart.setOnClickListener(v -> addToCart());

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDetailActivity.this, UserpageActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadFoodDetail(String foodId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Foods").child(foodId);
        ref.get().addOnSuccessListener(snapshot -> {
            food = snapshot.getValue(FoodModel.class);
            if (food != null) {
                Glide.with(this).load(food.getImagePath()).into(imgFood);
                tvTitle.setText(food.getTitle());
                tvDescription.setText(food.getDescription());
                ratingBar.setRating((float) food.getStar());
                pricePerUnit = food.getPrice();
                tvPrice.setText(String.format("$%.2f", pricePerUnit));
                updateQuantityAndPrice();
            } else {
                Toast.makeText(this, "Không tìm thấy dữ liệu món ăn", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateQuantityAndPrice() {
        tvQuantity.setText(String.valueOf(quantity));
        double total = pricePerUnit * quantity;
        tvTotalPrice.setText(String.format("$%.2f", total));
    }

    private void addToCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        CartItemModel cartItem = new CartItemModel();
        cartItem.setFood(food);         // `food` là biến đã lấy từ Firebase ở `loadFoodDetail()`
        cartItem.setQuantity(quantity); // số lượng đã chọn
        cartItem.setCoupon(null);       // nếu chưa có mã giảm giá

        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("Carts").child(uid).child("items").child(foodId);

        cartRef.setValue(cartItem)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Thêm thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}
