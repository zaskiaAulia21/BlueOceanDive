package com.example.blueoceandive.adapter.cart;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.model.CartItem;
import com.example.blueoceandive.util.NumberFormat;

import java.util.Locale;

public class CartViewHolder extends RecyclerView.ViewHolder {
    private CheckBox cbCart;
    private ImageView ivPackage, ivTrash;
    private TextView tvTitle, tvPickupDate, tvTotalDay, tvTourPackage, tvAdditionalShuttleService, tvSubtotal;
    private CartActionListener listener;

    public CartViewHolder(@NonNull View itemView, CartActionListener listener) {
        super(itemView);
        this.listener = listener;
        cbCart = itemView.findViewById(R.id.cb_cart);
        ivPackage = itemView.findViewById(R.id.iv_package);
        ivTrash = itemView.findViewById(R.id.iv_trash);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvPickupDate = itemView.findViewById(R.id.tv_pickup_date);
        tvTotalDay = itemView.findViewById(R.id.tv_total_day);
        tvTourPackage = itemView.findViewById(R.id.tv_tour_package);
        tvAdditionalShuttleService = itemView.findViewById(R.id.tv_additional_shuttle_service);
        tvSubtotal = itemView.findViewById(R.id.tv_subtotal);
    }

    public void bind(CartItem cartItem) {
        Glide.with(itemView.getContext())
                .load(cartItem.getTripPackage().getImageUrl())
                .into(ivPackage);
        cbCart.setOnCheckedChangeListener(null);
        cbCart.setChecked(cartItem.getChecked());
        cbCart.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onCheckboxChanged(cartItem, isChecked);
        });
        ivTrash.setOnClickListener(v -> listener.onDeleteClicked(cartItem));
        tvTitle.setText(cartItem.getTripPackage().getPackageName());
        tvPickupDate.setText(String.format("Pickup Date: %s", cartItem.getPickupDate()));
        tvTotalDay.setText(String.format("Total Day: %s", cartItem.getTripPackage().getFormattedDuration()));
        tvAdditionalShuttleService.setText(cartItem.getPackageAdditionalService().getServiceName());
        tvSubtotal.setText(String.format("Subtotal: %s", NumberFormat.format(cartItem.getSubtotal())));
    }
}