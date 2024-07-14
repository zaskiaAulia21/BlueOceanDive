package com.example.blueoceandive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blueoceandive.R;
import com.example.blueoceandive.model.RegisterModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUsernameActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private EditText etUsername;
    private Button btnUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_username);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String username = getIntent().getStringExtra("username");

        etUsername = findViewById(R.id.et_username);
        btnUpdate = findViewById(R.id.btn_update);

        etUsername.setText(username);

        btnUpdate.setOnClickListener(v -> updateUsername());
    }

    private void updateUsername() {
        String username = etUsername.getText().toString();

        if (username.trim().isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference myRef = database.getReference("users");
            String id = auth.getUid();
            String email = auth.getCurrentUser().getEmail();
            myRef.child(id).setValue(new RegisterModel(username, email));
            Toast.makeText(this, "Update success", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
