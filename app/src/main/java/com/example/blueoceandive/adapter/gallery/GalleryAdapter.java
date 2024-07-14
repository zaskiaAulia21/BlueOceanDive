package com.example.blueoceandive.adapter.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blueoceandive.R;
import com.example.blueoceandive.model.TripGallery;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private ArrayList<TripGallery> items = new ArrayList<>();
    private GalleryActionListener listener;

    public GalleryAdapter(GalleryActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        TripGallery tripGallery = items.get(position);
        holder.bind(tripGallery);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(ArrayList<TripGallery> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }
}

