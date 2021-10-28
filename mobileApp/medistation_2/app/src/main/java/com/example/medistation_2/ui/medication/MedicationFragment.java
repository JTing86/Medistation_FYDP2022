package com.example.medistation_2.ui.medication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.dbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MedicationFragment extends Fragment {

    private static final String TAG = MedicationFragment.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MedicationViewModel mViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Button pill1SaveButton = view.findViewById(R.id.pill1SaveButton);
        Button pill2SaveButton = view.findViewById(R.id.pill2SaveButton);
        Button pill3SaveButton = view.findViewById(R.id.pill3SaveButton);
        Button pill4SaveButton = view.findViewById(R.id.pill4SaveButton);
        Button pill5SaveButton = view.findViewById(R.id.pill5SaveButton);
        AtomicInteger pill1DailyDosage = new AtomicInteger();
        AtomicInteger pill2DailyDosage = new AtomicInteger();
        AtomicInteger pill3DailyDosage = new AtomicInteger();
        AtomicInteger pill4DailyDosage = new AtomicInteger();
        AtomicInteger pill5DailyDosage = new AtomicInteger();
        pill1SaveButton.setOnClickListener(v-> {
            Log.d(TAG,"pill1SaveButton Pressed");
            TableLayout tableLayout = view.findViewById(R.id.pill1DosageTable);
            pill1DailyDosage.set(Integer.parseInt(((EditText) view.findViewById(R.id.pill1DosageInput)).getText().toString()));
            for (int i =1; i <= pill1DailyDosage.get(); i++)
            {
                TableRow tableRow = new TableRow(getContext());
                Spinner numberOfPillsDropDownList =  new Spinner(getContext());
                Spinner hourDropDownList =  new Spinner(getContext());
                Spinner minuteDropDownList = new Spinner(getContext());

                TextView rowTitle = new TextView(getContext());
                rowTitle.setText("Dose " + i);
                //first number represent pill
                //second number represent row
                //third number represent which block of the row
                rowTitle.setId(Integer.decode("1"+i+"1"));
                Log.d(TAG,String.valueOf(rowTitle.getId()));
                rowTitle.setTextSize(18);
                rowTitle.setTypeface(null,Typeface.BOLD);

                numberOfPillsDropDownList.setId(Integer.decode("1"+i+"2"));
                hourDropDownList.setId(Integer.decode("1"+i+"3"));
                minuteDropDownList.setId(Integer.decode("1"+i+"4"));
                initializeDropDownList(numberOfPillsDropDownList,hourDropDownList,minuteDropDownList);
                tableRow.addView(rowTitle);
                tableRow.addView(numberOfPillsDropDownList);
                tableRow.addView(hourDropDownList);
                tableRow.addView(minuteDropDownList);
                tableLayout.addView(tableRow);
            }
        });
        pill2SaveButton.setOnClickListener(v-> {
            Log.d(TAG,"pill2SaveButton Pressed");
            TableLayout tableLayout = view.findViewById(R.id.pill2DosageTable);
            pill2DailyDosage.set(Integer.parseInt(((EditText) view.findViewById(R.id.pill2DosageInput)).getText().toString()));
            for (int i =1; i <= pill2DailyDosage.get(); i++)
            {
                TableRow tableRow = new TableRow(getContext());
                Spinner numberOfPillsDropDownList =  new Spinner(getContext());
                Spinner hourDropDownList =  new Spinner(getContext());
                Spinner minuteDropDownList = new Spinner(getContext());

                TextView rowTitle = new TextView(getContext());
                rowTitle.setText("Dose " + i);
                //first number represent pill
                //second number represent row
                //third number represent which block of the row
                rowTitle.setId(Integer.decode("2"+i+"1"));
                rowTitle.setTextSize(18);
                rowTitle.setTypeface(null,Typeface.BOLD);

                numberOfPillsDropDownList.setId(Integer.decode("2"+i+"2"));
                hourDropDownList.setId(Integer.decode("2"+i+"3"));
                minuteDropDownList.setId(Integer.decode("2"+i+"4"));
                initializeDropDownList(numberOfPillsDropDownList,hourDropDownList,minuteDropDownList);
                tableRow.addView(rowTitle);
                tableRow.addView(numberOfPillsDropDownList);
                tableRow.addView(hourDropDownList);
                tableRow.addView(minuteDropDownList);
                tableLayout.addView(tableRow);
            }
        });
        pill3SaveButton.setOnClickListener(v-> {
            Log.d(TAG,"pill3SaveButton Pressed");
            TableLayout tableLayout = view.findViewById(R.id.pill3DosageTable);
            pill3DailyDosage.set(Integer.parseInt(((EditText) view.findViewById(R.id.pill3DosageInput)).getText().toString()));
            for (int i =1; i <= pill3DailyDosage.get(); i++)
            {
                TableRow tableRow = new TableRow(getContext());
                Spinner numberOfPillsDropDownList =  new Spinner(getContext());
                Spinner hourDropDownList =  new Spinner(getContext());
                Spinner minuteDropDownList = new Spinner(getContext());

                TextView rowTitle = new TextView(getContext());
                rowTitle.setText("Dose " + i);
                //first number represent pill
                //second number represent row
                //third number represent which block of the row
                rowTitle.setId(Integer.decode("3"+i+"1"));
                rowTitle.setTextSize(18);
                rowTitle.setTypeface(null,Typeface.BOLD);

                numberOfPillsDropDownList.setId(Integer.decode("3"+i+"2"));
                hourDropDownList.setId(Integer.decode("3"+i+"3"));
                minuteDropDownList.setId(Integer.decode("3"+i+"4"));
                initializeDropDownList(numberOfPillsDropDownList,hourDropDownList,minuteDropDownList);
                tableRow.addView(rowTitle);
                tableRow.addView(numberOfPillsDropDownList);
                tableRow.addView(hourDropDownList);
                tableRow.addView(minuteDropDownList);
                tableLayout.addView(tableRow);
            }
        });
        pill4SaveButton.setOnClickListener(v-> {
            Log.d(TAG,"pill4SaveButton Pressed");
            TableLayout tableLayout = view.findViewById(R.id.pill4DosageTable);
            pill4DailyDosage.set(Integer.parseInt(((EditText) view.findViewById(R.id.pill4DosageInput)).getText().toString()));
            for (int i =1; i <= pill4DailyDosage.get(); i++)
            {
                TableRow tableRow = new TableRow(getContext());
                Spinner numberOfPillsDropDownList =  new Spinner(getContext());
                Spinner hourDropDownList =  new Spinner(getContext());
                Spinner minuteDropDownList = new Spinner(getContext());

                TextView rowTitle = new TextView(getContext());
                rowTitle.setText("Dose " + i);
                //first number represent pill
                //second number represent row
                //third number represent which block of the row
                rowTitle.setId(Integer.decode("4"+i+"1"));
                rowTitle.setTextSize(18);
                rowTitle.setTypeface(null,Typeface.BOLD);

                numberOfPillsDropDownList.setId(Integer.decode("4"+i+"2"));
                hourDropDownList.setId(Integer.decode("4"+i+"3"));
                minuteDropDownList.setId(Integer.decode("4"+i+"4"));
                initializeDropDownList(numberOfPillsDropDownList,hourDropDownList,minuteDropDownList);
                tableRow.addView(rowTitle);
                tableRow.addView(numberOfPillsDropDownList);
                tableRow.addView(hourDropDownList);
                tableRow.addView(minuteDropDownList);
                tableLayout.addView(tableRow);
            }
        });
        pill5SaveButton.setOnClickListener(v-> {
            Log.d(TAG,"pill5SaveButton Pressed");
            TableLayout tableLayout = view.findViewById(R.id.pill5DosageTable);
            pill5DailyDosage.set(Integer.parseInt(((EditText) view.findViewById(R.id.pill5DosageInput)).getText().toString()));
            for (int i =1; i <= pill5DailyDosage.get(); i++)
            {
                TableRow tableRow = new TableRow(getContext());
                Spinner numberOfPillsDropDownList =  new Spinner(getContext());
                Spinner hourDropDownList =  new Spinner(getContext());
                Spinner minuteDropDownList = new Spinner(getContext());

                TextView rowTitle = new TextView(getContext());
                rowTitle.setText("Dose " + i);
                //first number represent pill
                //second number represent row
                //third number represent which block of the row
                rowTitle.setId(Integer.decode("5"+i+"1"));
                rowTitle.setTextSize(18);
                rowTitle.setTypeface(null,Typeface.BOLD);

                numberOfPillsDropDownList.setId(Integer.decode("5"+i+"2"));
                hourDropDownList.setId(Integer.decode("5"+i+"3"));
                minuteDropDownList.setId(Integer.decode("5"+i+"4"));
                initializeDropDownList(numberOfPillsDropDownList,hourDropDownList,minuteDropDownList);
                tableRow.addView(rowTitle);
                tableRow.addView(numberOfPillsDropDownList);
                tableRow.addView(hourDropDownList);
                tableRow.addView(minuteDropDownList);
                tableLayout.addView(tableRow);
            }
        });
        Button scheduleSaveButton = view.findViewById(R.id.scheduleSaveButton);
        scheduleSaveButton.setOnClickListener(v-> savePillScheduleToDatabase(view,1,pill1DailyDosage.get()));
    }
    public void initializeDropDownList(Spinner numberOfPillsDropDownList, Spinner hourDropDownList, Spinner minuteDropDownList ){
        numberOfPillsDropDownList.setGravity(Gravity.CENTER);
        hourDropDownList.setGravity(Gravity.CENTER);
        minuteDropDownList.setGravity(Gravity.CENTER);
        String[] dosage = new String[] {
                "Dosage", "1","2","3","4","5","6","7","8","9","10"};
        String[] hour = new String[]{
                "Hour", "00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String[] minute = new String[]{
                "Min",
                "00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
                "24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","45","46","47","48",
                "49","50","51","52","53","54","55","56","57","58","59"};
        List<String> dosageList = new ArrayList<>(Arrays.asList(dosage));
        List<String> hourList = new ArrayList<>(Arrays.asList(hour));
        List<String> minuteList = new ArrayList<>(Arrays.asList(minute));
        //Create dosage dropdown menu
        ArrayAdapter<String> dosageMenuArrayAdapter = new ArrayAdapter<String> (requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, dosageList) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from spinner, to be used for hints
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
                // Disable the first item from spinner,to be used for hints
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
    public void savePillScheduleToDatabase (View view, int pillNumber,int dailyDosage){
        String pillName = "pill"+ pillNumber;
        dbHelper dbHelperCall = new dbHelper();
        //retrieve the day of the week selected by user
        String [] toggleButton = new String [] {
                pillName+"MonToggle", pillName+"TuesToggle", pillName+"WedToggle", pillName+"ThursToggle", pillName+"FriToggle", pillName+"SatToggle", pillName+"SunToggle"};
        String [] dayOfTheWeek = new String [] {
                pillName+"01", pillName+"02", pillName+"03", pillName+"04", pillName+"05", pillName+"06", pillName+"07"};
        for (int i=0; i<=6; i++){
            int RID = view.getResources().getIdentifier(toggleButton[i],"id", requireActivity().getPackageName());
            ToggleButton dayOfTheWeekToggleButton = view.findViewById(RID);
            dbHelperCall.AddSimpleBooleanData("/Patient/medication/"+pillName+"/"+dayOfTheWeek[i], dayOfTheWeekToggleButton.isChecked());
        }
        //retrieve the dosage schedule for single day
        for (int i=1;i<=dailyDosage;i++){
            //String rowTitle = ((TextView) view.findViewById(Integer.decode(pillNumber+String.valueOf(i)+"1"))).getText().toString().toLowerCase();
            String numberOfPills = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber+String.valueOf(i)+"2"))).getSelectedItem().toString();
            String hour = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber+String.valueOf(i)+"3"))).getSelectedItem().toString();
            String minute = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber+String.valueOf(i)+"4"))).getSelectedItem().toString();
            String data = numberOfPills+","+hour+","+minute;
            dbHelperCall.AddSimpleStringData("Patient/medication/"+pillName+"/dosage/dose"+i,data);
        }
    }
}