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
import com.example.foodapp.Model.CartItemModel;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;
import com.google.firebase.database.*;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private final Context context;
    private final List<CartItemModel> itemList;

    public OrderItemAdapter(Context context, List<CartItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        CartItemModel item = itemList.get(position);

        holder.textQuantity.setText("Số lượng: " + item.getQuantity());
        holder.textPrice.setText(String.format("Giá: %.0fđ", item.getPrice() * item.getQuantity()));
        holder.textFoodName.setText("Đang tải...");

        // Truy vấn Firebase để lấy chi tiết món ăn theo foodId
        FirebaseDatabase.getInstance()
                .getReference("Foods")
                .child(item.getFoodId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FoodModel food = snapshot.getValue(FoodModel.class);
                        if (food != null) {
                            holder.textFoodName.setText(food.getTitle());
                            Glide.with(context)
                                    .load(food.getImagePath())
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(holder.imageFood);
                        } else {
                            holder.textFoodName.setText("Không tìm thấy món");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.textFoodName.setText("Lỗi tải món");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder cho từng item trong hoá đơn
    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView textFoodName, textQuantity, textPrice;
        ImageView imageFood;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
            imageFood = itemView.findViewById(R.id.imageFood);
        }
    }
}
