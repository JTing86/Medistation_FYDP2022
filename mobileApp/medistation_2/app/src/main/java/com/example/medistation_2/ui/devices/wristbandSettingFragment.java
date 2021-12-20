package com.example.medistation_2.ui.devices;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.medistation_2.helperFunctions.dbHelper;
import com.google.android.gms.common.api.Api;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class wristbandSettingFragment extends Fragment {

    private static final String TAG = wristbandSettingFragment.class.getSimpleName();
    public MqttAndroidClient client;
    public static wristbandSettingFragment newInstance() {
        return new wristbandSettingFragment();
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

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(requireContext().getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //initializeMQTT(client,view);
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Button profileUserInfoSaveButton = view.findViewById(R.id.wristbandSymptomSaveButton);
        profileUserInfoSaveButton.setOnClickListener(v -> {
            dbHelper dbHelperCall = new dbHelper();
            dbHelperCall.AddSimpleStringData("/Patient/wristband/batnotification",((Spinner) requireActivity().findViewById(R.id.wristbandBatteryNotificationDropDown)).getSelectedItem().toString());
            dbHelperCall.AddSimpleStringData("/Patient/wristband/symptom1button",((Spinner) requireActivity().findViewById(R.id.wristbandSymptomButton1DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleStringData("/Patient/wristband/symptom2button",((Spinner) requireActivity().findViewById(R.id.wristbandSymptomButton2DropDownMenu)).getSelectedItem().toString());
            dbHelperCall.AddSimpleStringData("/Patient/wristband/symptom3button",((Spinner) requireActivity().findViewById(R.id.wristbandSymptomButton3DropDownMenu)).getSelectedItem().toString());
            MQTTSendData(client, ((Spinner) requireActivity().findViewById(R.id.wristbandBatteryNotificationDropDown)).getSelectedItem().toString());
        });
    }
    public void setupDropDownMenu (View view) {
        //set up drop down list
        Spinner batteryLevelNotification = view.findViewById(R.id.wristbandBatteryNotificationDropDown);
        Spinner symptom1DropDownList = view.findViewById(R.id.wristbandSymptomButton1DropDownMenu);
        Spinner symptom2DropDownList = view.findViewById(R.id.wristbandSymptomButton2DropDownMenu);
        Spinner symptom3DropDownlist = view.findViewById(R.id.wristbandSymptomButton3DropDownMenu);

        String[] batteryLevel = new String[]{
                "Battery Level","50%","25%","15%","10%","5%","Never"};
        List<String> batteryLevelList = new ArrayList<>(Arrays.asList(batteryLevel));
        ArrayAdapter<String> batteryMenuArrayAdapter = new ArrayAdapter<String> (requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, batteryLevelList) {
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
        String[] symptoms = new String[]{
                "Symptom","Constipation","Diarrhea","Dizziness","Dry Mouth","Headaches","Insomnia","Skin Rash"};
        List<String> symptomsList = new ArrayList<>(Arrays.asList(symptoms));

        ArrayAdapter<String> symptomButtonMenuArrayAdapter = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, symptomsList) {
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
        batteryMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        batteryLevelNotification.setAdapter(batteryMenuArrayAdapter);
        symptomButtonMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        symptom1DropDownList.setAdapter(symptomButtonMenuArrayAdapter);
        symptom2DropDownList.setAdapter(symptomButtonMenuArrayAdapter);
        symptom3DropDownlist.setAdapter(symptomButtonMenuArrayAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initializeMQTT (MqttAndroidClient client, View view) {
        String topic = "";//TODO: add topic;
        int qos = 2;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) { }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) { }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        if (client.isConnected()) {
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    Log.d(TAG,"message>>" + new String(message.getPayload()));
                    Log.d(TAG,"topic>>" + topic);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }
    }
    public void MQTTSendData (MqttAndroidClient client, String payload){
        String topic = "";//TODO: add topic
        byte[] encodedPayload;
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

}