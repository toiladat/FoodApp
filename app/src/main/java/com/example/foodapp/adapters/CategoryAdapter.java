package com.example.foodapp.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.models.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryItem> categoryList;

    public CategoryAdapter(List<CategoryItem> categoryList) {
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
        holder.imageViewCategory.setImageResource(categoryItem.getIconResId());
        holder.textViewCategoryName.setText(categoryItem.getName());

        // Set background color dynamically
        GradientDrawable backgroundDrawable = (GradientDrawable) holder.imageViewCategory.getBackground();
        if (backgroundDrawable != null) {
            backgroundDrawable.setColor(categoryItem.getBackgroundColor());
        }

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Clicked " + categoryItem.getName(), Toast.LENGTH_SHORT).show();
            // Implement category selection logic
        });
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