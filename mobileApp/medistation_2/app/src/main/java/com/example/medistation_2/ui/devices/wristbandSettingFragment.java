package com.example.medistation_2.ui.devices;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.medistation_2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class wristbandSettingFragment extends Fragment {

    private WristbandSettingViewModel mViewModel;
    private static final String TAG = wristbandSettingFragment.class.getSimpleName();
    public static wristbandSettingFragment newInstance() {
        return new wristbandSettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wristband_setting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WristbandSettingViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupDropDownMenu(view);
    }
    public void setupDropDownMenu (View view) {
        //set up drop down list
        Spinner batteryLevelNotification = view.findViewById(R.id.wristbandBatteryNotificationDropDown);

        String[] batteryLevel = new String[]{
                "Battery Level","50%","25%","15%","10%","5%","Never"};
        List<String> batteryLevelList = new ArrayList<>(Arrays.asList(batteryLevel));
        ArrayAdapter<String> batteryMenuArrayAdapater = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, batteryLevelList) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0; }
            @Override
            public View getDropDownView(int position,View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        batteryMenuArrayAdapater.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        batteryLevelNotification.setAdapter(batteryMenuArrayAdapater);

    }

}