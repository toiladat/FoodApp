package com.example.foodapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Adapter.FoodAdapter;
import com.example.foodapp.Model.CategoryItem;
import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class UserpageActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewFoods, recyclerViewCategories;
    private FoodAdapter foodAdapter;
    private CategoryAdapter categoryAdapter;
    private TextView textViewUserName;

    ImageView btnLogout;
    private static final String TAG = "UserPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        btnLogout = findViewById(R.id.btnLogout);

        initViews();
        loadFoodList();
        loadCategoryList();

        // Lấy UID đã lưu sau khi đăng nhập
        String uid = getSharedPreferences("MyApp", MODE_PRIVATE).getString("uid", null);

        if (uid == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserpageActivity.this, LoginActivity.class));
            finish();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        loadUserName(uid);

        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("FoodApp", MODE_PRIVATE).edit().remove("uid").apply();

            Intent intent = new Intent(UserpageActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack trước
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        textViewUserName = findViewById(R.id.textViewUserName);
        recyclerViewFoods = findViewById(R.id.recyclerViewFoods);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
    }

    private void loadUserName(String uid) {
        mDatabase.child(uid).child("name").get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        textViewUserName.setText(snapshot.getValue(String.class));
                    } else {
                        textViewUserName.setText("Không tìm thấy tên.");
                    }
                })
                .addOnFailureListener(e -> {
                    textViewUserName.setText("Lỗi tải tên.");
                    Log.e(TAG, "Lỗi khi load tên người dùng", e);
                });
    }

    private void loadFoodList() {
        List<FoodItem> foodList = new ArrayList<>();
        foodList.add(new FoodItem(R.drawable.chicagohotdog, "Chili Cheese Dog", "4.6", "10-15 min", "40$"));
        foodList.add(new FoodItem(R.drawable.margherita, "Classic Sandwich", "4.9", "12-18 min", "35$"));
        foodList.add(new FoodItem(R.drawable.sandwich, "Pizza Slice", "4.5", "10-20 min", "25$"));

        foodAdapter = new FoodAdapter(foodList);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFoods.setAdapter(foodAdapter);
    }

    private void loadCategoryList() {
        List<CategoryItem> categoryList = new ArrayList<>();
        categoryList.add(new CategoryItem(R.drawable.btn_1, "Pizza", Color.parseColor("#E1BEE7")));
        categoryList.add(new CategoryItem(R.drawable.btn_2, "Burger", Color.parseColor("#B3E5FC")));
        categoryList.add(new CategoryItem(R.drawable.btn_3, "Chicken", Color.parseColor("#FFCCBC")));
        categoryList.add(new CategoryItem(R.drawable.btn_4, "Sushi", Color.parseColor("#C8E6C9")));
        categoryList.add(new CategoryItem(R.drawable.btn_5, "Meat", Color.parseColor("#F8BBD0")));
        categoryList.add(new CategoryItem(R.drawable.btn_6, "Hotdog", Color.parseColor("#DCEDC8")));
        categoryList.add(new CategoryItem(R.drawable.btn_7, "Drink", Color.parseColor("#BBDEFB")));
        categoryList.add(new CategoryItem(R.drawable.btn_8, "More", Color.parseColor("#E0F2F7")));

        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewCategories.setAdapter(categoryAdapter);
    }

}
