#include <stdlib.h>
#include <PubSubClient.h>
#include <WiFi.h>
#include <Wire.h>
#include <WiFiManager.h>
#include <WiFiClientSecure.h>
#include <ArduinoJson.h>
#include <InputDebounce.h>
#include "MAX30105.h"
#include "heartRate.h"
#include <TimeLib.h>

#define BATTERY_PIN 35

#define I2C_SDA 4
#define I2C_SCL 33

#define DEBOUNCE_DELAY 20 //ms

String sendRequest(String type, String base_url, String path, String payload = "", int port = 443, String header = "");

const uint8_t MAX_DAYS_NO_WIFI = 2;
const uint8_t BPM_SAMPLE_FREQ = 24; // samples per day
const unsigned long BPM_SAMPLE_PERIOD = 24*60*60*1000/BPM_SAMPLE_FREQ; // 24 samples per day
const unsigned long TEMP_SAMPLE_PERIOD = 24*60*60*1000; // 1 sample per day
const uint8_t NUM_BUTTONS = 3;
const uint8_t MAX_SYMPTOM_LOGS = 10;

const uint8_t BUTTON_PIN[NUM_BUTTONS] = {25,26,27};

const char* broker = "broker.hivemq.com";

const String database_url = "medistation-e235d-default-rtdb.firebaseio.com";

const String time_url = "showcase.api.linx.twenty57.net";
const String time_now = "/UnixTime/tounix?date=now";

const String day_url = "worldclockapi.com";
const String day_path = "/api/json/est/now";


WiFiClientSecure secureClient;
WiFiClient espClient;
PubSubClient client(espClient);

MAX30105 sensor;

DynamicJsonDocument doc(2048);

static InputDebounce symptom_button[NUM_BUTTONS];
String symptom_name[NUM_BUTTONS];
uint8_t button_presses[NUM_BUTTONS][MAX_SYMPTOM_LOGS];
unsigned long button_times[NUM_BUTTONS][MAX_SYMPTOM_LOGS];
uint8_t button_counter[NUM_BUTTONS];
bool button_lost_wifi = false;

unsigned long bpm_timer = 0;
unsigned long temp_timer = 0;

uint8_t bpm_index = 0;
uint8_t bpm_vals[MAX_DAYS_NO_WIFI][BPM_SAMPLE_FREQ];
uint8_t days_no_wifi = 0;
unsigned long sample_time[MAX_DAYS_NO_WIFI];

uint8_t batt_threshold = 0;
bool batt_alert_sent = false;

// temperautre sampling is built scalably to work with sample frequencies higher than once per day
float temperature[MAX_DAYS_NO_WIFI];
unsigned long sample_offset = 24*60*60/2; // ensures the temperature sample is always far from when the push happens so the sample is guranteed to be there


// twilio stuff
const String twilio_url = "api.twilio.com";
const String twilio_id = "AC11968c29a022136b8c2a3645eedbec00";
const String twilio_sms_path = "/2010-04-01/Accounts/" + twilio_id + "/Messages";
const String twilio_token = "QUMxMTk2OGMyOWEwMjIxMzZiOGMyYTM2NDVlZWRiZWMwMDpiMTMxZGEwNDk0MmZmMDAxZWY0NmIwODc0MDM3YzQ2OA==";
String user_phone;

TwoWire I2C = TwoWire(0);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);

  pinMode(7,INPUT);
  pinMode(8,INPUT);

  I2C.begin(I2C_SDA, I2C_SCL, I2C_SPEED_FAST);

  while(!sensor.begin(I2C, I2C_SPEED_FAST)) //Use default I2C port, 400kHz speed
  {
    Serial.println("MAX30105 was not found. Please check wiring/power. ");
    
  }

  sensor.setup();
  sensor.setPulseAmplitudeRed(0x0A);
  sensor.setPulseAmplitudeGreen(0);

  sensor.enableDIETEMPRDY(); //Enable the temp ready interrupt. This is required.

  Serial.println("start");

//  pinMode(BATTERY_PIN, INPUT);
  analogReadResolution(11);
  
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

  user_phone = tryParseFirstNumber(sendRequest("GET", database_url, "/phone.json"));

  // get the name of the symptoms recorded by the buttons
  String resp = sendRequest("GET", database_url, "/wristband/button.json");
  deserializeJson(doc, resp);
  JsonArray symptoms = doc.as<JsonArray>();

  // also initialize the buttons as inputs
  for(uint8_t i = 0; i < NUM_BUTTONS;i++) {
    symptom_name[i] = String((const char*)symptoms[i]);
    symptom_button[i].registerCallbacks(buttonPressedCallback, buttonReleasedCallback);
    symptom_button[i].setup(BUTTON_PIN[i], DEBOUNCE_DELAY, InputDebounce::PIM_EXT_PULL_UP_RES);
    button_counter[i] = 0;
  }

  Serial.println(measureBatt());
}

