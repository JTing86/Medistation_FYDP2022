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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dispenserSettingFragment extends Fragment {

    private static final String TAG = dispenserSettingFragment.class.getSimpleName();
    public MqttAndroidClient client;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dispenser_setting, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupDropDownMenu(view);

        Button refillSaveButton = view.findViewById(R.id.refillSaveButton);
        refillSaveButton.setOnClickListener(v -> refillNotification());

        Button calibrateSaveButton = view.findViewById(R.id.calibrationSaveButton);
        calibrateSaveButton.setOnClickListener(v -> calibration());

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
    }

    public void setupDropDownMenu(View view) {
        //set up drop down list
        Spinner container1DropDownMenu = view.findViewById(R.id.dispenserContainer1DropDownMenu);
        Spinner container2DropDownMenu = view.findViewById(R.id.dispenserContainer2DropDownMenu);
        Spinner container3DropDownMenu = view.findViewById(R.id.dispenserContainer3DropDownMenu);
        Spinner container4DropDownMenu = view.findViewById(R.id.dispenserContainer4DropDownMenu);
        Spinner container5DropDownMenu = view.findViewById(R.id.dispenserContainer5DropDownMenu);

        String[] numberOfPills = new String[]{"Amount", "30", "20", "15", "10", "5", "Never"};
        List<String> numberOfPillsList = new ArrayList<>(Arrays.asList(numberOfPills));

        ArrayAdapter<String> symptomButtonMenuArrayAdapters = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, numberOfPillsList) {
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
        symptomButtonMenuArrayAdapters.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        container1DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container2DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container3DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container4DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
        container5DropDownMenu.setAdapter(symptomButtonMenuArrayAdapters);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void refillNotification() {
        String currentContainer1Pill = ((Spinner) requireActivity().findViewById(R.id.dispenserContainer1DropDownMenu)).getSelectedItem().toString();
        String currentContainer2Pill = ((Spinner) requireActivity().findViewById(R.id.dispenserContainer2DropDownMenu)).getSelectedItem().toString();
        String currentContainer3Pill = ((Spinner) requireActivity().findViewById(R.id.dispenserContainer3DropDownMenu)).getSelectedItem().toString();
        String currentContainer4Pill = ((Spinner) requireActivity().findViewById(R.id.dispenserContainer4DropDownMenu)).getSelectedItem().toString();
        String currentContainer5Pill = ((Spinner) requireActivity().findViewById(R.id.dispenserContainer5DropDownMenu)).getSelectedItem().toString();
        ArrayList<Integer> refillNotificationNumbers = new ArrayList<>();
        if (!(currentContainer1Pill.equals("Amount") || currentContainer1Pill.equals("Never"))) {
            dbHelper.addToDB("/medications/0/alertAmount", Integer.parseInt(currentContainer1Pill));
            refillNotificationNumbers.add(Integer.parseInt(currentContainer1Pill));
        } else if (currentContainer1Pill.equals("Never")) {
            dbHelper.addToDB("/medications/0/alertAmount", 0);
            refillNotificationNumbers.add(0);
        } else {
            refillNotificationNumbers.add(null);
        }
        if (!(currentContainer2Pill.equals("Amount") || currentContainer2Pill.equals("Never"))) {
            dbHelper.addToDB("/medications/1/alertAmount", Integer.parseInt(currentContainer2Pill));
            refillNotificationNumbers.add(Integer.parseInt(currentContainer2Pill));
        } else if (currentContainer2Pill.equals("Never")) {
            dbHelper.addToDB("/medications/1/alertAmount", 0);
            refillNotificationNumbers.add(0);
        } else {
            refillNotificationNumbers.add(null);
        }
        if (!(currentContainer3Pill.equals("Amount") || currentContainer3Pill.equals("Never"))) {
            dbHelper.addToDB("/medications/2/alertAmount", Integer.parseInt(currentContainer3Pill));
            refillNotificationNumbers.add(Integer.parseInt(currentContainer3Pill));
        } else if (currentContainer3Pill.equals("Never")) {
            dbHelper.addToDB("/medications/2/alertAmount", 0);
            refillNotificationNumbers.add(0);
        } else {
            refillNotificationNumbers.add(null);
        }
        if (!(currentContainer4Pill.equals("Amount") || currentContainer4Pill.equals("Never"))) {
            dbHelper.addToDB("/medications/3/alertAmount", Integer.parseInt(currentContainer4Pill));
            refillNotificationNumbers.add(Integer.parseInt(currentContainer4Pill));
        } else if (currentContainer4Pill.equals("Never")) {
            dbHelper.addToDB("/medications/3/alertAmount", 0);
            refillNotificationNumbers.add(0);
        } else {
            refillNotificationNumbers.add(null);
        }
        if (!(currentContainer5Pill.equals("Amount") || currentContainer5Pill.equals("Never"))) {
            dbHelper.addToDB("/medications/4/alertAmount", Integer.parseInt(currentContainer5Pill));
            refillNotificationNumbers.add(Integer.parseInt(currentContainer5Pill));
        } else if (currentContainer5Pill.equals("Never")) {
            dbHelper.addToDB("/medications/4/alertAmount", 0);
            refillNotificationNumbers.add(0);
        } else {
            refillNotificationNumbers.add(null);
        }
        MQTT.MQTTSendIntListData(client, "amount", refillNotificationNumbers, "medistation2021/dispenser/alert");

    }

    public void calibration() {
        String container1RefillAmount = ((EditText) requireActivity().findViewById(R.id.calibrationContainer1Input)).getText().toString();
        String container2RefillAmount = ((EditText) requireActivity().findViewById(R.id.calibrationContainer2Input)).getText().toString();
        String container3RefillAmount = ((EditText) requireActivity().findViewById(R.id.calibrationContainer3Input)).getText().toString();
        String container4RefillAmount = ((EditText) requireActivity().findViewById(R.id.calibrationContainer4Input)).getText().toString();
        String container5RefillAmount = ((EditText) requireActivity().findViewById(R.id.calibrationContainer5Input)).getText().toString();

        if (!(container1RefillAmount.equals("")))
            dbHelper.addToDB("/medications/0/currentAmount", Integer.parseInt(container1RefillAmount));
        if (!(container2RefillAmount.equals("")))
            dbHelper.addToDB("/medications/1/currentAmount", Integer.parseInt(container2RefillAmount));
        if (!(container3RefillAmount.equals("")))
            dbHelper.addToDB("/medications/2/currentAmount", Integer.parseInt(container3RefillAmount));
        if (!(container4RefillAmount.equals("")))
            dbHelper.addToDB("/medications/3/currentAmount", Integer.parseInt(container4RefillAmount));
        if (!(container5RefillAmount.equals("")))
            dbHelper.addToDB("/medications/4/currentAmount", Integer.parseInt(container5RefillAmount));
    }
    //TODO, add function to send notification when number of pills in dispenser is low
}