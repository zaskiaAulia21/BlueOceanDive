package com.example.blueoceandive.fragment;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.blueoceandive.R;
import com.example.blueoceandive.adapter.ImageCardDecoration;
import com.example.blueoceandive.adapter.gallery.GalleryActionListener;
import com.example.blueoceandive.adapter.gallery.GalleryAdapter;
import com.example.blueoceandive.model.TripGallery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentGallery extends Fragment implements GalleryActionListener {

    private FirebaseDatabase database;
    private FragmentContainerDashboardListener listener;
    private RecyclerView rvGalleries;
    private GalleryAdapter adapter;
    private ImageView ivBack;

    public FragmentGallery() {

    }

    public FragmentGallery(FragmentContainerDashboardListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();

        ivBack = view.findViewById(R.id.iv_back);
        rvGalleries = view.findViewById(R.id.rv_galleries);

        ivBack.setOnClickListener(v -> {
            listener.popBackStack();
        });

        adapter = new GalleryAdapter(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvGalleries.setLayoutManager(staggeredGridLayoutManager);
        rvGalleries.addItemDecoration(new ImageCardDecoration(requireContext()));
        rvGalleries.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        DatabaseReference db = database.getReference("galleries");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TripGallery> galleries = new ArrayList<>();
                for (DataSnapshot tripGallerySnapshot : snapshot.getChildren()) {
                    TripGallery tripGallery = tripGallerySnapshot.getValue(TripGallery.class);
                    tripGallery.setId(tripGallerySnapshot.getKey());
                    galleries.add(tripGallery);
                }

                adapter.setData(galleries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
