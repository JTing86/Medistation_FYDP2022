package com.example.medistation_2.ui.devices;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.example.medistation_2.ui.profile.ProfileFragment;
import com.example.medistation_2.ui.profile.ProfileViewModel;

public class DevicesFragment extends Fragment {
    private static final String TAG = DevicesFragment.class.getSimpleName();

    public static DevicesFragment newInstance() {
        return new DevicesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button wristbandNavButton =  view.findViewById(R.id.deviceWristbandButton);
        wristbandNavButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.replace(((ViewGroup)getView().getParent()).getId(), new wristbandSettingFragment(), null);
            transaction.commit();

        });
        Button dispenserNavButton =  view.findViewById(R.id.deviceDispenserButton);
        dispenserNavButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.replace(((ViewGroup)getView().getParent()).getId(), new dispenserSettingFragment(), null);
            transaction.commit();

        });
    }

}