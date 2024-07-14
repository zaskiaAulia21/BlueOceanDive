package com.example.blueoceandive.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.blueoceandive.R;
import com.example.blueoceandive.fragment.FragmentLogin;
import com.example.blueoceandive.fragment.FragmentRegister;

public class AuthActivity extends AppCompatActivity implements FragmentRegister.AuthActionListener {

    private TextView tvLogin, tvSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        tvLogin = findViewById(R.id.tv_login);
        tvSignIn = findViewById(R.id.tv_signin);

        loadFragment(new FragmentLogin());

        tvLogin.setOnClickListener(v -> {
            loadFragment(new FragmentLogin());
            enableLogin();
        });
        tvSignIn.setOnClickListener(v -> {
            loadFragment(new FragmentRegister(this));
            enableSignIn();
        });
    }

    @Override
    public void openLogin() {
        loadFragment(new FragmentLogin());
        enableLogin();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void enableLogin() {
        tvLogin.setTextColor(getResources().getColor(R.color.white, null));
        tvLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.background_rounded_border));
        tvSignIn.setTextColor(getResources().getColor(R.color.primary, null));
        tvSignIn.setBackground(null);
    }

    private void enableSignIn() {
        tvSignIn.setTextColor(getResources().getColor(R.color.white, null));
        tvSignIn.setBackground(ContextCompat.getDrawable(this, R.drawable.background_rounded_border));
        tvLogin.setTextColor(getResources().getColor(R.color.primary, null));
        tvLogin.setBackground(null);
    }
}
