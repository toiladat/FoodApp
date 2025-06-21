package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.OrderItemAdapter;
import com.example.foodapp.Model.CartItemModel;
import com.example.foodapp.Model.OrderModel;
import com.example.foodapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBillItems;
    private TextView textTotalAmount;
    private EditText editTextCouponCode, editTextAddress, editTextPhone;
    private Button btnPay, btnPayWithMomo;

    private List<CartItemModel> cartItemList;
    private String uid;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerViewBillItems = findViewById(R.id.recyclerViewBillItems);
        textTotalAmount = findViewById(R.id.textTotalAmount);
        editTextCouponCode = findViewById(R.id.editTextCouponCode);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnPay = findViewById(R.id.btnPay);
        btnPayWithMomo = findViewById(R.id.btnPayWithMomo);

        // Nhận dữ liệu từ CartActivity
        total = getIntent().getDoubleExtra("total", 0);
        uid = getIntent().getStringExtra("uid");
        cartItemList = (List<CartItemModel>) getIntent().getSerializableExtra("cartList");

        textTotalAmount.setText(String.format("Tổng: %.0fđ", total));
        recyclerViewBillItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBillItems.setAdapter(new OrderItemAdapter(this, cartItemList));

        btnPay.setOnClickListener(v -> {
            String couponCode = editTextCouponCode.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ địa chỉ và số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đơn hàng
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
            String orderId = orderRef.push().getKey();

            OrderModel order = new OrderModel();
            order.setUserId(uid);
            order.setTotal(total);
            order.setCreatedAt(System.currentTimeMillis());
            order.setStatus("pending");
            order.setCouponId(couponCode.isEmpty() ? "0" : couponCode);
            order.setAddress(address);
            order.setPhone(phone);
            order.setItems(cartItemList);

            assert orderId != null;
            orderRef.child(orderId).setValue(order)
                    .addOnSuccessListener(unused -> {
                        // Xoá giỏ hàng của user sau khi đặt hàng thành công
                        FirebaseDatabase.getInstance().getReference("Carts").child(uid).removeValue();

                        Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

                        // Quay về trang chính
                        Intent intent = new Intent(OrderActivity.this, UserpageActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        btnPayWithMomo.setOnClickListener(v -> {
            // TODO: Xử lý thanh toán bằng MoMo tại đây
            Toast.makeText(this, "Tính năng MoMo chưa được tích hợp", Toast.LENGTH_SHORT).show();
        });
    }
}
