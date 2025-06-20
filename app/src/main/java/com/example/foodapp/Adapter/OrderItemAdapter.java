package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.CartItemModel;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<CartItemModel> itemList;

    public OrderItemAdapter(Context context, List<CartItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        CartItemModel item = itemList.get(position);
        FoodModel food = item.getFood();

        holder.textFoodName.setText(food.getTitle());
        holder.textQuantity.setText("Số lượng: " + item.getQuantity());
        holder.textPrice.setText(String.format("Giá: %.0fđ", food.getPrice() * item.getQuantity()));

        // Tải ảnh bằng Glide
        Glide.with(context)
                .load(food.getImagePath())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageFood);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView textFoodName, textQuantity, textPrice;
        ImageView imageFood;

        public OrderItemViewHolder(View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
            imageFood = itemView.findViewById(R.id.imageFood);
        }
    }
}
