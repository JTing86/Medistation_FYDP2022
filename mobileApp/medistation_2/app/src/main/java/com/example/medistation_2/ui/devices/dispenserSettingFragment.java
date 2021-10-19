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
import java.util.concurrent.atomic.AtomicLong;

public class dispenserSettingFragment extends Fragment {

    private DispenserSettingViewModel mViewModel;
    private static final String TAG = dispenserSettingFragment.class.getSimpleName();
    public static dispenserSettingFragment newInstance() {
        return new dispenserSettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dispenser_setting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DispenserSettingViewModel.class);
        // TODO: Use the ViewModel
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();

        Button refillSaveButton = view.findViewById(R.id.refillSaveButton);
        refillSaveButton.setOnClickListener(v -> {
            dbHelper dbHelperCall = new dbHelper();
            dbHelperCall.AddSimpleData("/Patient/dispenser/refillContainer1",((Spinner) requireActivity().findViewById(R.id.dispenserContainer1DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleData("/Patient/dispenser/refillContainer2",((Spinner) requireActivity().findViewById(R.id.dispenserContainer2DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleData("/Patient/dispenser/refillContainer3",((Spinner) requireActivity().findViewById(R.id.dispenserContainer3DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleData("/Patient/dispenser/refillContainer4",((Spinner) requireActivity().findViewById(R.id.dispenserContainer4DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleData("/Patient/dispenser/refillContainer5",((Spinner) requireActivity().findViewById(R.id.dispenserContainer5DropDownMenu)).getSelectedItem().toString());
        });

        Button calibrationSaveButton = view.findViewById(R.id.calibrationSaveButton);
        calibrationSaveButton.setOnClickListener(v -> {
            AtomicLong pillsAddedContainer1 = null;
            AtomicLong pillsAddedContainer2 = null;
            AtomicLong pillsAddedContainer3 = null;
            AtomicLong pillsAddedContainer4 = null;
            AtomicLong pillsAddedContainer5 = null;
            if ((((EditText) view.findViewById(R.id.calibrationContainer1Input)).getText().toString()) != "")
                pillsAddedContainer1 = new AtomicLong(Long.parseLong(((EditText) view.findViewById(R.id.calibrationContainer1Input)).getText().toString()));
            if((((EditText) view.findViewById(R.id.calibrationContainer2Input)).getText().toString()) != "")
                pillsAddedContainer2 = new AtomicLong(Long.parseLong(((EditText) view.findViewById(R.id.calibrationContainer2Input)).getText().toString()));
            if ((((EditText) view.findViewById(R.id.calibrationContainer3Input)).getText().toString()) != "")
                pillsAddedContainer3 = new AtomicLong(Long.parseLong(((EditText) view.findViewById(R.id.calibrationContainer3Input)).getText().toString()));
            if ((((EditText) view.findViewById(R.id.calibrationContainer4Input)).getText().toString()) != "")
                pillsAddedContainer4 = new AtomicLong(Long.parseLong(((EditText) view.findViewById(R.id.calibrationContainer4Input)).getText().toString()));
            if ((((EditText) view.findViewById(R.id.calibrationContainer5Input)).getText().toString()) != "")
                pillsAddedContainer5 = new AtomicLong(Long.parseLong(((EditText) view.findViewById(R.id.calibrationContainer5Input)).getText().toString()));

            dbHelper dbHelperCall = new dbHelper();
            AtomicLong finalPillsAddedContainer1 = pillsAddedContainer1;
            rootDbRef.child("Patient/dispenser/currentAmountContainer1").get().addOnCompleteListener(task -> {
                finalPillsAddedContainer1.set(finalPillsAddedContainer1.get() + Long.parseLong((String) Objects.requireNonNull(task.getResult()).getValue()));
                Log.d(TAG,String.valueOf(finalPillsAddedContainer1.get()));
                Log.d(TAG,Objects.requireNonNull(task.getResult()).getValue().toString());
                dbHelperCall.AddSimpleData("/Patient/dispenser/currentAmountContainer1",String.valueOf(finalPillsAddedContainer1.get()));
            });
            AtomicLong finalPillsAddedContainer2 = pillsAddedContainer2;
            rootDbRef.child("Patient/dispenser/currentAmountContainer2").get().addOnCompleteListener(task -> {
                finalPillsAddedContainer2.set(finalPillsAddedContainer2.get() + (Long) Objects.requireNonNull(task.getResult()).getValue());
                Log.d(TAG,String.valueOf(finalPillsAddedContainer2.get()));
                Log.d(TAG,Objects.requireNonNull(task.getResult()).getValue().toString());
                dbHelperCall.AddSimpleData("/Patient/dispenser/currentAmountContainer2",String.valueOf(finalPillsAddedContainer2.get()));
            });
            AtomicLong finalPillsAddedContainer3 = pillsAddedContainer3;
            rootDbRef.child("Patient/dispenser/currentAmountContainer3").get().addOnCompleteListener(task -> {
                finalPillsAddedContainer3.set(finalPillsAddedContainer3.get() + (Long) Objects.requireNonNull(task.getResult()).getValue());
                Log.d(TAG,String.valueOf(finalPillsAddedContainer3.get()));
                Log.d(TAG,Objects.requireNonNull(task.getResult()).getValue().toString());
                dbHelperCall.AddSimpleData("/Patient/dispenser/currentAmountContainer3",String.valueOf(finalPillsAddedContainer3.get()));
            });
            AtomicLong finalPillsAddedContainer4 = pillsAddedContainer4;
            rootDbRef.child("Patient/dispenser/currentAmountContainer4").get().addOnCompleteListener(task -> {
                finalPillsAddedContainer4.set(finalPillsAddedContainer4.get() + (Long) Objects.requireNonNull(task.getResult()).getValue());
                Log.d(TAG,String.valueOf(finalPillsAddedContainer4.get()));
                Log.d(TAG,Objects.requireNonNull(task.getResult()).getValue().toString());
                dbHelperCall.AddSimpleData("/Patient/dispenser/currentAmountContainer4",String.valueOf(finalPillsAddedContainer4.get()));
            });
            AtomicLong finalPillsAddedContainer5 = pillsAddedContainer5;
            rootDbRef.child("Patient/dispenser/currentAmountContainer5").get().addOnCompleteListener(task -> {
                finalPillsAddedContainer5.set(finalPillsAddedContainer5.get() + (Long) Objects.requireNonNull(task.getResult()).getValue());
                Log.d(TAG,String.valueOf(finalPillsAddedContainer5.get()));
                Log.d(TAG,Objects.requireNonNull(task.getResult()).getValue().toString());
                dbHelperCall.AddSimpleData("/Patient/dispenser/currentAmountContainer5",String.valueOf(finalPillsAddedContainer5.get()));
            });



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

        ArrayAdapter<String> symptomButtonMenuArrayAdapater = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, numberOfPillsList) {
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
        symptomButtonMenuArrayAdapater.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        container1DropDownMenu.setAdapter(symptomButtonMenuArrayAdapater);
        container2DropDownMenu.setAdapter(symptomButtonMenuArrayAdapater);
        container3DropDownMenu.setAdapter(symptomButtonMenuArrayAdapater);
        container4DropDownMenu.setAdapter(symptomButtonMenuArrayAdapater);
        container5DropDownMenu.setAdapter(symptomButtonMenuArrayAdapater);




    }
}