long convertLong(String str) {
  String temp_str = "";
  for(int i = 0; i < str.length(); i++) {
    if(str.charAt(i) >= '0' && str.charAt(i) <= '9') {
      temp_str += str.charAt(i);
    }
  }

  return atol(temp_str.c_str());
}

int getWeekday(String weekday_str) {
  if(weekday_str == "Monday") {
    return 0;
  }
  else if(weekday_str == "Tuesday") {
    return 1;
  }
  else if(weekday_str == "Wednesday") {
    return 2;
  }
  else if(weekday_str == "Thursday") {
    return 3;
  }
  else if(weekday_str == "Friday") {
    return 4;
  }
  else if(weekday_str == "Saturday") {
    return 5;
  }
  else {
    return 6;
  }
}

void parseDate(String timestamp, uint8_t& hour, uint8_t& minute) {
  uint8_t i = timestamp.indexOf('T');
  hour = timestamp.substring(i + 1, i + 3).toInt();
  i = timestamp.indexOf(':');
  minute = timestamp.substring(i + 1, i + 3).toInt();
}

// sends HTTP request
// port is 443 for HTTPS, 80 for HTTP
String sendRequest(String type, String base_url, String path, String payload, int port, String header) {
  secureClient.setInsecure();//skip verification
  if (!secureClient.connect(base_url.c_str(), 443)){
    Serial.println("Connection failed!");
    return "Error";
  }
  else {
    Serial.println("Connected to server!");
    // Make a HTTP request:
    
    secureClient.println(type + " https://" + base_url + path + " HTTP/1.0");
    secureClient.println("Host: " + base_url);
    secureClient.println("Connection: close");

    if (header != "") {
      secureClient.println(header);
    }
    
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
  Serial.println(topic);
  if(strcmp(topic, "medistation2021/battery/request") == 0) {
    // send battery level
//    int batt_read = analogRead(BATTERY_PIN);
//    float voltage = 4/3*3.3*batt_read/1024; // 10 bit ADC
    int percentage = (int)measureBatt(); // equation of line. Transform from volatage to percentage

    String payload = "{\"percentage\":" + String(percentage) + "}";
    client.publish("medistation2021/battery/send", payload.c_str());
  }
  else if(strcmp(topic, "medistation2021/battery/threshold") == 0) {
    // update battery low threshold
    deserializeJson(doc, (char*)payload);
    batt_threshold = (int)doc["threshold"];
    Serial.println(batt_threshold);
  }
  else if(strcmp(topic, "medistation2021/health-status/request") == 0) {
    // send BPM and and temp and push to DB
    Serial.println("hello");
    doc.clear();
    int temp = sensor.readTemperature();
    int bpm = (int)getHeartRate();
//    int temp = 30;
//    int bpm = 70;
    doc["temp"] = temp;
    doc["bpm"] = bpm;
    String payload;
    serializeJson(doc, payload);
    Serial.println(payload);

    if(!client.connected()) {
      reconnect();
    }
    client.publish("medistation2021/health-status/send", payload.c_str());
    // don't need to try to push with WiFi because if we got this message, we must have WiFi
    // Only need to push first column since there will only be one column with WiFi
    pushBPM(bpm_vals[0], sample_time[0]);
    pushTemp(temperature[0], sample_time[0]); 

  }
  else if(strcmp(topic, "medistation2021/wristband/buttons") == 0) {
    // update symptom button values
    deserializeJson(doc, (char*)payload);

    for(uint8_t i = 0; i < NUM_BUTTONS; i++) {
      symptom_name[i] = String((const char*)doc["symptoms"][i]);
      Serial.println(symptom_name[i]);
    }
  }
  else if(strcmp(topic, "medistation/phone")) {
    deserializeJson(doc, (char*)payload);
    user_phone = String((const char*)doc["phone"][0]);
  }

}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("medistation")) {
      Serial.println("connected");
      client.subscribe("medistation2021/#");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 1 second");
      // Wait 5 seconds before retrying
      delay(1000);
    }
  }
}

// measures battery level as a percentage
float measureBatt() {
  int batt_read = analogRead(BATTERY_PIN);
  float voltage = (4.0/3.0)*3.3*((float)batt_read/2047.0); // 11 bit ADC
  
  return (100.0/(4.2-3.3)*(voltage-3.3)); // equation of line. Transform from volatage to percentage
}

