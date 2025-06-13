package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageButton twitterButton;
    private ImageButton googleButton;
    private ImageButton facebookButton;
    private TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        twitterButton = findViewById(R.id.buttonTwitter);
        googleButton = findViewById(R.id.buttonGoogle);
        facebookButton = findViewById(R.id.buttonFacebook);
        signUpTextView = findViewById(R.id.textViewSignUp);

        // Set up click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Here you would implement your login logic (e.g., call an API)
//                    Toast.makeText(LoginActivity.this, "Login attempt with: " + email, Toast.LENGTH_SHORT).show();
//                    // For demonstration, let's assume successful login
//                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    // startActivity(intent);
//                    // finish();
//                }

                Intent intent = new Intent(LoginActivity.this, UserpageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Twitter login clicked", Toast.LENGTH_SHORT).show();
                // Implement Twitter login integration
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Google login clicked", Toast.LENGTH_SHORT).show();
                // Implement Google login integration
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Facebook login clicked", Toast.LENGTH_SHORT).show();
                // Implement Facebook login integration
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SingupActivity.class);
                startActivity(intent);
            }
        });

    }
}