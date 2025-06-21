package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.CartItemModel;
import com.example.foodapp.Model.FoodModel;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItemModel> cartList;
    private Map<String, FoodModel> foodMap;
    private OnQuantityChangeListener quantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CartAdapter(Context context, List<CartItemModel> cartList,
                       Map<String, FoodModel> foodMap,
                       OnQuantityChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.foodMap = foodMap;
        this.quantityChangeListener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItemModel item = cartList.get(position);
        FoodModel food = foodMap.get(item.getFoodId());

        if (food != null) {
            holder.textName.setText(food.getTitle());
            holder.textPrice.setText(String.format("$%.2f", food.getPrice()));
            holder.textQuantity.setText("x" + item.getQuantity());

            Glide.with(holder.itemView.getContext())
                    .load(food.getImagePath())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_error)
                    .into(holder.imageView);
        }

        // Tăng số lượng
        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.textQuantity.setText("x" + newQuantity);
            updateQuantityInFirebase(item.getFoodId(), newQuantity);
            if (quantityChangeListener != null) quantityChangeListener.onQuantityChanged();
        });

        // Giảm số lượng
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                item.setQuantity(newQuantity);
                holder.textQuantity.setText("x" + newQuantity);
                updateQuantityInFirebase(item.getFoodId(), newQuantity);
                if (quantityChangeListener != null) quantityChangeListener.onQuantityChanged();
            }
        });

        // Xoá khỏi giỏ
        holder.btnDelete.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance()
                    .getReference("Carts")
                    .child(uid)
                    .child("items")
                    .child(item.getFoodId());

            cartRef.removeValue().addOnSuccessListener(aVoid -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    cartList.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, cartList.size());
                    Toast.makeText(context, "Đã xoá khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    if (quantityChangeListener != null) quantityChangeListener.onQuantityChanged();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    private void updateQuantityInFirebase(String foodId, int quantity) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartItemRef = FirebaseDatabase.getInstance()
                .getReference("Carts")
                .child(uid)
                .child("items")
                .child(foodId)
                .child("quantity");
        cartItemRef.setValue(quantity);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice, textQuantity;
        ImageView imageView;
        ImageView btnPlus, btnMinus, btnDelete;

        public CartViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            imageView = itemView.findViewById(R.id.imageFood);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
