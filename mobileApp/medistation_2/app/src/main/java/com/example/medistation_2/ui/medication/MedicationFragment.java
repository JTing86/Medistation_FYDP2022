package com.example.medistation_2.ui.medication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
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
import java.util.Collections;
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


        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(requireContext().getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "MQTT successfully connected in dispenser setting fragment");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "MQTT client in dispenser setting fragment did not connect successfully");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        dosageTableCreator(view, R.id.pill1DosageTable, "1");
        dosageTableCreator(view, R.id.pill2DosageTable, "2");
        dosageTableCreator(view, R.id.pill3DosageTable, "3");
        dosageTableCreator(view, R.id.pill4DosageTable, "4");
        dosageTableCreator(view, R.id.pill5DosageTable, "5");

        Button scheduleSaveButton = view.findViewById(R.id.scheduleSaveButton);
        scheduleSaveButton.setOnClickListener(v -> {
            if (!((EditText) view.findViewById(R.id.pill1NameInput)).getText().equals(" "))
                savePillScheduleToDatabase(view, "1");
            if (!((EditText) view.findViewById(R.id.pill2NameInput)).getText().equals(" "))
                savePillScheduleToDatabase(view, "2");
            if (!((EditText) view.findViewById(R.id.pill3NameInput)).getText().toString().equals(" "))
                savePillScheduleToDatabase(view, "3");
            if (!((EditText) view.findViewById(R.id.pill4NameInput)).getText().equals(" "))
                savePillScheduleToDatabase(view, "4");
            if (!(((EditText) view.findViewById(R.id.pill5NameInput)).getText().toString().length() == 0))
                savePillScheduleToDatabase(view, "5");
            MQTT.MQTTSendData(client, "", "", "medistation2021/pill/schedule");
        });
        initializeUserInfo(view);
    }

    public void initializeDropDownList(Spinner numberOfPillsDropDownList, Spinner hourDropDownList, Spinner minuteDropDownList) {
        numberOfPillsDropDownList.setGravity(Gravity.CENTER);
        hourDropDownList.setGravity(Gravity.CENTER);
        minuteDropDownList.setGravity(Gravity.CENTER);
        String[] dosage = new String[]{
                "Dosage", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] hour = new String[]{
                "Hour", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        String[] minute = new String[]{
                "Min",
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
                "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43","44", "45", "46", "47", "48",
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

    public void savePillScheduleToDatabase(View view, String pillNumber) {
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
        List<String> totalDailyDosage = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String numberOfPills = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber + i + "1"))).getSelectedItem().toString();
            String hour = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber + i + "2"))).getSelectedItem().toString();
            String minute = ((Spinner) requireActivity().findViewById(Integer.decode(pillNumber + i + "3"))).getSelectedItem().toString();
            if (!(numberOfPills.equals("Dosage") || hour.equals("Hour") || minute.equals("Min"))) {
                totalDailyDosage.add(hour + minute + numberOfPills);
            }
        }
        Collections.sort(totalDailyDosage);
        for (int i = 0; i < totalDailyDosage.size(); i++) {
            Map<String, Object> singleDosage = new HashMap<>();
            String hour = String.valueOf(totalDailyDosage.get(i)).substring(0, 2);
            String minute = String.valueOf(totalDailyDosage.get(i)).substring(2, 4);
            String numberOfPills = String.valueOf(totalDailyDosage.get(i)).substring(4);
            singleDosage.put("quantity", Integer.parseInt(numberOfPills));
            singleDosage.put("hour", Integer.parseInt(hour));
            singleDosage.put("minute", Integer.parseInt(minute));
            totalDosagePerPill.put(String.valueOf(i), singleDosage);
        }
        dbHelper.addToDB("medications/" + (Integer.parseInt(pillNumber) - 1) + "/dose/", totalDosagePerPill);
        //save the pill name to database
        int pillNameUserInputID = view.getResources().getIdentifier(("pill" + pillNumber + "NameInput"), "id", requireActivity().getPackageName());
        String pillNameUserInput = ((EditText) requireActivity().findViewById(pillNameUserInputID)).getText().toString();
        pillNameUserInput = pillNameUserInput.replace(" ", "");
        if (!(pillNameUserInput.equals("Pill Name"))) {
            dbHelper.addToDB("medications/" + (Integer.parseInt(pillNumber) - 1) + "/name", pillNameUserInput);
        }
    }

    public void dosageTableCreator(View view, int dosageTableID, String pillNumber) {
        TableLayout tableLayout = view.findViewById(dosageTableID);
        tableLayout.removeAllViews();
        for (int i = 1; i <= 3; i++) {
            TableRow tableRow = new TableRow(getContext());
            Spinner numberOfPillsDropDownList = new Spinner(getContext());
            Spinner hourDropDownList = new Spinner(getContext());
            Spinner minuteDropDownList = new Spinner(getContext());

            TextView rowTitle = new TextView(getContext());
            rowTitle.setText("Dose " + i);
            //first number represent pill
            //second number represent row
            //third number represent which column of the row
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

    public void initializeUserInfo(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();

        ValueEventListener pillNameValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int pillNumber = 1; pillNumber <= 5; pillNumber++) {
                    if (dataSnapshot.child("medications/" + (pillNumber - 1) + "/name").exists()) {
                        int finalPillNumber = pillNumber;
                        rootDbRef.child("medications/" + (pillNumber - 1) + "/name").get().addOnCompleteListener(task -> {
                            String pillName = (String) Objects.requireNonNull((task.getResult().getValue()));
                            int pillNameUserInputID = view.getResources().getIdentifier(("pill" + finalPillNumber + "NameInput"), "id", requireActivity().getPackageName());
                            ((EditText) requireActivity().findViewById(pillNameUserInputID)).setText(pillName);
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        rootDbRef.addListenerForSingleValueEvent(pillNameValueEventListener);

        ValueEventListener DayOfTheWeekValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int pillNumber = 1; pillNumber <= 5; pillNumber++) {
                    if (dataSnapshot.child("medications/" + (pillNumber - 1) + "/days").exists()) {
                        int finalPillNumber = pillNumber;
                        rootDbRef.child("medications/" + (pillNumber - 1) + "/days").get().addOnCompleteListener(task -> {
                            List<Long> days = (List<Long>) Objects.requireNonNull(task.getResult().getValue());
                            for (int j = 0; j < days.size(); j++) {
                                String daysOfTheWeek = "";
                                switch (String.valueOf(days.get(j))) {
                                    case "0":
                                        daysOfTheWeek = "Mon";
                                        break;
                                    case "1":
                                        daysOfTheWeek = "Tues";
                                        break;
                                    case "2":
                                        daysOfTheWeek = "Wed";
                                        break;
                                    case "3":
                                        daysOfTheWeek = "Thurs";
                                        break;
                                    case "4":
                                        daysOfTheWeek = "Fri";
                                        break;
                                    case "5":
                                        daysOfTheWeek = "Sat";
                                        break;
                                    case "6":
                                        daysOfTheWeek = "Sun";
                                        break;
                                }
                                int dayOfTheWeekToggle = view.getResources().getIdentifier(("pill" + finalPillNumber + daysOfTheWeek + "Toggle"), "id", requireActivity().getPackageName());
                                ((ToggleButton) requireActivity().findViewById(dayOfTheWeekToggle)).setChecked(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        rootDbRef.addListenerForSingleValueEvent(DayOfTheWeekValueEventListener);

        ValueEventListener timeOfTheDayValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int pillNumber = 1; pillNumber <= 5; pillNumber++) {
                    if (dataSnapshot.child("medications/" + (pillNumber - 1) + "/dose").exists()) {
                        int finalPillNumber = pillNumber;
                        rootDbRef.child("medications/" + (pillNumber - 1) + "/dose").get().addOnCompleteListener(task -> {
                            List<HashMap<String, Long>> dose = (List<HashMap<String, Long>>) Objects.requireNonNull(task.getResult().getValue());
                            for (int k = 0; k < dose.size(); k++) {
                                HashMap<String, Long> singleDose = dose.get(k);
                                Long hour = singleDose.get("hour");
                                Long minute = singleDose.get("minute");
                                Long quantity = singleDose.get("quantity");
                                int quantityDropDownList = view.getResources().getIdentifier((finalPillNumber + String.valueOf(k + 1) + 1), "id", requireActivity().getPackageName());
                                int hourDropDownList = view.getResources().getIdentifier((finalPillNumber + String.valueOf(k + 1) + 2), "id", requireActivity().getPackageName());
                                int minuteDropDownList = view.getResources().getIdentifier((finalPillNumber + String.valueOf(k + 1) + 3), "id", requireActivity().getPackageName());
                                ((Spinner) requireActivity().findViewById(quantityDropDownList)).setSelection((int) (quantity + 0));
                                ((Spinner) requireActivity().findViewById(hourDropDownList)).setSelection((int) (hour + 1));
                                ((Spinner) requireActivity().findViewById(minuteDropDownList)).setSelection((int) (minute + 1));
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        rootDbRef.addListenerForSingleValueEvent(timeOfTheDayValueEventListener);
    }
}
