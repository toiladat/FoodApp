package com.example.foodapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangeListener {

    private List<CartItemModel> cartList;
    private TextView textSubtotal, textDelivery, textTotal;
    private RecyclerView recyclerViewCartItems;
    private CartAdapter cartAdapter;

    private ImageView iconBack;
    private Button btnPlaceOrder;

    private static final double DELIVERY_FEE = 3.00;
    private DatabaseReference cartRef;
    private String uid;
    private Map<String, FoodModel> foodMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ view
        textSubtotal = findViewById(R.id.textSubtotal);
        textDelivery = findViewById(R.id.textDelivery);
        textTotal = findViewById(R.id.textTotal);
        iconBack = findViewById(R.id.iconBack);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList, foodMap, this);


        recyclerViewCartItems.setAdapter(cartAdapter);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(uid).child("items");

        loadCartFromFirebase();

        iconBack.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, UserpageActivity.class));
            finish();
        });

        btnPlaceOrder.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Tính tổng tiền
            double subtotal = 0;
            for (CartItemModel item : cartList) {
                subtotal += item.getPrice() * item.getQuantity();
            }

            double total = subtotal + DELIVERY_FEE;

            // ✅ Chuyển sang OrderActivity và truyền dữ liệu
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
            intent.putExtra("total", total);
            intent.putExtra("uid", uid);

            Bundle bundle = new Bundle();
            bundle.putSerializable("cartList", new ArrayList<>(cartList));
            intent.putExtras(bundle);

            startActivity(intent);
        });
    }

    private void loadCartFromFirebase() {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                foodMap.clear();  // ← Xóa map cũ để tránh dữ liệu trùng

                int itemCount = (int) snapshot.getChildrenCount();
                if (itemCount == 0) {
                    cartAdapter.notifyDataSetChanged();
                    updateSummary();
                    return;
                }

                final int[] loadedCount = {0};

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String foodId = itemSnapshot.child("foodId").getValue(String.class);
                    Integer quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                    if (foodId == null || quantity == null || quantity == 0) {
                        loadedCount[0]++;
                        if (loadedCount[0] == itemCount) {
                            cartAdapter.notifyDataSetChanged();
                            updateSummary();
                        }
                        continue;
                    }

                    DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodId);
                    foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot foodSnapshot) {
                            if (foodSnapshot.exists()) {
                                FoodModel food = foodSnapshot.getValue(FoodModel.class);
                                if (food != null) {
                                    foodMap.put(foodId, food);  // 💡 Lưu thông tin món ăn

                                    CartItemModel item = new CartItemModel();
                                    item.setFoodId(foodId);
                                    item.setQuantity(quantity);
                                    item.setPrice(food.getPrice());

                                    cartList.add(item);
                                }
                            }

                            loadedCount[0]++;
                            if (loadedCount[0] == itemCount) {
                                cartAdapter.notifyDataSetChanged();
                                updateSummary();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loadedCount[0]++;
                            Toast.makeText(CartActivity.this, "Lỗi khi tải món ăn", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Lỗi khi tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSummary() {
        double subtotal = 0;
        for (CartItemModel item : cartList) {
            subtotal += item.getPrice() * item.getQuantity();
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
