package com.example.foodapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.Model.UserModel;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingupActivity extends AppCompatActivity {
    private static final String TAG = "SingupActivity";

    private EditText nameEditText, emailEditText, passwordEditText, phoneEditText, addressEditText;
    private Button signupButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    
    private TextView signInTextView;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        signInTextView = findViewById(R.id.textViewSignIn);

        signInTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SingupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Ánh xạ View
        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail); 
        passwordEditText = findViewById(R.id.editTextPassword); 
        phoneEditText = findViewById(R.id.editTextPhone); 
        addressEditText = findViewById(R.id.editTextAddress); 
        signupButton = findViewById(R.id.buttonSignup);

        // Thiết lập sự kiện click cho nút Đăng ký
        signupButton.setOnClickListener(v -> createAccount());

    }
    private void createAccount() {
        // Lấy dữ liệu từ các EditText
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bắt đầu quá trình tạo tài khoản với Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // BƯỚC 1: TẠO TÀI KHOẢN AUTH THÀNH CÔNG
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        // BƯỚC 2: TIẾN HÀNH LƯU HỒ SƠ VÀO REALTIME DATABASE
                        saveUserProfile(firebaseUser, name, email, phone, address);
                    } else {
                        // TẠO TÀI KHOẢN AUTH THẤT BẠI
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        // Có thể email đã tồn tại
                        Toast.makeText(SingupActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserProfile(FirebaseUser firebaseUser, String name, String email, String phone, String address) {
        if (firebaseUser == null) {
            return;
        }
        String uid = firebaseUser.getUid();

        // Tạo đối tượng UserModel để lưu
        // Quan trọng: KHÔNG LƯU MẬT KHẨU Ở ĐÂY
        UserModel userProfile = new UserModel(name, email, phone, address);

        // Lưu vào Realtime Database dưới node /Users/{uid}
        databaseReference.child(uid).setValue(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // BƯỚC 3: LƯU DATABASE THÀNH CÔNG -> CHUYỂN TRANG
                        Log.d(TAG, "User profile saved successfully.");
                        Toast.makeText(SingupActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển người dùng đến trang chính
                        Intent intent = new Intent(SingupActivity.this, UserpageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // LƯU DATABASE THẤT BẠI
                        Log.e(TAG, "Failed to save user profile.", task.getException());
                        Toast.makeText(SingupActivity.this, "Lỗi khi lưu hồ sơ người dùng.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


