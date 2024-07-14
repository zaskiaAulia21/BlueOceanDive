package com.example.blueoceandive.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.blueoceandive.R;
import com.example.blueoceandive.fragment.FragmentCart;
import com.example.blueoceandive.fragment.FragmentContainerDashboardListener;
import com.example.blueoceandive.fragment.FragmentDashboard;
import com.example.blueoceandive.fragment.FragmentProcess;
import com.example.blueoceandive.fragment.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, FragmentContainerDashboardListener {

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(this);

        loadFragment(new FragmentDashboard(this));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.app_home:
                loadFragment(new FragmentDashboard(this));
                return true;
            case R.id.cart:
                loadFragment(new FragmentCart(this));
                return true;
            case R.id.process:
                loadFragment(new FragmentProcess());
                return true;
            case R.id.profile:
                loadFragment(new FragmentProfile());
                return true;
        }
        return false;
    }

    @Override
    public void loadFragment(Fragment fragment, String name) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(name)
                .commit();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
