package com.example.foodapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangeListener {

    private List<CartModel> CartModel;
    private TextView textSubtotal, textDelivery, textTotal;
    private RecyclerView recyclerViewCartItems;
    private CartAdapter cartAdapter;

    private static final double DELIVERY_FEE = 3.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart); // tên layout của bạn

        textSubtotal = findViewById(R.id.orderSummaryTitle); // Cập nhật sau nếu bạn muốn hiển thị riêng subtotal
        textDelivery = findViewById(R.id.orderSummaryTitle); // tương tự
        textTotal = findViewById(R.id.orderSummaryTitle); // hoặc tạo mới các TextView cho subtotal/delivery/total

        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        CartModel = getMockCartItems();

        cartAdapter = new CartAdapter(this, CartModel, this);
        recyclerViewCartItems.setAdapter(cartAdapter);

        updateSummary();

        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(v -> {
            // Xử lý đặt hàng
        });
    }

    private List<CartModel> getMockCartItems() {
        List<CartModel> items = new ArrayList<>();
        items.add(new CartModel("Classic Beef Burger", 8.99, 1, R.drawable.ic_launcher_background));
        return items;
    }

    private void updateSummary() {
        double subtotal = 0;
        for (CartModel item : CartModel) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        double total = subtotal + DELIVERY_FEE;

        TextView subtotalView = findViewById(R.id.orderSummaryBox).findViewById(R.id.textSubtotal);
        TextView deliveryView = findViewById(R.id.orderSummaryBox).findViewById(R.id.textDelivery);
        TextView totalView = findViewById(R.id.orderSummaryBox).findViewById(R.id.textTotal);

        subtotalView.setText(String.format("$%.2f", subtotal));
        deliveryView.setText(String.format("$%.2f", DELIVERY_FEE));
        totalView.setText(String.format("$%.2f", total));
    }

    @Override
    public void onQuantityChanged() {
        updateSummary();
    }
}
