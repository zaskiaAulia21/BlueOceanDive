package com.example.blueoceandive.adapter.process;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.model.CheckoutItem;

public class ProcessViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout llContainer;

    public ProcessViewHolder(@NonNull View itemView) {
        super(itemView);
        llContainer = itemView.findViewById(R.id.ll_container);
    }

    public void bind(CheckoutItem checkoutItem) {
        llContainer.removeAllViews();
        checkoutItem.getCarts().forEach(cartItem -> {
            LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
            View view = layoutInflater.inflate(R.layout.item_process, null, false);
            ImageView ivPackage = view.findViewById(R.id.iv_package);
            TextView title = view.findViewById(R.id.tv_title);
            Glide.with(itemView.getContext()).load(cartItem.getTripPackage().getImageUrl()).into(ivPackage);
            title.setText(cartItem.getTripPackage().getPackageName());
            llContainer.addView(view);
        });

    }
}
