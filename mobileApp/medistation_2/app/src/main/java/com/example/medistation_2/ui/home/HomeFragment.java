package com.example.medistation_2.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.databinding.FragmentHomeBinding;
import com.example.medistation_2.helperFunctions.JsonHelper;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import com.example.medistation_2.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    public MqttAndroidClient client;
    private static final String TAG = HomeFragment.class.getSimpleName();

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
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(requireContext().getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    initializeMQTT(client,view,"medistation2021/wifi-status/send");//TODO: add dispenser wifi status
                    initializeMQTT(client,view, "medistation2021/health-status/send");
                    initializeMQTT(client,view, "medistation2021/temp/send");
                    initializeMQTT(client,view,"medistation2021/battery/send");

                    MQTTSendData(client,"medistation2021/wifi-status/request","");//TODO: need to add request for dispenser wifi status
                    MQTTSendData(client,"medistation2021/heart-rate/request","");
                    MQTTSendData(client,"medistation2021/temp/request","");
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initializeMQTT (MqttAndroidClient client, View view, String topic) {
        int qos = 1;
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
                public void connectionLost(Throwable cause) {}
                @Override
                public void messageArrived(String topic, MqttMessage message) throws JSONException {
                    if (topic.equals(requireContext().getString(R.string.healthStatusSend))) {
                        JsonHelper json = new JsonHelper();
                        TextView heartRateValue = view.findViewById(R.id.homeHeartRateValue);
                        TextView temperature = view.findViewById(R.id.homeTempValue);
                        Log.d(TAG,String.valueOf(json.intDecoder("bpm",new String(message.getPayload()))));
                        Log.d(TAG,String.valueOf(json.intDecoder("temp",new String(message.getPayload()))));
                        heartRateValue.setText(String.valueOf(json.intDecoder("bpm",new String(message.getPayload()))));
                        temperature.setText(String.valueOf(json.intDecoder("temp",new String(message.getPayload()))));
                    }
                    else if  (topic.equals(requireContext().getString(R.string.wifiStatusSend))) {
                        Log.d(TAG,"wifiStat");
                        TextView wristbandWifiStatus = view.findViewById((R.id.homeWristbandSignalStrengthValue));
                        JsonHelper json = new JsonHelper();
                        int signalStrength = json.intDecoder ("quality",new String(message.getPayload()));
                        Log.d(TAG,String.valueOf(signalStrength));
                        switch (signalStrength) {
                            case 1:
                                wristbandWifiStatus.setText("Poor");
                                break;
                            case 2:
                                wristbandWifiStatus.setText("Moderate");
                                break;
                            case 3:
                                wristbandWifiStatus.setText("Good");
                                break;
                            case 4:
                                wristbandWifiStatus.setText("Excellent");
                                break;
                            default:
                                wristbandWifiStatus.setText("Not Connected");
                                break;
                        }
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }
    }
    public void MQTTSendData (MqttAndroidClient client,String topic,String payload){
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