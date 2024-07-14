package com.example.blueoceandive.fragment;

import androidx.fragment.app.Fragment;

public interface FragmentContainerDashboardListener {
    void loadFragment(Fragment fragment, String name);
    void popBackStack();
}
