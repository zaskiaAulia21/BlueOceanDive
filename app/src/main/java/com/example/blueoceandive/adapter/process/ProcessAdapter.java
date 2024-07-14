package com.example.blueoceandive.adapter.process;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blueoceandive.R;
import com.example.blueoceandive.adapter.packages.PackageActionListener;
import com.example.blueoceandive.adapter.packages.PackageViewHolder;
import com.example.blueoceandive.model.CheckoutItem;
import com.example.blueoceandive.model.TripPackage;

import java.util.ArrayList;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessViewHolder> {

    private ArrayList<CheckoutItem> items = new ArrayList<>();

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.process_container, parent, false);
        return new ProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
        CheckoutItem checkoutItem = items.get(position);
        holder.bind(checkoutItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(ArrayList<CheckoutItem> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }
}
