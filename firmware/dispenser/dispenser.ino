#include <PubSubClient.h>
#include <WiFi.h>
#include <Wire.h>
#include <WiFiManager.h>
#include <WiFiClientSecure.h>
#include <ArduinoJson.h>
#include <climits>
#include <Tone32.h>
#include "REST.h"

#define I2C_SDA 4
#define I2C_SCL 5

#define GPIO_EXPAND_ADDR 0x20
#define GPIO_EXPAND_A 0x12
#define GPIO_EXPAND_B 0x13

#define BUZZER 14

typedef struct {
    int quantity;
    long time;
} Combination;

// global pill constants
const int MAX_PILLS = 5;
const int MAX_DAILY_DOSES = 3;

const uint8_t MOTOR_PINS[4] = {26, 13, 32, 33};

// API constants
const char* broker = "broker.hivemq.com";

const String database_url = "medistation-e235d-default-rtdb.firebaseio.com";

const String time_url = "showcase.api.linx.twenty57.net";
const String now = "/UnixTime/tounix?date=now";

const String day_url = "worldclockapi.com";
const String day_path = "/api/json/est/now";

// Twilio stuff
const String twilio_url = "api.twilio.com";
const String twilio_id = "AC11968c29a022136b8c2a3645eedbec00";
const String twilio_sms_path = "/2010-04-01/Accounts/" + twilio_id + "/Messages";
const String twilio_token = "QUMxMTk2OGMyOWEwMjIxMzZiOGMyYTM2NDVlZWRiZWMwMDpiMTMxZGEwNDk0MmZmMDAxZWY0NmIwODc0MDM3YzQ2OA==";
String user_phone;
String caretaker_phone;
String alert_msg = "User has not taken their pills in time";

REST rest;
WiFiClient espClient;
PubSubClient client(espClient);

DynamicJsonDocument doc(2048);

uint8_t pill_threshold[MAX_PILLS];
uint8_t num_pills[MAX_PILLS];

// variables for schedule
uint8_t weekday = 0;
Combination schedules[MAX_PILLS][MAX_DAILY_DOSES];

Combination next_pill;
uint8_t next_pill_index = 0;
unsigned long schedule_time = 0;

unsigned long millisecs_to_midnight = 0;

// pill taken status variables
const uint16_t check_weight_freq = 5*1000; // check weight every 5 seconds
const uint8_t reminder_freq = 60*1000; // remind them once a minute
const uint8_t alert_threshold = 15; // Send alert after 15 reminders (15 mins in this case)
uint8_t reminder_counter = 0;
uint8_t weight;
unsigned long check_weight_time = 0;
unsigned long remind_time = 0;
bool should_remind = false;


bool contains(JsonArray arr, int b) {
  for (int i = 0; i < arr.size(); i++) {
    if (b == arr[i]) {
      return true;
    }
  }

  return false;
}

int sortSchedulesAsc(const void* cmp1, const void* cmp2) {
  Combination* a = ((Combination *)cmp1);
  Combination* b = ((Combination *)cmp2);

  return a->time - b->time;
}

void loadScheduleHelper(JsonArray medicationsArr, int pillIndex, int hour, int minute) {
  
  JsonArray days = medicationsArr[pillIndex]["days"];
  JsonArray doses = medicationsArr[pillIndex]["dose"];

  if(!contains(days, weekday)) {
    return;
  }

  int quantity = 0;
  long time = 0;
  for (int i = 0; i < doses.size(); i++) {
    JsonObject dose = doses[i];
    quantity = dose["quantity"];
    time = (60*((int)dose["hour"]-hour)+((int)dose["minute"]-minute))*60*1000; // miliseconds from now

    if (time < 0) {
      quantity = 0;
      time = LONG_MAX;
    }

    schedules[pillIndex][i] = {quantity, time};
  }

  // sort schedules in ascending order based on time
  qsort(schedules[pillIndex], sizeof schedules[pillIndex] / sizeof (*schedules[pillIndex]), sizeof (*schedules[pillIndex]), sortSchedulesAsc);
}

void loadSchedules(JsonArray medicationsArr, int hour, int minute) {
  // get the schedules of pills for the day
  for (int i = 0; i < MAX_PILLS; i++) {
    loadScheduleHelper(medicationsArr, i, hour, minute);
  }
}

void printCombination(Combination val) {
  Serial.print("(quantity: ");
  Serial.print(val.quantity);
  Serial.print(", time:");
  Serial.print(val.time);
  Serial.print(")");
}

// utility function to view the schedules
void printSchedule() {
  for (int pill = 0; pill < MAX_PILLS; pill++) {
    Serial.print(pill);
    Serial.print(": ");
    for(int dose = 0; dose < MAX_DAILY_DOSES; dose++) {
      printCombination(schedules[pill][dose]);
      Serial.print(" ");
    }
    Serial.println("");
  }
}

