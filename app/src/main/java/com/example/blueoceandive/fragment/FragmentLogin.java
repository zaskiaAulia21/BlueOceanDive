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
import com.example.blueoceandive.activity.ForgotPasswordActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentLogin extends Fragment {

    private TextView tvForgotPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar loading;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();

        tvForgotPassword = view.findViewById(R.id.tv_forget_password);
        etEmail = view.findViewById(R.id.te_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        loading = view.findViewById(R.id.loading);

        tvForgotPassword.setOnClickListener(v -> goToForgotPassword());
        btnLogin.setOnClickListener(v -> login());
    }

    private void goToForgotPassword() {
        Intent intent = new Intent(requireContext(), ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            loading.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        goToDashboard();
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
