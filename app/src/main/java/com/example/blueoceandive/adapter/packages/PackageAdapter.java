package com.example.blueoceandive.adapter.packages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blueoceandive.R;
import com.example.blueoceandive.model.TripPackage;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageViewHolder> {

    private ArrayList<TripPackage> items = new ArrayList<>();
    private PackageActionListener listener;

    public PackageAdapter(PackageActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_package, parent, false);
        return new PackageViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        TripPackage tripPackage = items.get(position);
        holder.bind(tripPackage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(ArrayList<TripPackage> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }
}
