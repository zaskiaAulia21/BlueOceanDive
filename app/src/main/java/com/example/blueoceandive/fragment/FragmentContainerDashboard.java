package com.example.blueoceandive.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.blueoceandive.R;

public class FragmentContainerDashboard extends Fragment implements FragmentContainerDashboardListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, new FragmentDashboard(this))
                .commit();
    }

    @Override
    public void popBackStack() {

    }

    @Override
    public void loadFragment(Fragment fragment, String name) {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(name)
                .replace(R.id.fl_container, fragment)
                .commit();
    }
}