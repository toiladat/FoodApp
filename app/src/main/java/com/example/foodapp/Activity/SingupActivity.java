package com.example.foodapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.R;

public class SingupActivity extends AppCompatActivity {

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

    }
}