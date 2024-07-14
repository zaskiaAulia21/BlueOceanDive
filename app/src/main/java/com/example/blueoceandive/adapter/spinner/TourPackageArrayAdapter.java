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
import com.example.blueoceandive.model.TourPackage;

import java.util.ArrayList;

public class TourPackageArrayAdapter extends ArrayAdapter<TourPackage> {

    LayoutInflater inflater;

    public TourPackageArrayAdapter(Activity context, int resouceId, int textviewId, ArrayList<TourPackage> list) {
        super(context, resouceId, textviewId, list);
        inflater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TourPackage rowItem = getItem(position);
        View rowview = inflater.inflate(R.layout.app_spinner_item, null, true);

        TextView txtTitle = rowview.findViewById(R.id.tv_title);
        txtTitle.setText(rowItem.getTourPackageName());

        return rowview;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}