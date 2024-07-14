package com.example.blueoceandive.adapter.gallery;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.model.TripGallery;

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivGallery;

    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        ivGallery = itemView.findViewById(R.id.iv_gallery);
    }

    public void bind(TripGallery tripGallery) {
        Glide.with(itemView.getContext()).load(tripGallery.getImageUrl()).into(ivGallery);
    }
}

