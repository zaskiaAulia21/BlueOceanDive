package com.example.blueoceandive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blueoceandive.R;
import com.example.blueoceandive.activity.DetailActivity;
import com.example.blueoceandive.adapter.packages.PackageActionListener;
import com.example.blueoceandive.adapter.packages.PackageAdapter;
import com.example.blueoceandive.model.TripPackage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentPackages extends Fragment implements PackageActionListener {

    private FirebaseDatabase database;
    private FragmentContainerDashboardListener listener;
    private RecyclerView rvPackages;
    private PackageAdapter adapter;
    private ImageView ivBack;

    public FragmentPackages(FragmentContainerDashboardListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_packages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        ivBack = view.findViewById(R.id.iv_back);
        rvPackages = view.findViewById(R.id.rv_packages);

        ivBack.setOnClickListener(v -> {
            listener.popBackStack();
        });

        adapter = new PackageAdapter(this);
        rvPackages.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPackages.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        DatabaseReference dbPost = database.getReference("packages");
        dbPost.limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TripPackage> packages = new ArrayList<>();
                for (DataSnapshot tripPackageSnapshot : snapshot.getChildren()) {
                    TripPackage tripPackage = tripPackageSnapshot.getValue(TripPackage.class);
                    tripPackage.setId(tripPackageSnapshot.getKey());
                    packages.add(tripPackage);
                }

                adapter.setData(packages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewDetail(TripPackage tripPackage) {
        Intent intent = new Intent(requireContext(), DetailActivity.class);
        intent.putExtra("packageId", tripPackage.getId());
        startActivity(intent);
    }
}
