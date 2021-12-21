#include <stdlib.h>
#include <PubSubClient.h>
#include <WiFi.h>
#include <Wire.h>
#include <WiFiManager.h>
#include <WiFiClientSecure.h>
#include <ArduinoJson.h>
#include "MAX30105.h"
#include "heartRate.h"

String sendRequest(String type, String path, String payload = "");

const unsigned long BPM_SAMPLE_PERIOD = 24*60*60*1000/5; // 5 samples per day
const unsigned long TEMP_SAMPLE_PERIOD = 24*60*60*1000/2; // 2 samples per day

const char* broker = "broker.hivemq.com";

const String database_url = "medistation-e235d-default-rtdb.firebaseio.com";


WiFiClientSecure secureClient;
WiFiClient espClient;
PubSubClient client(espClient);

MAX30105 sensor;

StaticJsonDocument<50> doc;
StaticJsonDocument<50> sendDoc;

unsigned long bpm_timer = 0;
unsigned long temp_timer = 0;


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

//  while(!sensor.begin(Wire, I2C_SPEED_FAST)) //Use default I2C port, 400kHz speed
//  {
//    Serial.println("MAX30105 was not found. Please check wiring/power. ");
//    
//  }
//
//  sensor.setup();
//  sensor.setPulseAmplitudeRed(0x0A);
//  sensor.setPulseAmplitudeGreen(0);
//
//  sensor.enableDIETEMPRDY(); //Enable the temp ready interrupt. This is required.

  Serial.println("start");
  WiFiManager wifiManager;
  //creates access point named medistation-AP, with password medistation
  wifiManager.autoConnect("medistation-AP", "medistation");

  Serial.println("connecting");
  while(WiFi.status() != WL_CONNECTED);

  Serial.println("Connected, IP address: ");
  Serial.println(WiFi.localIP());

  client.setServer(broker, 1883);
  client.setCallback(callback);

  bpm_timer = millis();
  temp_timer = bpm_timer;

  //Serial.println(sendRequest("GET","/Patient/name.json"));
}

// sends HTTP request
String sendRequest(String type, String path, String payload) {
  secureClient.setInsecure();//skip verification
  if (!secureClient.connect(database_url.c_str(), 443)){
    Serial.println("Connection failed!");
    return "Error";
  }
  else {
    Serial.println("Connected to server!");
    // Make a HTTP request:
    
    secureClient.println(type + " https://" + database_url + path + " HTTP/1.0");
    secureClient.println("Host: " + database_url);
    secureClient.println("Connection: close");
    
    if (type != "GET") {
      secureClient.print("Content-Length: ");
      secureClient.println(payload.length());
    }
    
    secureClient.println();

    if (type != "GET") {
      secureClient.println(payload);
    }

    while (secureClient.connected()) {
      String line = secureClient.readStringUntil('\n');
      if (line == "\r") {
        Serial.println("headers received");
        break;
      }
    }
    // if there are incoming bytes available
    // from the server, read them and print them:
    String response;
    while (secureClient.available()) {
      response += (char)secureClient.read();
    }
    
    secureClient.stop();

    return response;
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();

}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("medistation")) {
      Serial.println("connected");
      client.subscribe("medistation2021/#");
      client.publish("medistation2021/health-status/send", "{\"bpm\": 80, \"temp\": 30}");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 1 second");
      // Wait 5 seconds before retrying
      delay(1000);
    }
  }
}

byte getHeartRate() {
  const byte WINDOW_SIZE = 5;
  byte samples[WINDOW_SIZE];
  byte sample_index = 0;
  unsigned long last_beat = 0; //Time at which the last beat occurred

  byte bpm;
  byte avg_bpm = 0;
  
  while(sample_index < WINDOW_SIZE) {
    long irValue = sensor.getIR();
    
    if(checkForBeat(irValue)) {
      if(last_beat > 0) {
        bpm = 60 / ((millis() - last_beat) / 1000.0);
    
        if(bpm < 255 && bpm > 20) {
          samples[sample_index++] = (byte)bpm;
          avg_bpm += bpm / WINDOW_SIZE;
        }
      }

      last_beat = millis();
    }
  }

  return avg_bpm;
}

void loop() {
  // put your main code here, to run repeatedly:
  
  if(!client.connected()) {
    reconnect();
  }
  client.loop();

//  if(millis() - bpm_timer >= BPM_SAMPLE_PERIOD) {
//    byte avg_bpm = getHeartRate();
//    bpm_timer = millis();
//    // send to db
//  }
//
//  if(millis() - temp_timer >= TEMP_SAMPLE_PERIOD) {
//    //get temp and send to db
//    float temperature = sensor.readTemperature();
//    temp_timer = millis();
//  }


}
