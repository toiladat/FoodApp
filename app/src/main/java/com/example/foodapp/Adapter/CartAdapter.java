package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.CartItemModel;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItemModel> cartList;
    private List<FoodModel> foodList;
    private OnQuantityChangeListener quantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CartAdapter(Context context, List<CartItemModel> cartList, OnQuantityChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.quantityChangeListener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItemModel item = cartList.get(position); // lấy item tại vị trí hiện tại
        FoodModel food = item.getFood();
        holder.textName.setText(food.getTitle());
        holder.textPrice.setText(String.format("$%.2f", food.getPrice()));
        holder.textQuantity.setText("x" + item.getQuantity());
        // TẢI ẢNH TỪ URL
        Glide.with(holder.itemView.getContext())
                .load(food.getImagePath()) // <-- Đường dẫn ảnh động từ Firebase
                .placeholder(R.drawable.ic_launcher_background) // ảnh chờ trong lúc tải
                .error(R.drawable.ic_error) // ảnh hiển thị nếu tải lỗi
                .into(holder.imageView); // imageFood là ID trong item layout

        holder.btnPlus.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            item.setQuantity(quantity + 1);
            holder.textQuantity.setText(String.valueOf(item.getQuantity()));

            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged(); // Gọi callback để cập nhật tổng tiền
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            if (quantity > 1) {
                item.setQuantity(quantity - 1);
                holder.textQuantity.setText(String.valueOf(item.getQuantity()));
            }
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged(); // Gọi callback để cập nhật tổng tiền
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice, textQuantity;
        ImageView imageView;

        ImageButton btnMinus, btnPlus;

        public CartViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            imageView = itemView.findViewById(R.id.imageFood);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }
}
