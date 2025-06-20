package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.foodapp.Model.OrderModel;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        textSubtotal = findViewById(R.id.textSubtotal);
        textDelivery = findViewById(R.id.textDelivery);
        textTotal = findViewById(R.id.textTotal);
        iconBack = findViewById(R.id.iconBack);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList, this);
        recyclerViewCartItems.setAdapter(cartAdapter);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(uid).child("items");

        loadCartFromFirebase();

        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, UserpageActivity.class);
            startActivity(intent);
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
                subtotal += item.getFood().getPrice() * item.getQuantity();
            }

            // ✅ Chuyển sang OrderActivity và truyền dữ liệu
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
            intent.putExtra("total", subtotal + DELIVERY_FEE);
            intent.putExtra("uid", uid);

            Bundle bundle = new Bundle();
            bundle.putSerializable("cartList", new ArrayList<>(cartList)); // đảm bảo CartItemModel implements Serializable
            intent.putExtras(bundle);

            startActivity(intent);
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

                                CartItemModel model = new CartItemModel();
                                model.setQuantity(quantity);

                                FoodModel food = new FoodModel();
                                food.setTitle(title != null ? title : "");
                                food.setImagePath(image != null ? image : "");
                                food.setPrice(price != null ? price : 0.0);
                                food.setFoodId(foodId);

                                model.setFood(food);
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
