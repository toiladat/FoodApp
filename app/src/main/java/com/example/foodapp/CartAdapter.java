package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartModel> cartList;
    private OnQuantityChangeListener quantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CartAdapter(Context context, List<CartModel> cartList, OnQuantityChangeListener listener) {
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
        CartModel item = cartList.get(position);
        holder.textName.setText(item.getName());
        holder.textPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.textQuantity.setText("x" + item.getQuantity());
        // TẢI ẢNH TỪ URL
        Glide.with(holder.itemView.getContext())
                .load(CartModel.getImageUrl()) // <-- Đường dẫn ảnh động từ Firebase
                .placeholder(R.drawable.ic_launcher_background) // ảnh chờ trong lúc tải
                .error(R.drawable.ic_error) // ảnh hiển thị nếu tải lỗi
                .into(holder.imageFood); // imageFood là ID trong item layout
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice, textQuantity;
        ImageView imageView;

        public CartViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            imageView = itemView.findViewById(R.id.imageFood);
        }
    }
}
