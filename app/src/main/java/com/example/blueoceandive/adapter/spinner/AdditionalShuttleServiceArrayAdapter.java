package com.example.blueoceandive.adapter.spinner;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.blueoceandive.R;
import com.example.blueoceandive.model.PackageAdditionalService;

import java.util.ArrayList;

public class AdditionalShuttleServiceArrayAdapter extends ArrayAdapter<PackageAdditionalService> {

    LayoutInflater inflater;

    public AdditionalShuttleServiceArrayAdapter(Activity context, int resouceId, int textviewId, ArrayList<PackageAdditionalService> list) {
        super(context, resouceId, textviewId, list);
        inflater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PackageAdditionalService rowItem = getItem(position);
        View rowview = inflater.inflate(R.layout.app_spinner_item, null, true);

        TextView txtTitle = rowview.findViewById(R.id.tv_title);
        txtTitle.setText(rowItem.getServiceName());

        return rowview;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}