void initializeNumPills(JsonArray medicationsArr, uint8_t thresholds[], uint8_t pills[]) {
  for(uint8_t i  = 0; i < MAX_PILLS; i++) {
    thresholds[i] = medicationsArr[i]["alertAmount"];
    pills[i] = medicationsArr[i]["currentAmount"];
  }
}

// parses the date retrieved from the REST endpoint to hour and minute (24h time)
void parseDate(String timestamp, uint8_t& hour, uint8_t& minute) {
  uint8_t i = timestamp.indexOf('T');
  hour = timestamp.substring(i + 1, i + 3).toInt();
  i = timestamp.indexOf(':');
  minute = timestamp.substring(i + 1, i + 3).toInt();
}

String parseFirstNumber(String str) {
  uint8_t start = str.indexOf('"');
  uint8_t end = str.indexOf('"', start + 1);

  return str.substring(start+1, end);
}

void callback(char* topic, byte* payload, unsigned int length) {
  if(strcmp(topic, "medistation2021/dispenser/alert") == 0) {
    // update pill low threshold
    deserializeJson(doc, (char*)payload);
    
    for(uint8_t i = 0; i < MAX_PILLS; i++) {
      pill_threshold[i] = doc["amount"][i]; 
    }
  }
  else if(strcmp(topic, "medistation2021/pill/schedule") == 0) {
    // update schedule by checking database
    String resp = rest.sendRequest("GET", database_url, "/medications.json");
    deserializeJson(doc, resp);
    JsonArray medicationsArr = doc.as<JsonArray>();
    
    loadSchedules(medicationsArr, 0, 0);
    schedule_time = millis();

    printSchedule();
  }
  else if(strcmp(topic, "medistation/phone")) {
    deserializeJson(doc, (char*)payload);
    user_phone = String((const char*)doc["phone"][0]);
    caretaker_phone = String((const char*)doc["phone"][1]);
    Serial.println(user_phone);
    Serial.println(caretaker_phone);
  }
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("dispenser")) {
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

void getNextPill() {
  int min_index = 0;
  for (int i = 0; i < MAX_PILLS; i++) {
    if(i > 0 && schedules[i][0].time < schedules[min_index][0].time) {
      min_index = i;
    }
  }

  next_pill = schedules[min_index][0];
  next_pill_index = min_index;
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

float getWeight() {
  //request 2 bytes from the force sensor
  const uint16_t max_val = 0.8*16384.0; // 16384 = 2^14
  const uint16_t min_val = 0.2*16384.0;
  const uint8_t rated_force = 25; // in Newtons
  uint16_t reading = 0;
  uint8_t status = 0;
  
  Wire.requestFrom(0x28, 2);

  // concatenate bytes
  while(Wire.available()) {
    reading = reading << 8;
    reading = reading | Wire.read();
  }

  // status is 2 MSBs of reading
  status = reading >> 14;
  // remove status from reading
  reading = reading & 0x3F;

  // status = 0 means valid data
  if(status == 0) {
    return ((float)(reading - min_val)*rated_force)/((float)(max_val - reading));
  }

  return -1;
}

void moveMotor() {
  for(int i = 0; i < 2048; i++) {
//    if(i > 0) {
//      digitalWrite(MOTOR_PINS[(i-1)%4], LOW);
//    }
    digitalWrite(MOTOR_PINS[i%4], HIGH);
    delay(7);
    digitalWrite(MOTOR_PINS[i%4], LOW);
  }
}

void moveMotorExpander(uint8_t port, uint8_t start) {
  int pins = start;
  
  for(int i = 0; i < 2048; i++) {
    if (i % 4 == 0) {
      pins = start;
    }
    Wire.beginTransmission(GPIO_EXPAND_ADDR);
    Wire.write(port);
    Wire.write(pins);  // value to send
    Wire.endTransmission();

    pins = pins << 1;
    delay(7);
  }

  Wire.beginTransmission(GPIO_EXPAND_ADDR);
  Wire.write(port);
  Wire.write(0);  // value to send
  Wire.endTransmission();
}

void buzz() {
  for (uint8_t i = 0; i < 4; i++) {
    tone(BUZZER, 264, 750, 0); // middle C = 264 Hz      
    noTone(BUZZER, 0);     
    delay(750);
  }     
}

void setup() {
  Serial.begin(9600);

  pinMode(BUZZER, OUTPUT);

  for(uint8_t i = 0; i < 4; i++) {
    pinMode(MOTOR_PINS[i], OUTPUT);
  }
  
  WiFiManager wifi_manager;
  //creates access point named medistation-AP, with password medistation
  wifi_manager.autoConnect("medistation-AP", "medistation");

  Serial.println("connecting");
  while(WiFi.status() != WL_CONNECTED);

  Serial.println("Connected, IP address: ");
  Serial.println(WiFi.localIP());

  client.setServer(broker, 1883);
  client.setCallback(callback);

  // Initialize GPIO expander to have ports as outputs
  Wire.begin(I2C_SDA, I2C_SCL, 400000);
  Wire.beginTransmission(GPIO_EXPAND_ADDR);
  Wire.write(0x00); // IODIRA register
  Wire.write(0x00); // set all of port A to outputs
  Wire.endTransmission();

  Wire.beginTransmission(GPIO_EXPAND_ADDR);
  Wire.write(0x01); // IODIRB register
  Wire.write(0x00); // set all of port B to outputs
  Wire.endTransmission();

  // turn off the GPIOS to start
  Wire.beginTransmission(GPIO_EXPAND_ADDR);
  Wire.write(GPIO_EXPAND_B);
  Wire.write(0);  // value to send
  Wire.endTransmission();

  Wire.beginTransmission(GPIO_EXPAND_ADDR);
  Wire.write(GPIO_EXPAND_A);
  Wire.write(0);  // value to send
  Wire.endTransmission();

  //initiliaze schedule to intial state
  for(int pill = 0; pill < MAX_PILLS; pill++) {
    for(int dose = 0; dose < MAX_DAILY_DOSES; dose++) {
      schedules[pill][dose] = {0,LONG_MAX};
    }
  }

  // get day of the week and current time
  String resp = rest.sendRequest("GET", day_url, day_path, "", 80);
  deserializeJson(doc, resp);
  const char* weekday_str = doc["dayOfTheWeek"];
  weekday = getWeekday(weekday_str);

  const char* timestamp = doc["currentDateTime"];
  uint8_t hour;
  uint8_t minute;
  parseDate(String(timestamp), hour, minute);
  schedule_time = millis();
  millisecs_to_midnight = (24*60 - (hour*60 + minute))*60*1000;

  // schedule stuff
  resp = rest.sendRequest("GET", database_url, "/medications.json");
  deserializeJson(doc, resp);
  JsonArray medicationsArr = doc.as<JsonArray>();
  
  loadSchedules(medicationsArr, hour, minute);
  printSchedule();

  getNextPill();

  // load pill thresholds and current number of pills
  initializeNumPills(medicationsArr, pill_threshold, num_pills);

  // get the phone numbers
  user_phone = parseFirstNumber(rest.sendRequest("GET", database_url, "/phone.json"));
  caretaker_phone = parseFirstNumber(rest.sendRequest("GET", database_url, "/caretaker/phone.json"));  
}

void loop() {
  if(!client.connected()) { 
    reconnect();
  }
  client.loop();

  if (millis() - schedule_time >= next_pill.time) {
    // dispense that shit
    for(uint8_t i = 0; i < next_pill.quantity; i++) {
      if(next_pill_index == 4) {
        moveMotor();
      }
      else if(next_pill_index <= 1) {
        moveMotorExpander(GPIO_EXPAND_A, 4*next_pill_index+1);
      }
      else {
        moveMotorExpander(GPIO_EXPAND_B, 4*(next_pill_index-2)+1);
      }
    }

    weight = getWeight();
    buzz();
    remind_time = millis();
    check_weight_time = remind_time;
    should_remind = true;
    
    // update next pill
    schedules[next_pill_index][0].quantity = 0;
    schedules[next_pill_index][0].time = LONG_MAX;
    qsort(schedules[next_pill_index], sizeof schedules[next_pill_index] / sizeof (*schedules[next_pill_index]), sizeof (*schedules[next_pill_index]), sortSchedulesAsc);
    
    getNextPill();
  }

  // new day started
  if (millis() - schedule_time >= millisecs_to_midnight) {
    weekday = (weekday + 1)%7;
    String resp = rest.sendRequest("GET", database_url, "/medications.json");
    deserializeJson(doc, resp);
    JsonArray medicationsArr = doc.as<JsonArray>();
    
    loadSchedules(medicationsArr, 0, 0);
    schedule_time = millis();
  }

  // send another reminder
  if (should_remind && millis() - remind_time >= reminder_freq) {
    buzz();
    reminder_counter++;
    remind_time = millis();
  }

  // check if they have taken the pills by checling the weight
  if (millis() - check_weight_time >= check_weight_freq && getWeight() < 0.5*weight) {
    should_remind = false;
    reminder_counter = 0;
  }

  // they didn't take the pills in time - send an alert
  if (reminder_counter >= alert_threshold) {
    String header = "Authorization: Basic " + twilio_token + "\n" + "Content-Type: application/x-www-form-urlencoded";
    String payload = "Body=" + alert_msg + "&From=%2B19106657562&To=%2B1";
    rest.sendRequest("POST", twilio_url, twilio_sms_path, payload + user_phone, 443, header);
    rest.sendRequest("POST", twilio_url, twilio_sms_path, payload + caretaker_phone, 443, header);
  }
}
