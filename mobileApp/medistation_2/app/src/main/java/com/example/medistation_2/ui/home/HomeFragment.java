package com.example.medistation_2.ui.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.JsonHelper;
import com.example.medistation_2.helperFunctions.MQTT;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
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
        initializeMQTT();
        dispenserRefillTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nextPillTime(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initializeMQTT() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(requireContext().getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MQTTSubscribe("medistation2021/wifi-status/send");
                    MQTTSubscribe("medistation2021/health-status/send");
                    MQTTSubscribe("medistation2021/battery/send");
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

    public void MQTTSubscribe(String topic) {
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
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_4);
                    break;
                /*case 2:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_2);
                    break;
                case 3:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_3);
                    break;
                case 4:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_4);
                    break;*/
                default:
                    wristbandWifiStatus.setImageResource(R.drawable.wifi_signal_0);
                    break;
            }
        } else if (device.equals("dispenser")) {
            switch (signalStrength) {
                case 1:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_1);
                    break;
                /*case 2:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_2);
                    break;
                case 3:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_3);
                    break;
                case 4:
                    dispenserWifiStatus.setImageResource(R.drawable.wifi_signal_4);
                    break;*/
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
                        TextView refillText = requireActivity().findViewById(R.id.homeRefillText);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextPillTime (View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();

        Calendar rightNow = Calendar.getInstance();
        Date currentDate = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("kk:mm:ss");
        String currentTime = dateFormatter.format(currentDate);
        int currentDayOfTheWeek = rightNow.get(Calendar.DAY_OF_WEEK) - 2;
        Long currentTimeInMin = totalWeekMinute((long) currentDayOfTheWeek,Long.parseLong(currentTime.substring(0,2)),Long.parseLong(currentTime.substring(3,5)));

        rootDbRef.child("medications").get().addOnCompleteListener(task -> {
            List<Object> allMedications;
            allMedications = (List<Object>) Objects.requireNonNull(task.getResult()).getValue();
            Long bestTime = Long.MAX_VALUE;
            for (int i = 0; i< Objects.requireNonNull(allMedications).size(); i++){
                HashMap <String,Object> medicine = (HashMap<String, Object>) allMedications.get(i);
                Long nextDoseInMinute = findNextDose(medicine,currentDayOfTheWeek,currentTime);
                if (Math.abs(nextDoseInMinute - currentTimeInMin) < bestTime) {
                    bestTime = Math.abs(nextDoseInMinute - currentTimeInMin);
                }
            }
            displayNextPillTime (bestTime,currentTime,view);

        });
    }
    public Long totalWeekMinute (Long dayOfTheWeek, Long hour, Long minute) {
        return dayOfTheWeek *24*60 + hour* 60 + minute;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Long findNextDose (HashMap <String, Object> medicine, int dayOfTheWeek, String currentTime){
        long currentHour = Long.parseLong(currentTime.substring(0,2));
        long currentMinute = Long.parseLong(currentTime.substring(3,5));
        int day = dayOfTheWeek;
        int daysArrayindex = 0;
        boolean foundDayOfTheWeek = false;
        ArrayList<Long> daysArray = (ArrayList<Long>) medicine.get("days");
        HashMap<String,Object> individualDose;
        do {
            for (int i = 0; i < Objects.requireNonNull(daysArray).size(); i++) {
                if (daysArray.get(i).equals((long) day % 7)) {
                    foundDayOfTheWeek = true;
                    daysArrayindex = i;
                }
            }
            if (foundDayOfTheWeek) {
                ArrayList<Map<String,Object>> allDoses;
                allDoses = (ArrayList<Map <String,Object>>) medicine.get("dose");
                day = day % 7;
                if (day == dayOfTheWeek) {
                    for (int i = 0; i< Objects.requireNonNull(allDoses).size(); i++){
                        individualDose = (HashMap<String, Object>) allDoses.get(i);
                        long hour = (long) individualDose.get("hour");
                        long minute = (long) individualDose.get("minute");
                        if(hour > currentHour || (hour == currentHour && minute >= currentMinute)){
                            return totalWeekMinute((long) day,hour,minute);
                        }
                    }
                    if (daysArrayindex == daysArray.size() - 1 ) {
                        individualDose = (HashMap<String, Object>) allDoses.get(0);
                        Long hour = (Long) individualDose.get("hour");
                        Long minute = (Long) individualDose.get("minute");
                        return totalWeekMinute(daysArray.get(0),hour,minute);
                    }
                    else {
                        individualDose = (HashMap<String, Object>) allDoses.get(0);
                        Long hour = (Long) individualDose.get("hour");
                        Long minute = (Long) individualDose.get("minute");
                        return totalWeekMinute(daysArray.get(daysArrayindex+1),hour,minute);
                    }
                }
                assert allDoses != null;
                individualDose = (HashMap<String, Object>) allDoses.get(0);
                Long hour = (Long) individualDose.get("hour");
                Long minute = (Long) individualDose.get("minute");
                return totalWeekMinute((long) day,hour,minute);
            }
            day = day + 1;
        } while (true);
    }
    public void displayNextPillTime (Long bestTime, String currentTime, View view){
        long bestTimeTemp = bestTime;
        Long nextPillDayCount = (long) Math.floor(bestTime/24/60);
        bestTimeTemp = bestTime % (24*60);
        Long nextPillHourCount = (long) Math.floor(bestTimeTemp/60);
        long nextPillMinuteCount = bestTime - nextPillDayCount*60*24 - nextPillHourCount*60;
        if (!nextPillDayCount.equals( (long) 0 )) {
            ((TextView) view.findViewById(R.id.nextPillFromNow)).setText("In " + nextPillDayCount + " Days " + nextPillHourCount + " Hours " + nextPillMinuteCount + " Minutes");
        }
        else if (!nextPillHourCount.equals((long) 0)){
            ((TextView) view.findViewById(R.id.nextPillFromNow)).setText("In " + nextPillHourCount + " Hours " + nextPillMinuteCount + " Minutes");
        }
        else {
            ((TextView) view.findViewById(R.id.nextPillFromNow)).setText("In " + nextPillMinuteCount + " Minutes");
        }

        long nextPillMinute = nextPillMinuteCount + Long.parseLong(currentTime.substring(3,5));
        long minuteCarryover = (long) 0;
        if (nextPillMinute >= 60){
            nextPillMinute = nextPillMinute - 60;
            minuteCarryover++;
        }
        Long nextPillHour =  nextPillHourCount + Long.parseLong(currentTime.substring(0,2)) + minuteCarryover;
        while (nextPillHour>=24){
            nextPillHour = nextPillHour - 24;
        }
        String nextPillHourDisplay = "";
        String nextPillMinuteDisplay = "";
        if (nextPillHour < 10)
            nextPillHourDisplay = "0"+nextPillHour;
        else
            nextPillHourDisplay = String.valueOf(nextPillHour);
        if (nextPillMinute < 10)
            nextPillMinuteDisplay = "0" + nextPillMinute;
        else
            nextPillMinuteDisplay = String.valueOf(nextPillMinute);
        ((TextView) view.findViewById(R.id.nextPillTime)).setText("Next pill at " + nextPillHourDisplay + ":" +nextPillMinuteDisplay);
    }
}