package com.example.medistation_2.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.medistation_2.R;

public class DevicesFragment extends Fragment {

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
            transaction.replace(((ViewGroup) requireView().getParent()).getId(), new wristbandSettingFragment(), null);
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