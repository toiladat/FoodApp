package com.example.foodapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.example.foodapp.adapters.CategoryAdapter;
import com.example.foodapp.adapters.FoodAdapter;
import com.example.foodapp.models.CategoryItem;
import com.example.foodapp.models.FoodItem;

public class UserpageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFoods;
    private RecyclerView recyclerViewCategories;
    private FoodAdapter foodAdapter;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        // Optional: Set the current time
        TextView textViewTime = findViewById(R.id.textViewTime);
        // You'd typically format current time here. For now, hardcoded as in the image.
        // SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
        // String currentTime = sdf.format(new Date());
        // textViewTime.setText(currentTime);

        recyclerViewFoods = findViewById(R.id.recyclerViewFoods);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);

        // Setup Food RecyclerView
        List<FoodItem> foodList = new ArrayList<>();
        foodList.add(new FoodItem(R.drawable.chicagohotdog, "Chili Cheese Dog", "4.6", "10-15 min", "40$"));
        foodList.add(new FoodItem(R.drawable.margherita, "Classic Sandwich", "4.9", "12-18 min", "35$"));
        foodList.add(new FoodItem(R.drawable.sandwich, "Pizza Slice", "4.5", "10-20 min", "25$"));

        foodAdapter = new FoodAdapter(foodList);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFoods.setAdapter(foodAdapter);

        // Setup Category RecyclerView
        List<CategoryItem> categoryList = new ArrayList<>();

        int colorPizza = Color.parseColor("#E1BEE7"); // Light purple
        int colorBurger = Color.parseColor("#B3E5FC"); // Light blue
        int colorChicken = Color.parseColor("#FFCCBC"); // Light orange
        int colorSushi = Color.parseColor("#C8E6C9"); // Light green
        int colorMeat = Color.parseColor("#F8BBD0"); // Light pink
        int colorHotdog = Color.parseColor("#DCEDC8"); // Light lime green
        int colorDrink = Color.parseColor("#BBDEFB"); // Light blue
        int colorMore = Color.parseColor("#E0F2F7"); // Very light blue/gray

        categoryList.add(new CategoryItem(R.drawable.btn_1, "Pizza", colorPizza));
        categoryList.add(new CategoryItem(R.drawable.btn_2, "Burger", colorBurger));
        categoryList.add(new CategoryItem(R.drawable.btn_3, "Chicken", colorChicken));
        categoryList.add(new CategoryItem(R.drawable.btn_4, "Sushi", colorSushi));
        categoryList.add(new CategoryItem(R.drawable.btn_5, "Meat", colorMeat));
        categoryList.add(new CategoryItem(R.drawable.btn_6, "Hotdog", colorHotdog));
        categoryList.add(new CategoryItem(R.drawable.btn_7, "Drink", colorDrink));
        categoryList.add(new CategoryItem(R.drawable.btn_8, "More", colorMore));

        categoryAdapter = new CategoryAdapter(categoryList);
        // GridLayoutManager với 4 cột
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewCategories.setAdapter(categoryAdapter);
    }
}