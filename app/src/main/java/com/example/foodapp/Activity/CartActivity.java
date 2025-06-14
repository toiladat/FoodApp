package com.example.foodapp.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.CartAdapter;
import com.example.foodapp.Model.CartItemModel;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangeListener {

    private List<CartItemModel> cartList;
    private TextView textSubtotal, textDelivery, textTotal;
    private RecyclerView recyclerViewCartItems;
    private CartAdapter cartAdapter;

    private static final double DELIVERY_FEE = 3.00;

    private DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ các view
        textSubtotal = findViewById(R.id.textSubtotal);
        textDelivery = findViewById(R.id.textDelivery);
        textTotal = findViewById(R.id.textTotal);

        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách và adapter
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList, this);
        recyclerViewCartItems.setAdapter(cartAdapter);

//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String uid = "uid1"; // Dùng UID test trong Firebase
        cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(uid).child("items");


        // Gọi hàm tải dữ liệu từ Firebase
        loadCartFromFirebase();

        // Nút đặt hàng
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(v -> {

        });
    }

    private void loadCartFromFirebase() {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String foodId = itemSnapshot.getKey();
                    Integer quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                    if (foodId == null || quantity == null || quantity == 0) continue;

                    DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodId);
                    foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot foodSnapshot) {
                            if (foodSnapshot.exists()) {
                                String title = foodSnapshot.child("Title").getValue(String.class);
                                String image = foodSnapshot.child("ImagePath").getValue(String.class);
                                Double price = foodSnapshot.child("Price").getValue(Double.class);

                                if (price == null) price = 0.0;

                                CartItemModel model = new CartItemModel();
                                model.setQuantity(quantity);

                                // ✅ Tạo mới và gán FoodModel
                                FoodModel food = new FoodModel();
                                food.setTitle(title != null ? title : "");
                                food.setImagePath(image != null ? image : "");
                                food.setPrice(price);


                                model.setFood(food); // ✅ Gán food vào model

                                cartList.add(model);
                                cartAdapter.notifyDataSetChanged();
                                updateSummary();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(CartActivity.this, "Lỗi tải món ăn", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void updateSummary() {
        double subtotal = 0;
        for (CartItemModel item : cartList) {
            subtotal += item.getFood().getPrice() * item.getQuantity();
        }

        double total = subtotal + DELIVERY_FEE;

        textSubtotal.setText(String.format("$%.2f", subtotal));
        textDelivery.setText(String.format("$%.2f", DELIVERY_FEE));
        textTotal.setText(String.format("$%.2f", total));
    }

    @Override
    public void onQuantityChanged() {
        updateSummary();
    }
}
