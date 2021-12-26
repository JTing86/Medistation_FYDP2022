package com.example.medistation_2.ui.medication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.MQTT;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MedicationFragment extends Fragment {

    private static final String TAG = MedicationFragment.class.getSimpleName();
    public MqttAndroidClient client;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        initializePillName(view);
        Button pill1SaveButton = view.findViewById(R.id.pill1SaveButton);
        Button pill2SaveButton = view.findViewById(R.id.pill2SaveButton);
        Button pill3SaveButton = view.findViewById(R.id.pill3SaveButton);
        Button pill4SaveButton = view.findViewById(R.id.pill4SaveButton);
        Button pill5SaveButton = view.findViewById(R.id.pill5SaveButton);
        boolean[] pillSelector = new boolean[5];
        Integer[] dailyDosage = new Integer[5];

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(requireContext().getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG,"MQTT successfully connected in dispenser setting fragment");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG,"MQTT client in dispenser setting fragment did not connect successfully");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        pill1SaveButton.setOnClickListener(v -> {
            String pill1DailyDosage = String.valueOf(((TextView) view.findViewById(R.id.pill1DosageInput)).getText());
            if (!(pill1DailyDosage.equals(" "))) {
                dosageTableCreator(view, R.id.pill1DosageInput, R.id.pill1DosageTable, "1");
                dailyDosage[0] = Integer.parseInt(pill1DailyDosage);
                pillSelector[0] = true;
            }
        });
        pill2SaveButton.setOnClickListener(v -> {
            String pill2DailyDosage = String.valueOf(((TextView) view.findViewById(R.id.pill2DosageInput)).getText());
            if (!(pill2DailyDosage.equals(" "))) {
                dosageTableCreator(view, R.id.pill2DosageInput, R.id.pill2DosageTable, "2");
                dailyDosage[1] = Integer.parseInt(pill2DailyDosage);
                pillSelector[1] = true;
            }
        });
        pill3SaveButton.setOnClickListener(v -> {
            String pill3DailyDosage = String.valueOf(((TextView) view.findViewById(R.id.pill3DosageInput)).getText());
            if (!(pill3DailyDosage.equals(" "))) {
                dosageTableCreator(view, R.id.pill3DosageInput, R.id.pill3DosageTable, "3");
                dailyDosage[2] = Integer.parseInt(pill3DailyDosage);
                pillSelector[2] = true;
            }
        });
        pill4SaveButton.setOnClickListener(v -> {
            String pill4DailyDosage = String.valueOf(((TextView) view.findViewById(R.id.pill4DosageInput)).getText());
            if (!(pill4DailyDosage.equals(" "))) {
                dosageTableCreator(view, R.id.pill4DosageInput, R.id.pill4DosageTable, "4");
                dailyDosage[3] = Integer.parseInt(pill4DailyDosage);
                pillSelector[3] = true;
            }
        });
        pill5SaveButton.setOnClickListener(v -> {
            String pill5DailyDosage = String.valueOf(((TextView) view.findViewById(R.id.pill5DosageInput)).getText());
            if (!(pill5DailyDosage.equals(" "))) {
                dosageTableCreator(view, R.id.pill5DosageInput, R.id.pill5DosageTable, "5");
                dailyDosage[4] = Integer.parseInt(pill5DailyDosage);
                pillSelector[4] = true;
            }
        });
        Button scheduleSaveButton = view.findViewById(R.id.scheduleSaveButton);
        scheduleSaveButton.setOnClickListener(v -> {
            if (pillSelector[0])
                savePillScheduleToDatabase(view, "1", dailyDosage[0]);
            if (pillSelector[1])
                savePillScheduleToDatabase(view, "2", dailyDosage[1]);
            if (pillSelector[2])
                savePillScheduleToDatabase(view, "3", dailyDosage[2]);
            if (pillSelector[3])
                savePillScheduleToDatabase(view, "4", dailyDosage[3]);
            if (pillSelector[4])
                savePillScheduleToDatabase(view, "5", dailyDosage[4]);
            Handler handler = new Handler();
            handler.postDelayed(() -> informDispenser(pillSelector), 5000);   //5 seconds
        });

    }

    public void initializeDropDownList(Spinner numberOfPillsDropDownList, Spinner hourDropDownList, Spinner minuteDropDownList) {
        numberOfPillsDropDownList.setGravity(Gravity.CENTER);
        hourDropDownList.setGravity(Gravity.CENTER);
        minuteDropDownList.setGravity(Gravity.CENTER);
        String[] dosage = new String[]{
                "Dosage", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] hour = new String[]{
                "Hour", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        String[] minute = new String[]{
                "Min",
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
                "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "45", "46", "47", "48",
                "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
        List<String> dosageList = new ArrayList<>(Arrays.asList(dosage));
        List<String> hourList = new ArrayList<>(Arrays.asList(hour));
        List<String> minuteList = new ArrayList<>(Arrays.asList(minute));
        //Create dosage dropdown menu
        ArrayAdapter<String> dosageMenuArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, dosageList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from spinner, to be used for hints
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        //Create hour dropdown menu
        ArrayAdapter<String> hourMenuArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, hourList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from spinner,to be used for hints
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        //Create minute dropdown menu
        ArrayAdapter<String> minuteMenuArrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, minuteList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
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

    public void savePillScheduleToDatabase(View view, String pillNumber, int dailyDosage) {
        String pillName = "pill" + pillNumber;
        String[] toggleButton = new String[]{
                pillName + "MonToggle", pillName + "TuesToggle", pillName + "WedToggle", pillName + "ThursToggle", pillName + "FriToggle", pillName + "SatToggle", pillName + "SunToggle"};
        ArrayList<Integer> daysOfTheWeek = new ArrayList<>();

        //save the day of the week selected by user to database
        for (int i = 0; i < 7; i++) {
            int RID = view.getResources().getIdentifier(toggleButton[i], "id", requireActivity().getPackageName());
            ToggleButton dayOfTheWeekToggleButton = view.findViewById(RID);
            if (dayOfTheWeekToggleButton.isChecked()) {
                daysOfTheWeek.add(i);
            }
        }
        dbHelper.addToDBIntegerArray("medications/" + (Integer.parseInt(pillNumber) - 1) + "/days", daysOfTheWeek);

        //save the dosage schedule for single day to database
        dbHelper.deleteNode("medications/" + (Integer.parseInt(pillNumber) - 1) + "/dose");
        Map<String, Object> totalDosagePerPill = new HashMap<>();

        for (int i = 1; i <= dailyDosage; i++) {
            Map<String, Object> singleDosage = new HashMap<>();
            String numberOfPills = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber + i + "1"))).getSelectedItem().toString();
            String hour = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber + i + "2"))).getSelectedItem().toString();
            String minute = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber + i + "3"))).getSelectedItem().toString();
            if (!(numberOfPills.equals("Dosage") || hour.equals("Hour") || minute.equals("Min"))) {
                singleDosage.put("quantity", Integer.parseInt(numberOfPills));
                singleDosage.put("hour", Integer.parseInt(hour));
                singleDosage.put("minute", Integer.parseInt(minute));
            }
            totalDosagePerPill.put(String.valueOf(i), singleDosage);
        }
        dbHelper.addToDB("medications/" + (Integer.parseInt(pillNumber) - 1) + "/dose/", totalDosagePerPill);
        //save the pill name to database
        int pillNameUserInputID = view.getResources().getIdentifier(("pill" + pillNumber + "NameInput"), "id", requireActivity().getPackageName());
        String pillNameUserInput = ((EditText) requireActivity().findViewById(pillNameUserInputID)).getText().toString();
        if (!(pillNameUserInput.equals("Pill Name"))) {
            dbHelper.addToDB("medications/" + (Integer.parseInt(pillNumber) - 1) + "/name", pillNameUserInput);
        }
    }

    public void dosageTableCreator(View view, int dosageInputID, int dosageTableID, String pillNumber) {
        int numOfDosage = Integer.parseInt(((EditText) view.findViewById(dosageInputID)).getText().toString());
        TableLayout tableLayout = view.findViewById(dosageTableID);
        tableLayout.removeAllViews();
        for (int i = 1; i <= numOfDosage; i++) {
            TableRow tableRow = new TableRow(getContext());
            Spinner numberOfPillsDropDownList = new Spinner(getContext());
            Spinner hourDropDownList = new Spinner(getContext());
            Spinner minuteDropDownList = new Spinner(getContext());

            TextView rowTitle = new TextView(getContext());
            rowTitle.setText("Dose " + i);
            //first number represent pill
            //second number represent row
            //third number represent which block of the row
            rowTitle.setId(Integer.decode(pillNumber + i + "0"));
            rowTitle.setTextSize(18);
            rowTitle.setTypeface(null, Typeface.BOLD);

            numberOfPillsDropDownList.setId(Integer.decode(pillNumber + i + "1"));
            hourDropDownList.setId(Integer.decode(pillNumber + i + "2"));
            minuteDropDownList.setId(Integer.decode(pillNumber + i + "3"));
            initializeDropDownList(numberOfPillsDropDownList, hourDropDownList, minuteDropDownList);
            tableRow.addView(rowTitle);
            tableRow.addView(numberOfPillsDropDownList);
            tableRow.addView(hourDropDownList);
            tableRow.addView(minuteDropDownList);
            tableLayout.addView(tableRow);
        }
    }

    public void initializePillName(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();
        for (int i = 1; i <= 6; i++) {
            int pillNumber = i;
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("medications/" + (pillNumber - 1) + "/name").exists()) {
                        rootDbRef.child("medications/" + (pillNumber - 1) + "/name").get().addOnCompleteListener(task -> {
                            String pillName = (String) Objects.requireNonNull((task.getResult().getValue()));
                            int pillNameUserInputID = view.getResources().getIdentifier(("pill" + pillNumber + "NameInput"), "id", requireActivity().getPackageName());
                            ((EditText) requireActivity().findViewById(pillNameUserInputID)).setText(pillName);
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            rootDbRef.addListenerForSingleValueEvent(valueEventListener);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void informDispenser(boolean [] schedule){
        ArrayList<Integer> scheduleChange = new ArrayList<>();
        for (int i =0; i< schedule.length;i++){
            if(schedule[i]){
                scheduleChange.add(i);
            }
        }
        MQTT.MQTTSendIntListData(client,"pillNumber",scheduleChange,"medistation2021/pill/schedule");
    }
}
