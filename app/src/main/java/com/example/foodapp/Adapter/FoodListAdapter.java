package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private Context context;
    private List<FoodModel> foodList;

    public FoodListAdapter(Context context, List<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_burger, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodModel foodItem = foodList.get(position);
        Glide.with(context)
                .load(foodItem.getImagePath())
                .placeholder(R.drawable.margherita)
                .into(holder.imageFood);
        holder.textName.setText(foodItem.getTitle());
        holder.textRating.setText(String.valueOf(foodItem.getStar()));
        holder.textPrice.setText("$" + String.valueOf(foodItem.getPrice()));  // ví dụ: "4.5 ★"

        Glide.with(context)
                .load(foodItem.getImagePath()) // imageUrl là String URL ảnh từ Firebase
                .into(holder.imageFood);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFood;
        TextView textName, textTime, textPrice, textRating;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.imageFood);
            textName = itemView.findViewById(R.id.textName);
            textTime = itemView.findViewById(R.id.textTime);
            textPrice = itemView.findViewById(R.id.textPrice);
            textRating = itemView.findViewById(R.id.textRating);
        }
    }
}
