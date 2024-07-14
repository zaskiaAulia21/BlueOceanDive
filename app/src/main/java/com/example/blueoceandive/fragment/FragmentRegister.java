package com.example.blueoceandive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.blueoceandive.R;
import com.example.blueoceandive.activity.DashboardActivity;
import com.example.blueoceandive.model.RegisterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentRegister extends Fragment {

    private TextView tvGoToLogin;
    private TextInputEditText etEmail, etPassword, etConfirmPassword;
    private Button btnSignIn;
    private ProgressBar loading;
    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private AuthActionListener listener;

    public FragmentRegister(AuthActionListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        tvGoToLogin = view.findViewById(R.id.tv_go_to_login);
        etEmail = view.findViewById(R.id.te_email);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        loading = view.findViewById(R.id.loading);

        tvGoToLogin.setOnClickListener(v -> openLogin());
        btnSignIn.setOnClickListener(v -> signIn());
    }

    private void openLogin() {
        listener.openLogin();
    }

    private void signIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Password does not match", Toast.LENGTH_SHORT).show();
        } else {
            loading.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        updateNameToDatabase(email.substring(0, email.indexOf("@")), email);
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void goToDashboard() {
        Intent intent = new Intent(requireContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void updateNameToDatabase(String name, String email) {
        DatabaseReference myRef = mDatabase.getReference("users");
        String id = auth.getUid();
        myRef.child(id).setValue(new RegisterModel(name, email));
        Toast.makeText(requireContext(), "Register success", Toast.LENGTH_SHORT).show();
        loading.setVisibility(View.GONE);
        listener.openLogin();
    }

    public interface AuthActionListener {
        void openLogin();
    }
}
