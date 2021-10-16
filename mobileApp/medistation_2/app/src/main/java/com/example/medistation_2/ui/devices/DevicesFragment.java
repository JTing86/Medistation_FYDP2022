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
    private DevicesViewModel mViewModel;

    public static DevicesFragment newInstance() {
        return new DevicesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProfileViewModel mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button profileUserInfoSaveButton = (Button) view.findViewById(R.id.deviceWristbandButton);
        profileUserInfoSaveButton.setOnClickListener(v -> {
            Log.d(TAG,"User Info save button pressed");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
// Replace whatever is in the fragment_container view with this fragment
            transaction.replace(((ViewGroup)getView().getParent()).getId(), new ProfileFragment(), null);

// Commit the transaction
            transaction.commit();

        });
    }

}