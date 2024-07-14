package com.example.blueoceandive.adapter.packages;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.model.TripPackage;

import java.util.Locale;

public class PackageViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivPackage;
    private TextView tvTitle, tvDuration, tvGroup, tvReview, tvDesc;
    private LinearLayout llReview;
    private Button btnViewDetail;
    private PackageActionListener listener;

    public PackageViewHolder(@NonNull View itemView, PackageActionListener listener) {
        super(itemView);
        this.listener = listener;
        ivPackage = itemView.findViewById(R.id.iv_package);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvDuration = itemView.findViewById(R.id.tv_duration);
        tvGroup = itemView.findViewById(R.id.tv_group);
        tvReview = itemView.findViewById(R.id.tv_review);
        tvDesc = itemView.findViewById(R.id.tv_desc);
        llReview = itemView.findViewById(R.id.ll_review);
        btnViewDetail = itemView.findViewById(R.id.btn_view_detail);
    }

    public void bind(TripPackage tripPackage) {
        Glide.with(itemView.getContext()).load(tripPackage.getImageUrl()).into(ivPackage);
        tvTitle.setText(tripPackage.getPackageName());
        tvDuration.setText(tripPackage.getFormattedDuration());
        tvGroup.setText(tripPackage.getFrontDescription());
        tvReview.setText(String.format(Locale.getDefault(), "%d Review", tripPackage.getTotalReview()));
        tvDesc.setText(String.format(Locale.getDefault(), "Start From %s/Pax", tripPackage.getFormattedPrice()));
        llReview.removeAllViews();
        for (int i = 0; i < tripPackage.getRating(); i++) {
            ImageView imageView = new ImageView(itemView.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(24, 24);
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundResource(R.drawable.ic_star);
            llReview.addView(imageView);
        }
        btnViewDetail.setOnClickListener(v -> listener.onViewDetail(tripPackage));
    }
}
