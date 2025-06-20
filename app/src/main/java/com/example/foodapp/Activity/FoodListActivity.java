package com.example.foodapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.Adapter.FoodAdapter;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodapp.Adapter.FoodListAdapter;
import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private FoodListAdapter adapter;

    private FoodListAdapter adapter;
    private TextView tvTitle;

    private List<FoodModel> foodList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        recyclerView = findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        tvTitle = findViewById(R.id.tvTitle);

        foodList = new ArrayList<>();
        adapter = new FoodListAdapter(this, foodList);

        recyclerView.setAdapter(adapter);

        loadFoodsFromFirebase();
    }

    private void loadFoodsFromFirebase() {
        // ✅ Lấy categoryId từ Intent
        String categoryId = getIntent().getStringExtra("categoryId");
        String categoryName = getIntent().getStringExtra("categoryName");
        FirebaseDatabase.getInstance().getReference("Foods")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        foodList.clear();
                        for (DataSnapshot foodSnap : snapshot.getChildren()) {
                            FoodModel food = foodSnap.getValue(FoodModel.class);
                            // ✅ Chỉ hiển thị món ăn có cùng CategoryId
                            if (food != null && categoryId != null && categoryId.equals(food.getCategoryId()) && categoryName != null) {
                                foodList.add(food);
                                tvTitle.setText(categoryName);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error: " + error.getMessage());
                    }
                });
    }
}
