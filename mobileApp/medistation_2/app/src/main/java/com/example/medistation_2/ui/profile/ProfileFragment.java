package com.example.medistation_2.ui.profile;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private MqttAndroidClient client;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initializeMQTT();
        //Initialize buttons
        Button profileUserInfoSaveButton = view.findViewById(R.id.profileSaveButton);
        Button profilePastMedicationSaveButton = view.findViewById(R.id.profilePastMedicationButton);
        Button profileSymptomSaveButton = view.findViewById(R.id.profileSymptomsButton);
        //Save user info record to database
        profileUserInfoSaveButton.setOnClickListener(v -> saveUserInfo(view));
        //Save past medication record to database
        profilePastMedicationSaveButton.setOnClickListener(v -> {
            try {
                addPastMedication(view);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        try {
            populateUserData(view);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setupDropDownMenu(view);
        //save symptom to database
        profileSymptomSaveButton.setOnClickListener(v -> saveSymptoms(view));
    }
    public void initializeMQTT() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(requireContext().getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setupDropDownMenu(View view) {
        //set up drop down list
        Spinner severityDropDownList = view.findViewById(R.id.profileSeverityDropDownList);
        Spinner hourDropDownList = view.findViewById(R.id.profileHourDropList);
        Spinner minuteDropDownList = view.findViewById(R.id.profileMinuteDropList);
        String[] severity = new String[]{"Severity", "Mild", "Moderate", "Severe"};
        String[] hour = new String[]{"Hour", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        String[] minute = new String[]{
                "Min",
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
                "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "45", "46", "47", "48",
                "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
        List<String> severityList = new ArrayList<>(Arrays.asList(severity));
        List<String> hourList = new ArrayList<>(Arrays.asList(hour));
        List<String> minuteList = new ArrayList<>(Arrays.asList(minute));
        //Create severity drop down menu
        ArrayAdapter<String> severityMenuArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, severityList) {
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
        //Create hour drop down menu
        ArrayAdapter<String> hourMenuArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, hourList) {
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
        //Create minute drop down menu
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
        severityMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        severityDropDownList.setAdapter(severityMenuArrayAdapter);
        hourMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        hourDropDownList.setAdapter(hourMenuArrayAdapter);
        minuteMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        minuteDropDownList.setAdapter(minuteMenuArrayAdapter);
    }

    public void populateUserData(@NonNull View view) throws InterruptedException {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();
        //display current user information stored in the database
        rootDbRef.child("name").get().addOnCompleteListener(task -> {
            EditText nameOfUser = view.findViewById(R.id.profileNameInput);
            nameOfUser.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("email").get().addOnCompleteListener(task -> {
            EditText emailOfUser = view.findViewById(R.id.profileEmailInput);
            emailOfUser.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("phone").get().addOnCompleteListener(task -> {
            EditText phoneOfUser = view.findViewById(R.id.profilePhoneInput);
            phoneOfUser.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //display current emergency contact stored in the database
        rootDbRef.child("caretaker").child("name").get().addOnCompleteListener(task -> {
            EditText emergencyNumber = view.findViewById(R.id.profileEmergencyNameInput);
            emergencyNumber.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("caretaker").child("phone").get().addOnCompleteListener(task -> {
            EditText emergencyNumber = view.findViewById(R.id.profileEmergencyNumberInput);
            emergencyNumber.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //past medication display section
        String[] rowNumber = {"0", "1", "2", "3", "4", "5"};
        for (String s : rowNumber) {
            rootDbRef.child("pastMedications/" + s).get().addOnCompleteListener(task -> {
                int medNameRID = view.getResources().getIdentifier("profileRow" + s + "Med", "id", requireActivity().getPackageName());
                Map<String, Object> pastMedication = (Map<String, Object>) Objects.requireNonNull(task.getResult()).getValue();
                if (pastMedication != null)
                    ((EditText) view.findViewById(medNameRID)).setText(Objects.requireNonNull(pastMedication.get("name")).toString());
            });
        }
        for (String s : rowNumber) {
            rootDbRef.child("pastMedications/" + s).get().addOnCompleteListener(task -> {
                int medDosageRID = view.getResources().getIdentifier("profileRow" + s + "Dosage", "id", requireActivity().getPackageName());
                Map<String, Object> pastMedication = (Map<String, Object>) Objects.requireNonNull(task.getResult()).getValue();
                if (pastMedication != null)
                    ((EditText) view.findViewById(medDosageRID)).setText(Objects.requireNonNull(pastMedication.get("dosage")).toString());
            });
        }
        for (String s : rowNumber) {
            rootDbRef.child("pastMedications/" + s).get().addOnCompleteListener(task -> {
                int medDurationRID = view.getResources().getIdentifier("profileRow" + s + "Duration", "id", requireActivity().getPackageName());
                Map<String, Object> pastMedication = (Map<String, Object>) Objects.requireNonNull(task.getResult()).getValue();
                if (pastMedication != null) {
                    String start = com.example.medistation_2.helperFunctions.dbHelper.fromEpochTime((Long) pastMedication.get("start"));
                    int index = start.indexOf(" ");
                    start = start.substring(0, index);
                    String end = com.example.medistation_2.helperFunctions.dbHelper.fromEpochTime((Long) pastMedication.get("end"));
                    index = end.indexOf(" ");
                    end = end.substring(0, index);
                    ((EditText) view.findViewById(medDurationRID)).setText(start + " " + end);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addPastMedication(View view) throws JSONException {
        String timeTrailingZero = " 00:00:00.000";
        String[] rowNumber = {"0", "1", "2", "3", "4", "5"};
        for (String s : rowNumber) {
            HashMap<String, Object> pastMedications = new HashMap<>();
            int medNameRID = view.getResources().getIdentifier("profileRow" + s + "Med", "id", requireActivity().getPackageName());
            int medDosageRID = view.getResources().getIdentifier("profileRow" + s + "Dosage", "id", requireActivity().getPackageName());
            int medDurationRID = view.getResources().getIdentifier("profileRow" + s + "Duration", "id", requireActivity().getPackageName());
            String[] startEnd = ((TextView) view.findViewById(medDurationRID)).getText().toString().split(" ");
            if (!((((TextView) view.findViewById(medDosageRID)).getText().toString()).equals("") || (((TextView) view.findViewById(medNameRID)).getText().toString()).equals(""))) {
                pastMedications.put("name", ((TextView) view.findViewById(medNameRID)).getText().toString());
                pastMedications.put("dosage", Integer.parseInt(((TextView) view.findViewById(medDosageRID)).getText().toString()));
                pastMedications.put("start", com.example.medistation_2.helperFunctions.dbHelper.toEpochTime(startEnd[0] + timeTrailingZero));
                pastMedications.put("end", com.example.medistation_2.helperFunctions.dbHelper.toEpochTime(startEnd[1] + timeTrailingZero));
                com.example.medistation_2.helperFunctions.dbHelper.addToDB("pastMedications/" + s, pastMedications);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveUserInfo(View view) {
        dbHelper.addToDB("name", ((EditText) view.findViewById(R.id.profileNameInput)).getText().toString());
        dbHelper.addToDB("email", ((EditText) view.findViewById(R.id.profileEmailInput)).getText().toString());
        dbHelper.addToDB("phone", ((EditText) view.findViewById(R.id.profilePhoneInput)).getText().toString());
        dbHelper.addToDB("caretaker/name", ((EditText) view.findViewById(R.id.profileEmergencyNameInput)).getText().toString());
        dbHelper.addToDB("caretaker/phone", Integer.parseInt(((EditText) view.findViewById(R.id.profileEmergencyNumberInput)).getText().toString()));
        ArrayList<String> phoneNumber = new ArrayList<>();
        phoneNumber.add(((EditText) view.findViewById(R.id.profilePhoneInput)).getText().toString());
        phoneNumber.add(((EditText) view.findViewById(R.id.profileEmergencyNumberInput)).getText().toString());
        MQTT.MQTTSendStrListData(client,"phone",phoneNumber,"medistation2021/phone");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    //TODO: Need to work on this based on new database structure
    public void saveSymptoms(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();
        String symptomName = ((EditText) view.findViewById(R.id.profileSymptomsNameInput)).getText().toString().toLowerCase();
        String symptomSeverity = ((Spinner) requireActivity().findViewById(R.id.profileSeverityDropDownList)).getSelectedItem().toString();
        String hour = ((Spinner) requireActivity().findViewById(R.id.profileHourDropList)).getSelectedItem().toString();
        String minute = ((Spinner) requireActivity().findViewById(R.id.profileMinuteDropList)).getSelectedItem().toString();
        if (!(symptomName.equals(" ") || hour.equals("Hour") || minute.equals("Min") || symptomSeverity.equals("Severity"))) {
            int symptomHour = Integer.parseInt(hour);
            int symptomMinute = Integer.parseInt(minute);
            int severity = 0;
            switch (symptomSeverity) {
                case "Moderate":
                    severity = 1;
                    break;
                case "Severe":
                    severity = 2;
                    break;
            }
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String formattedDate = df.format(currentTime) + " " + symptomHour + ":" + symptomMinute + ":00.000";
            Long time = dbHelper.toEpochTime(formattedDate)/1000;
            int finalSeverity = severity;

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("symptom/"+symptomName).exists()) {
                        rootDbRef.child("symptom/"+symptomName).get().addOnCompleteListener(task -> {
                            long numOfOccurrence = Objects.requireNonNull(task.getResult().getChildrenCount());
                            Map<String, Object> occurrence = new HashMap<>();
                            occurrence.put("date", time);
                            occurrence.put("severity", finalSeverity);
                            dbHelper.addToDB("symptom/"+symptomName+"/"+numOfOccurrence,occurrence);
                        });
                    } else {
                        Map<String, Object> occurrence = new HashMap<>();
                        occurrence.put("date", time);
                        occurrence.put("severity", finalSeverity);
                        dbHelper.addToDB("symptom/"+symptomName+"/0",occurrence);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            rootDbRef.addListenerForSingleValueEvent(valueEventListener);
        }
    }

}