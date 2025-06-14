package com.example.foodapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageButton twitterButton;
    private ImageButton googleButton;
    private ImageButton facebookButton;
    private TextView signUpTextView;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        twitterButton = findViewById(R.id.buttonTwitter);
        googleButton = findViewById(R.id.buttonGoogle);
        facebookButton = findViewById(R.id.buttonFacebook);
        signUpTextView = findViewById(R.id.textViewSignUp);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        SharedPreferences prefs = getSharedPreferences("FoodApp", MODE_PRIVATE);
        String savedUid = prefs.getString("uid", null);
        if (savedUid != null) {
            startActivity(new Intent(LoginActivity.this, UserpageActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra đăng nhập thủ công
            usersRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean isFound = false;

                    for (DataSnapshot userSnap : task.getResult().getChildren()) {
                        String dbEmail = userSnap.child("email").getValue(String.class);
                        String dbPassword = userSnap.child("password").getValue(String.class);

                        if (email.equals(dbEmail) && password.equals(dbPassword)) {
                            isFound = true;
                            String uid = userSnap.getKey();

                            // Lưu UID vào SharedPreferences
                            getSharedPreferences("MyApp", MODE_PRIVATE).edit().putString("uid", uid).apply();

                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            goToUserPage();
                            break;
                        }
                    }

                    if (!isFound) {
                        Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Lỗi kết nối Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        });

        signUpTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SingupActivity.class));
        });

        twitterButton.setOnClickListener(v -> Toast.makeText(this, "Twitter login clicked", Toast.LENGTH_SHORT).show());
        googleButton.setOnClickListener(v -> Toast.makeText(this, "Google login clicked", Toast.LENGTH_SHORT).show());
        facebookButton.setOnClickListener(v -> Toast.makeText(this, "Facebook login clicked", Toast.LENGTH_SHORT).show());
    }

    private void goToUserPage() {
        Intent intent = new Intent(LoginActivity.this, UserpageActivity.class);
        startActivity(intent);
        finish();
    }
}
