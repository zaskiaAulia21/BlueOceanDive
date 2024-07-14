package com.example.blueoceandive.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.activity.DetailActivity;
import com.example.blueoceandive.adapter.packages.PackageActionListener;
import com.example.blueoceandive.adapter.packages.PackageAdapter;
import com.example.blueoceandive.model.RegisterModel;
import com.example.blueoceandive.model.TripPackage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class FragmentDashboard extends Fragment implements PackageActionListener {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private TextView tvWelcomeName;
    private ImageView ivProfile;
    private EditText etSearch;
    private FrameLayout flTripPackages, flGallery;
    private RecyclerView rvPackages;
    private PackageAdapter adapter;
    private FragmentContainerDashboardListener listener;
    private ArrayList<TripPackage> packages;

    public FragmentDashboard() {

    }

    public FragmentDashboard(FragmentContainerDashboardListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        etSearch = view.findViewById(R.id.et_search);
        ivProfile = view.findViewById(R.id.iv_profile);
        tvWelcomeName = view.findViewById(R.id.tv_welcome_name);
        flGallery = view.findViewById(R.id.fl_gallery);
        flTripPackages = view.findViewById(R.id.fl_chip_packages);
        rvPackages = view.findViewById(R.id.rv_packages);
        adapter = new PackageAdapter(this);
        rvPackages.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPackages.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<TripPackage> newList = new ArrayList<>(packages);
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    adapter.setData(packages);
                } else {
                    newList = new ArrayList<>(
                            newList.stream().filter(item -> item.getPackageName().toLowerCase().contains(query.toLowerCase()))
                                    .collect(Collectors.toList())
                    );
                    adapter.setData(newList);
                }
            }
        });

        flGallery.setOnClickListener(v -> listener.loadFragment(new FragmentGallery(listener), "gallery"));
        flTripPackages.setOnClickListener(v -> listener.loadFragment(new FragmentPackages(listener), "packages"));

        loadProfile();
        loadProfilePhoto();
        loadData();
    }

    private void loadProfile() {
        DatabaseReference dbUsers = database.getReference("users");
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser user = auth.getCurrentUser();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    RegisterModel userModel = userSnapshot.getValue(RegisterModel.class);
                    if (userModel != null && user != null && user.getEmail().equals(userModel.getEmail())) {
                        tvWelcomeName.setText(String.format(Locale.getDefault(), "Hi %s", userModel.getUsername()));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
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
                FragmentDashboard.this.packages = packages;
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

    private void loadProfilePhoto() {
        String userId = auth.getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference ref = storageRef.child("profile/" + userId + "/profile.png");
        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String imageUrl = task.getResult().toString();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(requireContext()).load(imageUrl).into(ivProfile);
                    }
                }
            }
        });
    }
}
