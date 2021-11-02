package com.example.medistation_2.ui.devices;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class dispenserSettingFragment extends Fragment {

    private static final String TAG = dispenserSettingFragment.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dispenser_setting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DispenserSettingViewModel mViewModel = new ViewModelProvider(this).get(DispenserSettingViewModel.class);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();

        Button refillSaveButton = view.findViewById(R.id.refillSaveButton);
        refillSaveButton.setOnClickListener(v -> {
            dbHelper dbHelperCall = new dbHelper();
            dbHelperCall.AddSimpleStringData("/Patient/dispenser/refillContainer1",((Spinner) requireActivity().findViewById(R.id.dispenserContainer1DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleStringData("/Patient/dispenser/refillContainer2",((Spinner) requireActivity().findViewById(R.id.dispenserContainer2DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleStringData("/Patient/dispenser/refillContainer3",((Spinner) requireActivity().findViewById(R.id.dispenserContainer3DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleStringData("/Patient/dispenser/refillContainer4",((Spinner) requireActivity().findViewById(R.id.dispenserContainer4DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleStringData("/Patient/dispenser/refillContainer5",((Spinner) requireActivity().findViewById(R.id.dispenserContainer5DropDownMenu)).getSelectedItem().toString());
        });

        Button calibrateSaveButton = view.findViewById(R.id.calibrationSaveButton);
        calibrateSaveButton.setOnClickListener(v -> {
            dbHelper dbHelperCall = new dbHelper();
            AtomicInteger currentNumberOfPillsContainer1 = new AtomicInteger();
            AtomicInteger currentNumberOfPillsContainer2 = new AtomicInteger();
            AtomicInteger currentNumberOfPillsContainer3 = new AtomicInteger();
            AtomicInteger currentNumberOfPillsContainer4 = new AtomicInteger();
            AtomicInteger currentNumberOfPillsContainer5 = new AtomicInteger();
            AtomicInteger pillsAddedContainer1 = new AtomicInteger();
            AtomicInteger pillsAddedContainer2 = new AtomicInteger();
            AtomicInteger pillsAddedContainer3 = new AtomicInteger();
            AtomicInteger pillsAddedContainer4 = new AtomicInteger();
            AtomicInteger pillsAddedContainer5 = new AtomicInteger();
            if (!(((EditText) view.findViewById(R.id.calibrationContainer1Input)).getText().toString()).equals("")) {
                rootDbRef.child("Patient/dispenser/currentAmountContainer1").get().addOnCompleteListener(task -> {
                    currentNumberOfPillsContainer1.set(Integer.parseInt((String) Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getValue())));
                    pillsAddedContainer1.set(Integer.parseInt( ((EditText) view.findViewById(R.id.calibrationContainer1Input)).getText().toString()));
                    currentNumberOfPillsContainer1.getAndAdd(Integer.parseInt(String.valueOf(pillsAddedContainer1)));
                    dbHelperCall.AddSimpleStringData("/Patient/dispenser/currentAmountContainer1",String.valueOf(currentNumberOfPillsContainer1.get()));
                });
            }
            if (!(((EditText) view.findViewById(R.id.calibrationContainer2Input)).getText().toString()).equals("")) {
                rootDbRef.child("Patient/dispenser/currentAmountContainer2").get().addOnCompleteListener(task -> {
                    currentNumberOfPillsContainer2.set(Integer.parseInt((String) Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getValue())));
                    pillsAddedContainer2.set(Integer.parseInt( ((EditText) view.findViewById(R.id.calibrationContainer2Input)).getText().toString()));
                    currentNumberOfPillsContainer2.getAndAdd(Integer.parseInt(String.valueOf(pillsAddedContainer2)));
                    dbHelperCall.AddSimpleStringData("/Patient/dispenser/currentAmountContainer2",String.valueOf(currentNumberOfPillsContainer2.get()));
                });
            }
            if (!(((EditText) view.findViewById(R.id.calibrationContainer3Input)).getText().toString()).equals("")) {
                rootDbRef.child("Patient/dispenser/currentAmountContainer3").get().addOnCompleteListener(task -> {
                    currentNumberOfPillsContainer3.set(Integer.parseInt((String) Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getValue())));
                    pillsAddedContainer3.set(Integer.parseInt( ((EditText) view.findViewById(R.id.calibrationContainer3Input)).getText().toString()));
                    currentNumberOfPillsContainer3.getAndAdd(Integer.parseInt(String.valueOf(pillsAddedContainer3)));
                    dbHelperCall.AddSimpleStringData("/Patient/dispenser/currentAmountContainer3",String.valueOf(currentNumberOfPillsContainer3.get()));
                });
            }
            if (!(((EditText) view.findViewById(R.id.calibrationContainer4Input)).getText().toString()).equals("")) {
                rootDbRef.child("Patient/dispenser/currentAmountContainer4").get().addOnCompleteListener(task -> {
                    currentNumberOfPillsContainer4.set(Integer.parseInt((String) Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getValue())));
                    pillsAddedContainer4.set(Integer.parseInt( ((EditText) view.findViewById(R.id.calibrationContainer4Input)).getText().toString()));
                    currentNumberOfPillsContainer4.getAndAdd(Integer.parseInt(String.valueOf(pillsAddedContainer4)));
                    dbHelperCall.AddSimpleStringData("/Patient/dispenser/currentAmountContainer4",String.valueOf(currentNumberOfPillsContainer4.get()));
                });
            }
            if (!(((EditText) view.findViewById(R.id.calibrationContainer5Input)).getText().toString()).equals("")) {
                rootDbRef.child("Patient/dispenser/currentAmountContainer5").get().addOnCompleteListener(task -> {
                    currentNumberOfPillsContainer5.set(Integer.parseInt((String) Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getValue())));
                    pillsAddedContainer5.set(Integer.parseInt( ((EditText) view.findViewById(R.id.calibrationContainer5Input)).getText().toString()));
                    currentNumberOfPillsContainer5.getAndAdd(Integer.parseInt(String.valueOf(pillsAddedContainer5)));
                    dbHelperCall.AddSimpleStringData("/Patient/dispenser/currentAmountContainer5",String.valueOf(currentNumberOfPillsContainer5.get()));
                });
            }
        });
        setupDropDownMenu(view);
    }
    public void setupDropDownMenu (View view) {
        //set up drop down list
        Spinner container1DropDownMenu = view.findViewById(R.id.dispenserContainer1DropDownMenu);
        Spinner container2DropDownMenu = view.findViewById(R.id.dispenserContainer2DropDownMenu);
        Spinner container3DropDownMenu = view.findViewById(R.id.dispenserContainer3DropDownMenu);
        Spinner container4DropDownMenu = view.findViewById(R.id.dispenserContainer4DropDownMenu);
        Spinner container5DropDownMenu = view.findViewById(R.id.dispenserContainer5DropDownMenu);

        String[] numberOfPills = new String[]{"Amount","30","20","15","10","5","Never"};
        List<String> numberOfPillsList = new ArrayList<>(Arrays.asList(numberOfPills));

        ArrayAdapter<String> symptomButtonMenuArrayAdapters = new ArrayAdapter<String> (requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, numberOfPillsList) {
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
        symptomButtonMenuArrayAdapters.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        container1DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container2DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container3DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container4DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container5DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);




    }

    //TODO, add function to send notification when battery of wristband is low
}