package com.example.foodapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Adapter.FoodAdapter;
import com.example.foodapp.Adapter.FoodListAdapter;
import com.example.foodapp.Model.CategoryItem;
import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class UserpageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFoods, recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private TextView textViewUserName;

    private FoodAdapter foodAdapter;
    private ImageButton buttonCart;
    private ImageView btnLogout;

    private FirebaseAuth mAuth;
    private DatabaseReference mUsersRef;

    private List<FoodItem> foodList = new ArrayList<>(); // ✅ Thêm biến danh sách món ăn

    private static final String TAG = "UserPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        mAuth = FirebaseAuth.getInstance();
        mUsersRef = FirebaseDatabase.getInstance().getReference("Users");

        initViews();

        // Tải dữ liệu từ Firebase
        loadFoodListFromFirebase();
        loadCategoryList();

        setupLogoutButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserpageActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            String uid = currentUser.getUid();
            loadUserName(uid);
        }
    }

    private void setupLogoutButton() {
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Đã đăng xuất.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserpageActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(UserpageActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        textViewUserName = findViewById(R.id.textViewUserName);
        recyclerViewFoods = findViewById(R.id.recyclerViewFoods);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        buttonCart = findViewById(R.id.buttonCart);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void loadUserName(String uid) {
        mUsersRef.child(uid).child("name").get()
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

    private void loadFoodListFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Foods");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FoodModel> foodList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    FoodModel item = data.getValue(FoodModel.class);
                    Boolean isBest = data.child("BestFood").getValue(Boolean.class);

                    // ✅ Chỉ thêm nếu bestfood == true
                    if (item != null && Boolean.TRUE.equals(isBest)) {
                        foodList.add(item);
                    }
                }

                // Gắn adapter khi có dữ liệu
                foodAdapter = new FoodAdapter(UserpageActivity.this, foodList);
                recyclerViewFoods.setLayoutManager(
                        new LinearLayoutManager(UserpageActivity.this, LinearLayoutManager.HORIZONTAL, false)
                );
                recyclerViewFoods.setAdapter(foodAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserpageActivity.this, "Lỗi tải danh sách món ăn", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadCategoryList() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");
        List<CategoryItem> categoryList = new ArrayList<>();

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index = 0;
                String[] colorList = {
                        "#E1BEE7", "#B3E5FC", "#FFCCBC", "#C8E6C9",
                        "#F8BBD0", "#DCEDC8", "#BBDEFB", "#E0F2F7"
                };

                for (DataSnapshot data : snapshot.getChildren()) {
                    String categoryId = data.getKey(); // ✅ lấy "cat1", "cat2", ...
                    String imagePath = data.child("ImagePath").getValue(String.class);
                    String name = data.child("Name").getValue(String.class);
                    int bgColor = Color.parseColor(colorList[index % colorList.length]);
                    index++;

                    // ✅ Truyền categoryId vào constructor
                    CategoryItem item = new CategoryItem(categoryId, imagePath, name, bgColor);
                    categoryList.add(item);
                }

                categoryAdapter = new CategoryAdapter(UserpageActivity.this, categoryList);
                recyclerViewCategories.setLayoutManager(new GridLayoutManager(UserpageActivity.this, 4));
                recyclerViewCategories.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserpageActivity.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
