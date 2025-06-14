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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class UserpageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFoods, recyclerViewCategories;
    private FoodAdapter foodAdapter;
    private CategoryAdapter categoryAdapter;
    private TextView textViewUserName;
    private ImageView btnLogout;
    private static final String TAG = "UserPage";

    private FirebaseAuth mAuth; // Thêm biến FirebaseAuth
    private DatabaseReference mUsersRef; // Thêm biến tham chiếu đến nút "Users"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        // Khởi tạo Firebase Auth và Database Reference
        mAuth = FirebaseAuth.getInstance();
        mUsersRef = FirebaseDatabase.getInstance().getReference("Users");

        initViews();

        // Tải dữ liệu tĩnh (đồ ăn, danh mục)
        loadFoodList();
        loadCategoryList();

        // Thiết lập sự kiện cho nút Logout
        setupLogoutButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra trạng thái người dùng mỗi khi Activity được hiển thị
        checkUserStatus();
    }

    private void checkUserStatus() {
        // HỎI TRỰC TIẾP FIREBASE AUTHENTICATION
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Trường hợp 1: Không có ai đăng nhập
            Toast.makeText(this, "Bạn chưa đăng nhập.", Toast.LENGTH_SHORT).show();

            // Chuyển về Login và xóa hết các activity cũ
            Intent intent = new Intent(UserpageActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Đóng UserpageActivity
        } else {
            // Trường hợp 2: Có người dùng đang đăng nhập
            // -> Lấy UID trực tiếp từ currentUser và tải thông tin
            String uid = currentUser.getUid();
            Log.d(TAG, "User " + uid + " is logged in. Loading user name.");
            loadUserName(uid);
        }
    }

    private void setupLogoutButton() {
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // GỌI HÀM ĐĂNG XUẤT CHUẨN CỦA FIREBASE
            mAuth.signOut();

            // Sau khi đăng xuất, AuthState sẽ tự động thay đổi
            // Chuyển người dùng về trang Login
            Toast.makeText(this, "Đã đăng xuất.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserpageActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        // Dùng biến mUsersRef đã khởi tạo
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
