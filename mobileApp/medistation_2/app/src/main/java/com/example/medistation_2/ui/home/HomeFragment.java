package com.example.medistation_2.ui.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.JsonHelper;
import com.example.medistation_2.helperFunctions.MQTT;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.example.medistation_2.ui.profile.ProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    ;
    private MqttAndroidClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initializeMQTT(view);
        dispenserRefillTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initializeMQTT(View view) {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(requireContext().getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MQTTSubscribe("medistation2021/wifi-status/send", view);
                    MQTTSubscribe("medistation2021/health-status/send", view);
                    MQTTSubscribe("medistation2021/battery/send", view);
                    MQTT.MQTTSendData(client, "battery", "", requireContext().getString(R.string.batteryRequest));
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

    public void MQTTSubscribe(String topic, View view) {
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
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
        if (client.isConnected()) {
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws JSONException {
                    if (topic.equals(requireContext().getString(R.string.wifiStatusSend))) {
                        wifiStatusDisplay(message);
                    } else if (topic.equals(requireContext().getString(R.string.healthStatusSend))) {
                        healthStatusDisplay(message);
                    } else if (topic.equals(requireContext().getString((R.string.batterySend)))) {
                        int batteryLevel = Integer.parseInt(String.valueOf(JsonHelper.intDecoder("percentage", new String(message.getPayload()))));
                        TextView wristbandBatteryLevel = (TextView) requireActivity().findViewById(R.id.homeWristbandBatteryLevel);
                        wristbandBatteryLevel.setText(batteryLevel + "%");
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
        }
    }

    public void wifiStatusDisplay(MqttMessage message) throws JSONException {
        int signalStrength = JsonHelper.intDecoder("quality", new String(message.getPayload()));
        String device = JsonHelper.stringDecoder("device", new String(message.getPayload()));
        ImageView wristbandWifiStatus = requireActivity().findViewById(R.id.homeWristbandWifiSignal);
        ImageView dispenserWifiStatus = requireActivity().findViewById(R.id.homeDispenserWifiSignal);
        if (device.equals("wristband")) {
            switch (signalStrength) {
                case 1:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_1);
                    break;
                case 2:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_2);
                    break;
                case 3:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_3);
                    break;
                case 4:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_4);
                    break;
                default:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_0);
                    break;
            }
        } else if (device.equals("dispenser")) {
            switch (signalStrength) {
                case 1:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_1);
                    break;
                case 2:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_2);
                    break;
                case 3:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_3);
                    break;
                case 4:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_4);
                    break;
                default:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_0);
                    break;
            }
        }
    }

    public void healthStatusDisplay( MqttMessage message) throws JSONException {
        TextView heartRateValue = requireActivity().findViewById(R.id.homeHeartRateValue);
        TextView temperatureValue = requireActivity().findViewById(R.id.homeTempValue);
        int temperature = JsonHelper.intDecoder("temp",new String (message.getPayload()));
        int heartRate = JsonHelper.intDecoder("bpm", new String(message.getPayload()));
        heartRateValue.setText(String.valueOf(heartRate));
        if ((temperature-37)>0.5) {
            temperatureValue.setText(temperature + " (Higher Than Average)");
            temperatureValue.setTextColor(Color.RED);
        } else if ((temperature-37)<-0.5) {
            temperatureValue.setText(temperature + " (Lower Than Average)");
            temperatureValue.setTextColor(Color.BLUE);
        } else {
            temperatureValue.setText(temperature + " (Normal)");
        }
    }

    public void dispenserRefillTime() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("/medications").exists()) {
                    rootDbRef.child("/medications").get().addOnCompleteListener(task -> {
                        ArrayList<Objects> medication = (ArrayList<Objects>) task.getResult().getValue();
                        ArrayList<Integer> currentAmount = new ArrayList<>();
                        for (int i = 0; i < Objects.requireNonNull(medication).size(); i++) {
                            try {
                                currentAmount.add(JsonHelper.intDecoder("currentAmount", String.valueOf(medication.get(i))));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        int lowestCurrentAmount = Collections.min(currentAmount);
                        TextView refillText = (TextView) requireActivity().findViewById(R.id.homeRefillText);
                        refillText.setText("Refill in " + lowestCurrentAmount + " day(s)");
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