package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Activity.FoodListActivity;
import com.example.foodapp.R;
import com.example.foodapp.Model.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryItem> categoryList;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryItem> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_grid, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem categoryItem = categoryList.get(position);
        // Lấy ID ảnh từ tên chuỗi "btn_1", "btn_2", ...
        int resId = context.getResources().getIdentifier(categoryItem.getImagePath(), "drawable", context.getPackageName());
        // Đặt ảnh và màu nền
        holder.imageViewCategory.setImageResource(resId);
        holder.itemView.setBackgroundColor(categoryItem.getBackgroundColor());
        holder.textViewCategoryName.setText(categoryItem.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodListActivity.class);
            intent.putExtra("categoryId", categoryItem.getId()); // Truyền CategoryId
            intent.putExtra("categoryName", categoryItem.getName());
            context.startActivity(intent);
        });


        // Set background color dynamically
        GradientDrawable backgroundDrawable = (GradientDrawable) holder.imageViewCategory.getBackground();
        if (backgroundDrawable != null) {
            backgroundDrawable.setColor(categoryItem.getBackgroundColor());
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCategory;
        TextView textViewCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
        }
    }
}