uint8_t getHeartRate() {
  const byte WINDOW_SIZE = 3;
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

String tryParseFirstNumber(String str) {
  uint8_t start = str.indexOf('"');
  if(start == -1) {
    return "";
  }
  
  uint8_t end = str.indexOf('"', start + 1);
  if(end == -1) {
    return "";
  }

  return str.substring(start+1, end);
}

bool isInteger(String str) {
  for(uint8_t i = 0; i < str.length(); i++) {
    if (str.charAt(i) < '0' || str.charAt(i) > '9') {
      return false;
    }
  }

  return true;
}

String getIndex(String path) {
  String resp = sendRequest("GET",  database_url, path + ".json?orderBy\"$key\"&limitToLast=1");
  
  // try parsing for the index
  if(resp == "null") {
    return "-1";
  }
  
  String index = tryParseFirstNumber(resp);

  // if parsing didn't work, do a count
  if(!isInteger(index)) {
    String resp = sendRequest("GET",  database_url, path + ".json?shallow=true");
    deserializeJson(doc, resp);
    index =  String(doc.size()-1);
  }

  return index;
}

void buttonPressedCallback(uint8_t pinIn)
{
  return;
}

void buttonReleasedCallback(uint8_t pinIn)
{
  // hacky but works for now
  uint8_t index = pinIn - 25;
  
  // get time of occurrence
  unsigned long timestamp = convertLong(sendRequest("GET", time_url, time_now));

  if(WiFi.status() == WL_CONNECTED) {
    pushSymptom(symptom_name[index], timestamp, 1);
  }
  else {
    button_times[index][button_counter[index]] = timestamp;
    button_presses[index][button_counter[index]] = 1;
    button_counter[index]++;
    button_lost_wifi = true;
  }
}

void pushSymptom(String symptom, unsigned long timestamp, uint8_t level) {
//  String resp = sendRequest("GET",  database_url, "/symptom/" + symptom + ".json?orderBy\"$key\"&limitToLast=1");
  String index = getIndex("/symptom/" + symptom);
  index = String(index.toInt()+1);

  doc.clear();
  JsonObject root = doc.createNestedObject(index);

  root["date"] = timestamp;
  root["severity"] = 1;

  String payload;
  serializeJson(doc, payload);
  sendRequest("PATCH",  database_url, "/symptom/" + symptom + ".json", payload);

  doc.clear();
}

void bulkPushSymptom(String symptom, unsigned long timestamp[], uint8_t level, uint8_t size) {
//  String resp = sendRequest("GET",  database_url, "/symptom/" + symptom + ".json?orderBy\"$key\"&limitToLast=1");
  String index_str = getIndex("/symptom/" + symptom);
  unsigned int index = index_str.toInt()+1;

  doc.clear();
  for(uint8_t i = 0; i < size; i ++) {
    JsonObject current = doc.createNestedObject(String(index+i));
    current["date"] = timestamp[i];
    current["severity"] = level;
  }

  String payload;
  serializeJson(doc, payload);
  sendRequest("PATCH",  database_url, "/symptom/" + symptom + ".json", payload);

  doc.clear();
}

void pushTemp(float temp, unsigned long timestamp) {
//  String resp = sendRequest("GET",  database_url, "/temp/date.json?orderBy\"$key\"&limitToLast=1");
  String index = getIndex("/temp/date");
  index = String(index.toInt()+1);
  // get time in epoch seconds
  long time = timestamp;

  doc.clear();
  // update the date
  doc[index] = time;
  String payload;
  serializeJson(doc, payload);
  sendRequest("PATCH", database_url, "/temp/date.json", payload);

  // update the value
  doc[index] = temp;
  payload;
  serializeJson(doc, payload);
  sendRequest("PATCH", database_url, "/temp/value.json", payload);
  
  doc.clear();  
}

void pushBPM(uint8_t samples[], uint8_t timestamp) {
//  String resp = sendRequest("GET",  database_url, "/heartRate.json?orderBy\"$key\"&limitToLast=1");
  String index = getIndex("/heartRate");
  index = String(index.toInt()+1);

  doc.clear();
  JsonObject heart_rate = doc.createNestedObject(index);
  JsonArray values = heart_rate.createNestedArray("value");
  
  for(uint8_t i = 0; i < BPM_SAMPLE_FREQ; i++) {
    values.add(samples[i]);
  }

  heart_rate["date"] = timestamp;

  String payload;
  serializeJson(doc, payload);
  sendRequest("PATCH", database_url, "/heartRate.json", payload);
  doc.clear();
}

void pushSleepQuality(uint8_t quality, unsigned long start_time, unsigned long end_time) {
  //convertLong(sendRequest("GET", time_url, now));
  String day_index = getIndex("/sleep");
  int sleep_index = getIndex("/sleep/" + day_index).toInt() + 1;

  String resp = sendRequest("GET", database_url, "/sleep/" + day_index + ".json");
  deserializeJson(doc, resp);
  JsonArray start = doc["start"];

  if(sleep_index > 1 && day(start_time) == day(start[start.size()-1])){
    JsonArray end = doc["end"];
    JsonArray quality = doc["quality"];

    quality[sleep_index] = quality;
    start[sleep_index] = start_time;
    end[sleep_index] = end_time;

    String payload;
    serializeJson(doc, payload);

    sendRequest("PUT", database_url, "/sleep/" + day_index + ".json", payload);
  }
  else {
    day_index = String(day_index.toInt() + 1);
    sleep_index = 0;
    doc.clear();

    JsonObject root = doc.createNestedObject(day_index);
    JsonArray end = root.createNestedArray("end");
    JsonArray startArr = root.createNestedArray("start");
    JsonArray quality = root.createNestedArray("quality");

    quality[sleep_index] = quality;
    startArr[sleep_index] = start_time;
    end[sleep_index] = end_time;

    String payload;
    serializeJson(doc, payload);

    sendRequest("PATCH", database_url, "/sleep.json", payload);
  }

  doc.clear();
}

// tries to push all ready samples for BPM and temperature to the DB if there is wifi
// if there is no wifi, samples are put into the next column
// passes in global parameters for better clarity
void tryPushAll(unsigned long times[], uint8_t& wifi_counter) {
  if(WiFi.status() == WL_CONNECTED) {
    // trick to push only the first day of samples when days without WiFi = 0 or 1
    // want to push first day only because first day will only try to get pushed when ready
    // 2nd or higher day will try any time there is WiFi
    // This ensures only days with complete samples are pushed
    uint8_t i = 0;
    do {
      // can use the same timestamp for both BPM and temperature since it only needs to be accurate to the day
      pushBPM(bpm_vals[i], times[i]);
      pushTemp(temperature[i], times[i]);
      i++;
    } while(i < wifi_counter);

    // Shift unpushed days to the front
    for(uint8_t sample = 0; sample < BPM_SAMPLE_FREQ; sample++) {
      bpm_vals[0][sample] = bpm_vals[wifi_counter][sample];
    }
    temperature[0] = temperature[wifi_counter];
    
    wifi_counter = 0;
  }
  else {
    wifi_counter++;
  }
}

void loop() {  
  if(!client.connected()) {
    reconnect();
  }
  client.loop();

  if(millis() - bpm_timer >= BPM_SAMPLE_PERIOD) {
    Serial.println("hello");
    // store the time of the first sample for the date tag in the DB
    if(bpm_index == 0) {
      sample_time[days_no_wifi] = convertLong(sendRequest("GET", time_url, time_now));
    }

    // measure BPM
    bpm_vals[days_no_wifi][bpm_index++] = getHeartRate();
    bpm_timer = millis();

    // push all sensor readings to DB (BPM and temp) if there is wifi
    if(bpm_index == BPM_SAMPLE_FREQ - 1) {
      bpm_index = 0;
      tryPushAll(sample_time, days_no_wifi);
    }
  }

  if(WiFi.status() == WL_CONNECTED) {
    if(days_no_wifi > 0) {
      tryPushAll(sample_time, days_no_wifi);
    }
    if(button_lost_wifi) {
      for(uint8_t i = 0; i < NUM_BUTTONS; i++) {
        if(button_counter[i] > 0) {
          bulkPushSymptom(symptom_name[i], button_times[i], 1, button_counter[i]);
        }
        button_counter[i] = 0;
      }
      button_lost_wifi = false;
    }
  }

  if(millis() - temp_timer >= TEMP_SAMPLE_PERIOD - sample_offset) {
    //get temp
    temperature[days_no_wifi] = sensor.readTemperature();
    temp_timer = millis();
    sample_offset = 0;
  }

  // battery is low - send an alert
  if(measureBatt() <= batt_threshold) {
    String header = "Authorization: Basic " + twilio_token + "\n" + "Content-Type: application/x-www-form-urlencoded";
    String msg = "Wristband battery is low. Charge now";
    String payload = "Body=" + msg + "&From=%2B19106657562&To=%2B1";
    sendRequest("POST", twilio_url, twilio_sms_path, payload + user_phone, 443, header);
    batt_alert_sent = true;
  }
  else if (batt_alert_sent) {
    // getting here would mean they've charged it since
    batt_alert_sent = false;
  }

  unsigned long current_time = millis();
  for(uint8_t i = 0; i < NUM_BUTTONS; i++) {
    symptom_button[i].process(current_time);
  }
}
