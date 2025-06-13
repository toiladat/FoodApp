package com.example.foodapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangeListener {

    private List<CartModel> cartList;
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

        cartRef = FirebaseDatabase.getInstance().getReference("cart/items");

        // Gọi hàm tải dữ liệu từ Firebase
        loadCartFromFirebase();

        // Nút đặt hàng
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(v -> {
            Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCartFromFirebase() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartModel model = itemSnapshot.getValue(CartModel.class);
                    if (model != null) {
                        cartList.add(model);
                    }
                }
                cartAdapter.notifyDataSetChanged();
                updateSummary();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateSummary() {
        double subtotal = 0;
        for (CartModel item : cartList) {
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
