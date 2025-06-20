package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;
import com.example.foodapp.Model.FoodItem;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodModel> foodList;
    private Context context;

    public FoodAdapter(Context context, List<FoodModel> foodList) {
        this.context = context;
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
        FoodModel foodItem = foodList.get(position);
        Glide.with(context)
                .load(foodItem.getImagePath())
                .placeholder(R.drawable.margherita)
                .into(holder.imageViewFood);
        holder.textViewFoodName.setText(foodItem.getTitle());
        holder.textViewRating.setText(String.valueOf(foodItem.getStar()));
        holder.textViewPrice.setText("$" + String.valueOf(foodItem.getPrice()));

        holder.buttonAdd.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Added " + foodItem.getTitle() + " to cart", Toast.LENGTH_SHORT).show();

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