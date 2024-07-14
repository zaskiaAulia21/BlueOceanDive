package com.example.blueoceandive.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blueoceandive.R;
import com.example.blueoceandive.adapter.packages.PackageAdapter;
import com.example.blueoceandive.adapter.process.ProcessAdapter;
import com.example.blueoceandive.model.CheckoutItem;
import com.example.blueoceandive.model.TripPackage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentProcess extends Fragment {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private RecyclerView rvProcess;
    private ProcessAdapter adapter;
    private TextView tvNoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_process, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        tvNoData = view.findViewById(R.id.tv_no_item);
        rvProcess = view.findViewById(R.id.rv_process);
        adapter = new ProcessAdapter();
        rvProcess.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvProcess.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        String userId = auth.getUid();
        DatabaseReference dbPost = database.getReference("processes");
        dbPost.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CheckoutItem> items = new ArrayList<>();
                for (DataSnapshot checkoutItemSnapshot : snapshot.getChildren()) {
                    CheckoutItem checkoutItem = checkoutItemSnapshot.getValue(CheckoutItem.class);
                    items.add(checkoutItem);
                }
                adapter.setData(items);

                if (items.isEmpty()) {
                    tvNoData.setVisibility(View.VISIBLE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
