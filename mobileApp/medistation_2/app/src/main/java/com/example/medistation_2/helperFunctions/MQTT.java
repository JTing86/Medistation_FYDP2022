package com.example.medistation_2.helperFunctions;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MQTT {
    private static final String TAG = MQTT.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void MQTTSendData(MqttAndroidClient client, String key, String payload, String topic) {
        byte[] encodedPayload;
        String MQTTPayload = "{\"" + key + "\": " + payload + "}";
        try {
            encodedPayload = MQTTPayload.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.d(TAG,key);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void MQTTSendData(MqttAndroidClient client, String key, int payload, String topic) {
        byte[] encodedPayload;
        String MQTTPayload = "{\"" + key + "\": " + payload + "}";
        try {
            encodedPayload = MQTTPayload.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void MQTTSendStrListData(MqttAndroidClient client, String key, ArrayList<String> payload, String topic) {
        byte[] encodedPayload;
        StringBuilder MQTTPayload = new StringBuilder("{\"" + key + "\": [");
        for (String s: payload){
            MQTTPayload.append("\"").append(s).append("\", ");
        }
        MQTTPayload = new StringBuilder(MQTTPayload.substring(0, MQTTPayload.length() - 2));
        MQTTPayload.append("]}");
        try {
            encodedPayload = MQTTPayload.toString().getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void MQTTSendIntListData(MqttAndroidClient client, String key, ArrayList<Integer> payload, String topic) {
        byte[] encodedPayload;
        StringBuilder MQTTPayload = new StringBuilder("{\"" + key + "\": [");
        for (Integer s: payload){
            MQTTPayload.append(s).append(", ");
        }
        MQTTPayload = new StringBuilder(MQTTPayload.substring(0, MQTTPayload.length() - 2));
        MQTTPayload.append("]}");
        try {
            encodedPayload = MQTTPayload.toString().getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
