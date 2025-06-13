package com.example.foodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R; 
import com.example.foodapp.models.FoodItem;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;

    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_card, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.imageViewFood.setImageResource(foodItem.getImageResId());
        holder.textViewFoodName.setText(foodItem.getName());
        holder.textViewRating.setText(foodItem.getRating());
        holder.textViewDeliveryTime.setText(foodItem.getDeliveryTime());
        holder.textViewPrice.setText(foodItem.getPrice());

        holder.buttonAdd.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Added " + foodItem.getName() + " to cart", Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFood;
        TextView textViewFoodName;
        TextView textViewRating;
        TextView textViewDeliveryTime;
        TextView textViewPrice;
        ImageButton buttonAdd;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFood = itemView.findViewById(R.id.imageViewFood);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewDeliveryTime = itemView.findViewById(R.id.textViewDeliveryTime);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
        }
    }
}