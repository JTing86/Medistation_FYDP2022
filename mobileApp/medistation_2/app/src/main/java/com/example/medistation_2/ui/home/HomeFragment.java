package com.example.medistation_2.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.medistation_2.R;
import com.example.medistation_2.databinding.FragmentHomeBinding;
import com.example.medistation_2.ui.analysis.AnalysisFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

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
                    initializeMQTT(client,view, "medistation2021/heart-rate/send");
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
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    if (topic.equals("medistation2021/heart-rate/send")) {
                        TextView heartRateValue = view.findViewById((R.id.homeHeartRateValue));
                        heartRateValue.setText(new String(message.getPayload()));
                    } else if (topic.equals("medistation2021/temp/send")) {
                        TextView tempValue = view.findViewById((R.id.homeTempeValue));
                        tempValue.setText(new String(message.getPayload()));
                    } else if (topic.equals(("medistation2021/wifi-status/send"))) {
                        TextView wristbandWifiStatus = view.findViewById((R.id.homeWristbandSignalStrengthValue));
                        if (new String(message.getPayload()).equals("1")) {
                            wristbandWifiStatus.setText("Poor");
                        } else if (new String(message.getPayload()).equals("2")) {
                            wristbandWifiStatus.setText("Moderate");
                        } else if (new String(message.getPayload()).equals("3")) {
                            wristbandWifiStatus.setText(("Good"));
                        } else if (new String(message.getPayload()).equals("4")) {
                            wristbandWifiStatus.setText("Excellent");
                        } else
                            wristbandWifiStatus.setText("Not Connected");
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