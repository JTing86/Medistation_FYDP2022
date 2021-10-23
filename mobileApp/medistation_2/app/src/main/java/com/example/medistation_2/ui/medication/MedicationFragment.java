package com.example.medistation_2.ui.medication;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.medistation_2.R;
import com.example.medistation_2.ui.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedicationFragment extends Fragment {

    private MedicationViewModel mViewModel;
    private static final String TAG = MedicationFragment.class.getSimpleName();

    public static MedicationFragment newInstance() {
        return new MedicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Button pill1SaveButton = view.findViewById(R.id.pill1SaveButton);
        Button pill2SaveButton = view.findViewById(R.id.pill2SaveButton);

        pill1SaveButton.setOnClickListener(v -> {
            int pill1Dosage = Integer.parseInt(((EditText) view.findViewById(R.id.pill1DosageInput)).getText().toString());
            TableLayout pill1Table = (TableLayout)view.findViewById(R.id.pill1DosageTable);
            for(int i=1;i<=pill1Dosage;++i) {
                    TableRow row = (TableRow)LayoutInflater.from(requireActivity()).inflate(R.layout.schedule_table_format, null);
                    ((TextView)row.findViewById(R.id.dosageCount)).setText("Dose " + i);
                    initializeDropDownList(row);
                    pill1Table.addView(row);
            }
        });
        pill2SaveButton.setOnClickListener(v-> {
            int pill2Dosage = Integer.parseInt(((EditText) view.findViewById(R.id.pill2DosageInput)).getText().toString());
            TableLayout pill2Table = (TableLayout)view.findViewById(R.id.pill2DosageTable);
            for(int i=1;i<=pill2Dosage;++i) {
                TableRow row = (TableRow)LayoutInflater.from(requireActivity()).inflate(R.layout.schedule_table_format, null);
                ((TextView)row.findViewById(R.id.dosageCount)).setText("Dose " + i);
                initializeDropDownList(row);
                pill2Table.addView(row);
            }
        });
    }
    public void initializeDropDownList(TableRow row){
        Spinner numberOfPillsDropDownList = row.findViewById(R.id.numberOfPills);
        Spinner hourDropDownList = row.findViewById(R.id.pillHourDropDownList);
        Spinner minuteDropDownList = row.findViewById(R.id.pillMinDropDownList);
        String[] dosage = new String[] {
                "Dosage",
                "01","02","03","04","05","06","07","08","09","10"};
        String[] hour = new String[]{
                "Hour",
                "00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String[] minute = new String[]{
                "Min",
                "00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
                "24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","45","46","47","48",
                "49","50","51","52","53","54","55","56","57","58","59"};
        List<String> dosageList = new ArrayList<>(Arrays.asList(dosage));
        List<String> hourList = new ArrayList<>(Arrays.asList(hour));
        List<String> minuteList = new ArrayList<>(Arrays.asList(minute));
        //Create dosage dropdown menu
        ArrayAdapter<String> dosageMenuArrayAdapter = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, dosageList) {
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
        //Create hour dropdown menu
        ArrayAdapter<String> hourMenuArrayAdapter = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, hourList) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0; }
            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        //Create minute dropdown menu
        ArrayAdapter<String> minuteMenuArrayAdapter = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, minuteList) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0; }
            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        dosageMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        numberOfPillsDropDownList.setAdapter(dosageMenuArrayAdapter);
        hourMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        hourDropDownList.setAdapter(hourMenuArrayAdapter);
        minuteMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        minuteDropDownList.setAdapter(minuteMenuArrayAdapter);
    }
}