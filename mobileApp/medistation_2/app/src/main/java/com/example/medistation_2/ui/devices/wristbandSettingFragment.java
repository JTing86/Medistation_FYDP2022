package com.example.medistation_2.ui.devices;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.MQTT;
import com.example.medistation_2.helperFunctions.dbHelper;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class wristbandSettingFragment extends Fragment {

    private static final String TAG = wristbandSettingFragment.class.getSimpleName();
    private MqttAndroidClient client;
    @Override
    public void onResume(){
        initializeMQTT();
        super.onResume();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wristband_setting, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupDropDownMenu(view);
        initializeMQTT();

        Button profileUserInfoSaveButton = view.findViewById(R.id.wristbandSymptomSaveButton);
        profileUserInfoSaveButton.setOnClickListener(v -> saveButtonPressed());
    }
    public void initializeMQTT(){
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
            }
        });
    } catch (MqttException e) {
        e.printStackTrace();
    }
}
    public void setupDropDownMenu(View view) {
        //set up drop down list
        Spinner batteryLevelNotification = view.findViewById(R.id.wristbandBatteryNotificationDropDown);
        Spinner symptom1DropDownList = view.findViewById(R.id.wristbandSymptomButton1DropDownMenu);
        Spinner symptom2DropDownList = view.findViewById(R.id.wristbandSymptomButton2DropDownMenu);
        Spinner symptom3DropDownList = view.findViewById(R.id.wristbandSymptomButton3DropDownMenu);

        String[] batteryLevel = new String[]{
                "Battery Level", "50%", "25%", "15%", "10%", "5%", "Never"};
        List<String> batteryLevelList = new ArrayList<>(Arrays.asList(batteryLevel));
        ArrayAdapter<String> batteryMenuArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, batteryLevelList) {
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
        String[] symptoms = new String[]{
                "Symptom", "Constipation", "Diarrhea", "Dizziness", "Dry Mouth", "Headaches", "Insomnia", "Skin Rash"};
        List<String> symptomsList = new ArrayList<>(Arrays.asList(symptoms));

        ArrayAdapter<String> symptomButtonMenuArrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, symptomsList) {
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
        batteryMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        batteryLevelNotification.setAdapter(batteryMenuArrayAdapter);
        symptomButtonMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        symptom1DropDownList.setAdapter(symptomButtonMenuArrayAdapter);
        symptom2DropDownList.setAdapter(symptomButtonMenuArrayAdapter);
        symptom3DropDownList.setAdapter(symptomButtonMenuArrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveButtonPressed() {
        String wristbandNotifBatLevel = ((Spinner) requireActivity().findViewById(R.id.wristbandBatteryNotificationDropDown)).getSelectedItem().toString();
        String button1 = ((Spinner) requireActivity().findViewById(R.id.wristbandSymptomButton1DropDownMenu)).getSelectedItem().toString();
        String button2 = ((Spinner) requireActivity().findViewById(R.id.wristbandSymptomButton2DropDownMenu)).getSelectedItem().toString();
        String button3 = ((Spinner) requireActivity().findViewById(R.id.wristbandSymptomButton3DropDownMenu)).getSelectedItem().toString();
        //Wristband Battery Notification Level
        if (wristbandNotifBatLevel.equals("Never")) {
            dbHelper.addToDB("wristband/alertLevel",0);
            MQTT.MQTTSendData(client, "threshold", 0,requireContext().getString(R.string.batteryThreshold));
        } else if (!wristbandNotifBatLevel.equals("Battery Level")) {
            int batteryLevel = Integer.parseInt(wristbandNotifBatLevel.substring(0, wristbandNotifBatLevel.length() - 1));
            dbHelper.addToDB("wristband/alertLevel",batteryLevel);
            MQTT.MQTTSendData(client, "threshold", batteryLevel, requireContext().getString(R.string.batteryThreshold));
        }

        ArrayList <String> wristbandButtons = new ArrayList<>();
        if (!button1.equals("Symptom")){
            wristbandButtons.add(button1);
        }
        if (!button2.equals("Symptom")){
            wristbandButtons.add(button2);
        }
        if (!button3.equals("Symptom")){
            wristbandButtons.add(button3);
        }
        dbHelper.addToDBStrArray("wristband/button",wristbandButtons);
        MQTT.MQTTSendStrListData(client,"symptoms", wristbandButtons,"medistation2021/wristband/buttons");
    }
}