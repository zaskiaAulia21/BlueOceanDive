package com.example.blueoceandive.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.activity.SplashActivity;
import com.example.blueoceandive.activity.UpdatePasswordActivity;
import com.example.blueoceandive.activity.UpdateUsernameActivity;
import com.example.blueoceandive.model.RegisterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private CircleImageView ivAvatar;
    private TextView tvUsernameLabel, tvUsername, tvEmail, tvUpdatePassword, tvDeleteAccount, tvLogout;
    private ImageView ivEdit;
    private ImageView ivArrowRightUsername, ivArrowRightUpdatePassword, ivArrowRightDeleteAccount, ivArrowRightLogout;

    private String username;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        ivAvatar.setImageBitmap(bitmap);
                        uploadPhoto(uri);
                    } catch (IOException e) {
                        Log.d("PhotoPicker", "Something went wrong");
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        tvUsernameLabel = view.findViewById(R.id.tv_username_label);
        tvUsername = view.findViewById(R.id.tv_username);
        ivArrowRightUsername = view.findViewById(R.id.iv_arrow_right);
        tvUsernameLabel.setOnClickListener(v -> goToUpdateUsername());
        tvUsername.setOnClickListener(v -> goToUpdateUsername());
        ivArrowRightUsername.setOnClickListener(v -> goToUpdateUsername());

        tvUpdatePassword = view.findViewById(R.id.tv_update_password);
        tvDeleteAccount = view.findViewById(R.id.tv_delete_account);
        tvLogout = view.findViewById(R.id.tv_logout);
        ivArrowRightUpdatePassword = view.findViewById(R.id.iv_arrow_right_update_password);
        ivArrowRightDeleteAccount = view.findViewById(R.id.iv_arrow_right_delete_account);
        ivArrowRightLogout = view.findViewById(R.id.iv_arrow_right_logout);

        tvUpdatePassword.setOnClickListener(v -> goToUpdatePassword());
        ivArrowRightUpdatePassword.setOnClickListener(v -> goToUpdatePassword());

        tvDeleteAccount.setOnClickListener(v -> deleteAccount());
        ivArrowRightDeleteAccount.setOnClickListener(v -> deleteAccount());

        tvLogout.setOnClickListener(v -> logout());
        ivArrowRightLogout.setOnClickListener(v -> logout());

        tvEmail = view.findViewById(R.id.tv_email);

        ivEdit = view.findViewById(R.id.iv_edit);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> choosePhoto());
        ivEdit.setOnClickListener(v -> choosePhoto());

        loadProfilePhoto();
        loadProfile();
    }

    private void choosePhoto() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void uploadPhoto(Uri uri) {
        String userId = auth.getUid();
        StorageReference storageRef = storage.getReference();
        StorageReference ref = storageRef.child("profile/" + userId + "/profile.png");
        UploadTask uploadTask = ref.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(requireContext(), "Update photo failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(requireContext(), "Update photo success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfilePhoto() {
        String userId = auth.getUid();
        StorageReference storageRef = storage.getReference();
        StorageReference ref = storageRef.child("profile/" + userId + "/profile.png");
        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String imageUrl = task.getResult().toString();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(requireContext()).load(imageUrl).into(ivAvatar);
                    }
                }
            }
        });
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
                        FragmentProfile.this.username = userModel.getUsername();
                        tvUsername.setText(userModel.getUsername());
                        tvEmail.setText(userModel.getEmail());
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

    private void logout() {
        auth.signOut();
        Intent intent = new Intent(requireContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToUpdateUsername() {
        Intent intent = new Intent(requireContext(), UpdateUsernameActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void goToUpdatePassword() {
        Intent intent = new Intent(requireContext(), UpdatePasswordActivity.class);
        startActivity(intent);
    }

    private void deleteAccount() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.dialog_delete, null, false);
        Button btnDelete = view.findViewById(R.id.btn_delete);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        AlertDialog dialog = alertDialog.create();
        btnDelete.setOnClickListener(v -> {
            FirebaseUser user = auth.getCurrentUser();
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Delete account success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireContext(), SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setView(view);
        dialog.show();
    }
}
