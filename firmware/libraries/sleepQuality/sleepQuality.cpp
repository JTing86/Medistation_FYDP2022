/*
    sleepQuality.cpp - Library for determinging sleep quality for the wristband
*/
#include <Arduino.h>
#include <Wire.h> // communicate with I2C devices.
#include "MPU6050_tockn.h"
// #include <Time.h> // for timestamp
// #include <DS1307RTC.h>  // a basic DS1307 library that returns time as a time_t

#include "sleepQuality.h"

#define MODE_ACTIVE 0
#define MODE_SLEEP 1
#define MODE_DEEP_SLEEP 2


#define ONE_MIN 60
#define SAMPLE_FREQ 10// per 10 second
#define SAMPLE_AVG 1// per minute
#define MODE_DURATION 10// 10mins monitor duration before mode switching
#define TOTAL_SAMPLE_NUM 24// per day, with each hour one sample


#define ALLOWED_ERR_SEC 10 //shorter time interval with more space for error
#define ALLOWED_ERR_MIN 5
#define ALLOWED_ERR_HOUR 1

#define DEMO_SAMPLE_FREQ 2 // how many times in a second
#define DEMO_REFRESH_PERIOD 5000 //5 seconds refresh and update data


//#define I2C_SCL 21

//#define I2C_SDA 4

// Initialize Variables //////////////////////////////////////////////////

float AccX = 0;
float AccY = 0;
float AccZ = 0;

float GyroX = 0;
float GyroY = 0;
float GyroZ = 0;

float roll = 0;
float pitch = 0;
float yaw = 0;

float accAngleX = 0;
float accAngleY = 0;
float gyroAngleX = 0;
float gyroAngleY = 0;
float gyroAngleZ = 0;

float AccErrorX = 0;
float AccErrorY = 0;
float GyroErrorX = 0;
float GyroErrorY = 0;
float GyroErrorZ = 0;

float elapsedTime = 0;
float currentTime = 0;
float previousTime = 0;

float initGX = 0;
float initGY = 0;
float initGZ = 0;

//only gets initilized or reset in init()
static ACCL_t avg_acclData;
static GYRO_t avg_gyroData;
static int sample_count;
static int cycle_count;
static int deep_sleep_count;
static int second_count;
static int interval_count;
static unsigned long previousMillis;
static int error_range_factor;
static int sensitivity_factor;
static int sleep_status;
static int movement_factor; // 0 if stable, 1 if moving
static TwoWire* i2c_port;

const int MPU = 0x68; // MPU6050 I2C address
const int SAMPLE_NUM = ONE_MIN/SAMPLE_FREQ;
const unsigned long interval = SAMPLE_FREQ*1000; // convert to ms

ACCL_t acclData;
GYRO_t gyroData;
float avg_init = 1;
float avg_accum = 0;
int c = 0;
unsigned long currentMillis;
const int ledPin =  13;
int ledState = LOW;

MPU6050 mpu6050(Wire);

//char timestamp[20]; // declare a character variable that contains timestamp
uint8_t mode = MODE_ACTIVE;
// sleep quality algo //////////////////////////////////////////////////////////
void Sensor_Init(TwoWire *wire)
{
    // mpu6050 = new MPU6050(wire);
    mpu6050.setWire(wire);
}
void SleepQuality_Init(TwoWire &port)
{
    i2c_port = &port;
    i2c_port->begin();                      // Initialize comunication
    i2c_port->beginTransmission(MPU);       // Start communication with MPU6050 // MPU=0x68
    i2c_port->write(0x6B);                  // Talk to the register 6B
    i2c_port->write(0x00);                  // Make reset - place a 0 into the 6B register
    i2c_port->endTransmission(true);        //end the transmission

    //pinMode(ledPin, OUTPUT);
    // for the sleep_quality loop
    //acclData = {0,0};
    gyroData = {0,0,0};
    sensitivity_factor = 0.5; // 0-1 the larger it is, the lesser it is sensitive to movement
    sleep_status = 0; // poor sleep/active
    sample_count = 0;
    cycle_count = 0;
    deep_sleep_count = 0;
    avg_accum = 0;

    interval_count = 0;
    previousMillis = 0;
    currentMillis = 0;
    movement_factor = 0;
    error_range_factor = ALLOWED_ERR_SEC;
    // mode = MODE_ACTIVE;

    Sensor_Init(i2c_port);
    Sensor_begin();
    Sensor_calcGyroOffsets();

    initGX = mpu6050.getGyroAngleX();
    initGY = mpu6050.getGyroAngleY();
    initGZ = mpu6050.getGyroAngleZ();

}

void Sensor_Update(void)
{
    mpu6050.update();

}
void Sensor_begin(void)
{
    mpu6050.begin();
}
void Sensor_calcGyroOffsets(void)
{
    mpu6050.calcGyroOffsets(true);
}

void Sensor_printRPY(void)
{
    Serial.print(mpu6050.getGyroAngleX());
    Serial.print("/");Serial.print(mpu6050.getGyroAngleY());
    Serial.print("/");Serial.print(mpu6050.getGyroAngleZ());
    Serial.print("/");Serial.println(mode);
}

int Demo_SleepQuality_Analyzer(float sensitivity)
{
    // Sensor_Update();
    sensitivity_factor = sensitivity;

    if (false)
    {
        deep_sleep_count = 0;
        mode = MODE_DEEP_SLEEP;
    }
    else
    {
        if (cycle_count == 3) // sets mode
        { // reach here per 15 seconds, is movement factor remains zero

            cycle_count = 0;
            // sample_count = 0;
            if (mode == MODE_ACTIVE && movement_factor < 1) // for three 5sec periods, no obvious movement compare to initial data
            {
                mode = MODE_SLEEP;
            }
            else if (mode == MODE_SLEEP && movement_factor > 0 )
            {
                mode = MODE_ACTIVE;
            }
            else if (mode == MODE_SLEEP && movement_factor < 1 ){
                deep_sleep_count +=1;
            }
            // else if (mode == MODE_DEEP_SLEEP && movement_factor == 0){
            //     deep_sleep_count +=1;
            // }


            Serial.print("-------------------------mode: --(1 for sleep)--");
            Serial.println(mode);
            movement_factor = 0;
        }
        else
        {
            if (sample_count == 6) // collect 6 data in 3 seconds
            { //reach here per 3 seconds
                sample_count = 0;//reset sample count
                cycle_count += 1;


                //second_count += 1; //increment data record by 1 minute
                //error_range_factor = ALLOWED_ERR_MIN;
                movement_factor += DEMO_Movement_Analyzer();// temp,needs to add heartrate sensor
                //movement_factor +=0;
                //float avg_init = abs(initGX)+abs(initGY)+abs(initGZ);
                float avg_accum = abs(avg_gyroData.roll) + abs(avg_gyroData.pitch) + abs(avg_gyroData.yaw);
                //float error_factor = (abs(avg_init - avg_accum)) / abs(avg_init);

                   Serial.print("---movement indicator: ----");
                    Serial.print(movement_factor);
                // Serial.print("---movement abs(avg_init): ----");
                // Serial.print(abs(avg_init));
                // Serial.print("---movement avg_accum: ----");
                // Serial.println(avg_accum);
                // //Sensor_printRPY();
                Serial.print("---movement abs(avg_init - avg_accum): ----");
                Serial.println(abs(avg_init - avg_accum));


                // reset accumulated average


                avg_gyroData.roll = 0;
                avg_gyroData.pitch = 0;
                avg_gyroData.yaw = 0;
                // initGX = mpu6050.getGyroAngleX();
                // initGY = mpu6050.getGyroAngleY();
                // initGZ = mpu6050.getGyroAngleZ();
                //
                avg_init = avg_accum;
                avg_accum = 0;

            }
            else
            {
                currentMillis = millis();
                if (currentMillis - previousMillis > 500) // sample per half a second
                { // reach here per half second


                    //sample data and take average
                    gyroData = Gyro_ReadData();
                    //acclData = ACCL_ReadData();

                    sample_count += 1;

                    avg_gyroData.roll = (avg_gyroData.roll+gyroData.roll)/sample_count;
                    avg_gyroData.pitch = (avg_gyroData.pitch+gyroData.pitch)/sample_count;
                    avg_gyroData.yaw = (avg_gyroData.yaw+gyroData.yaw)/sample_count;

                    // Serial.print("--");
                    // Serial.print(sample_count);
                    // Serial.print("---gyroData roll: ----");
                    // Serial.print(gyroData.roll);
                    // Serial.print("---gyroData roll avg: ----");
                    //Serial.println(avg_gyroData.roll);
                    // avg_acclData.X = (avg_acclData.X+acclData.X)/sample_count;
                    // avg_acclData.Y = (avg_acclData.Y+acclData.Y)/sample_count;

                    previousMillis = currentMillis; //when the time has passed, reset initial time

                    // movement_factor = Movement_Analyzer();// temp,needs to add heartrate sensor
                    // Serial.print("---movement indicator: ----");
                    // Serial.println(movement_factor);
                    return mode; // don't have to return anything

                }
                else
                {   //do nothing

                }
            }
        }
    }
}
int DEMO_Movement_Analyzer(void) //output how much the average varies from the initial value
{

    //float avg_init = initGX+initGY+initGZ;
    avg_accum = abs(avg_gyroData.roll) + abs(avg_gyroData.pitch) + abs(avg_gyroData.yaw);
    float diff = abs(avg_init - avg_accum);


    if (diff < 2) // relatively stable
    {
        return 0;
    }
    else{
        return 1;
    }
}

/*
int SleepQuality_Analyzer(float sensitivity)
{
    sensitivity_factor = sensitivity;

    if (mode == MODE_ACTIVE)
    {
        if (interval_count == TOTAL_SAMPLE_NUM)
        {
            interval_count = 0; //a day has passed, reset
            minute_count = 0;
            sample_count = 0;//reset sample count
            error_range_factor = ALLOWED_ERR_MIN;
            //TODO add timestamp to prep for DB
        }
        else
        {
            if (minute_count == MODE_DURATION)
            {
                minute_count = 0;
                interval_count +=1;
                error_range_factor = ALLOWED_ERR_HOUR;
                // if avg within range active sleep mode, start fresh cycle.
                // calculate current average, record;
            }
            else
            {
                if (sample_count == SAMPLE_NUM)
                {
                    sample_count = 0;//reset sample count
                    minute_count += 1; //increment data record by 1 minute
                    error_range_factor = ALLOWED_ERR_MIN;

                    //calculate average or other sleep quality algo
                }
                else
                {
                    currentMillis = millis();
                    if (currentMillis - previousMillis > interval)
                    {
                        previousMillis = currentMillis; //when the time has passed, reset initial time

                        //sample data and take average
                        gyroData = Gyro_ReadData();
                        acclData = ACCL_ReadData();

                        sample_count += 1;
                        avg_gyroData.roll = (avg_gyroData.roll+gyroData.roll)/sample_count;
                        avg_gyroData.pitch = (avg_gyroData.pitch+gyroData.pitch)/sample_count;
                        avg_gyroData.yaw = (avg_gyroData.yaw+gyroData.yaw)/sample_count;

                        avg_acclData.X = (avg_acclData.X+acclData.X)/sample_count;
                        avg_acclData.Y = (avg_acclData.Y+acclData.Y)/sample_count;



                    }
                    else
                    {   //do nothing

                    }
                        //digitalWrite(ledPin, ledState);
                }
            }

        }

    }
    sleep_status = Movement_Analyzer();// temp,needs to add heartrate sensor
    return sleep_status;
}
*/
int Movement_Analyzer() //0 for poor sleep, 1 for okay sleep, 2 for deep sleep
{
    int score_rp = 0; // weight, 1
    int score_yaw = 0; // weight, 3
    int score_acc = 0; // weight, 2

    //----------calibrations----------//
    //roll & pitch
    int rest_limit_RnP = 1 + error_range_factor * sensitivity_factor;
    int active_limit_RnP = 11 + error_range_factor * sensitivity_factor;

    //yaw (get up)
    int rest_limit_YAW = 1 + error_range_factor * sensitivity_factor;
    int active_limit_YAW = 25 + error_range_factor * sensitivity_factor;

    //accelerometer
    int rest_limit_ACC = 10 + error_range_factor * sensitivity_factor;
    int active_limit_ACC = 10 + error_range_factor * sensitivity_factor;

    //----------scorings----------//
    //roll
    if (avg_gyroData.roll < rest_limit_RnP){
        score_rp += 0;
    }
    else if(avg_gyroData.roll > active_limit_RnP){
        score_rp += 2;
    }
    else{
        score_rp += 1;
    }

    //pitch
    if (avg_gyroData.pitch < rest_limit_RnP){
        score_rp += 0;
    }
    else if(avg_gyroData.pitch > active_limit_RnP){
        score_rp += 2;
    }
    else{
        score_rp += 1;
    }

    //yaw
    if (avg_gyroData.yaw < rest_limit_YAW){
        score_yaw += 0;
    }
    else if(avg_gyroData.yaw > rest_limit_YAW){
        score_yaw += 2;
    }
    else{
        score_yaw += 1;
    }

    // accelerator X
    if (avg_acclData.X < rest_limit_ACC){
        score_acc += 0;
    }
    else if(avg_acclData.X > rest_limit_ACC){
        score_acc += 2;
    }
    else{
        score_acc += 1;
    }

    // accelerator Y
    if (avg_acclData.Y < rest_limit_ACC){
        score_acc += 0;
    }
    else if(avg_acclData.Y > rest_limit_ACC){
        score_acc += 2;
    }
    else{
        score_acc += 1;
    }

    //----------addWeight----------//
    int finalScore = (score_rp * 1 + score_acc * 1 + score_yaw * 3)/ 8;
    return finalScore;
}
// helper functions ////////////////////////////////////////////////////////////

GYRO_t Gyro_ReadData(void){
    // previousTime = currentTime;        // Previous time is stored before the actual time read
    // currentTime = millis();            // Current time actual time read
    // elapsedTime = (currentTime - previousTime) / 1000; // Divide by 1000 to get seconds
    //
    // i2c_port->beginTransmission(MPU);
    // i2c_port->write(0x43); // Gyro data first register address 0x43
    // i2c_port->endTransmission(false);
    // i2c_port->requestFrom(MPU, 6, true); // Read 4 registers total, each axis value is stored in 2 registers
    //
    // GyroX = (i2c_port->read() << 8 | i2c_port->read()) / 131.0; // For a 250deg/s range we have to divide first the raw value by 131.0
    // GyroY = (i2c_port->read() << 8 | i2c_port->read()) / 131.0;
    // GyroZ = (i2c_port->read() << 8 | i2c_port->read()) / 131.0;
    //
    // // Correct the outputs with the calculated error values
    // GyroX = GyroX + 0.56; // GyroErrorX ~(-0.56)
    // GyroY = GyroY - 2; // GyroErrorY ~(2)
    // GyroZ = GyroZ + 0.79; // GyroErrorZ ~ (-0.8)
    //
    // // Currently the raw values are in degrees per seconds, deg/s, so we need to multiply by sendonds (s) to get the angle in degrees
    // gyroAngleX = GyroX * elapsedTime; // deg/s * s = deg
    // gyroAngleY = GyroY * elapsedTime;
    // gyroData.yaw = abs(GyroZ * elapsedTime);
    //
    // // Complementary filter - combine acceleromter and gyro angle values
    // gyroData.roll = abs(0.96 * gyroAngleX + 0.04 * accAngleX);
    // gyroData.pitch = abs(0.96 * gyroAngleY + 0.04 * accAngleY);

    gyroData.roll = mpu6050.getGyroAngleX();
    gyroData.pitch = mpu6050.getGyroAngleY();
    gyroData.yaw = mpu6050.getGyroAngleZ();

    return gyroData;
}

ACCL_t ACCL_ReadData(void){
    i2c_port->beginTransmission(MPU);
    i2c_port->write(0x3B); // Start with register 0x3B (ACCEL_XOUT_H)
    i2c_port->endTransmission(false);
    i2c_port->requestFrom(MPU, 6, true); // Read 6 registers total, each axis value is stored in 2 registers

    //For a range of +-2g, need to divide the raw values by 16384
    AccX = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0; // X-axis value
    AccY = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0; // Y-axis value
    AccZ = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0; // Z-axis value

    // Calculating Roll and Pitch from the accelerometer data
    acclData.X = (atan(AccY / sqrt(pow(AccX, 2) + pow(AccZ, 2))) * 180 / 3.1416) - 0.58; // AccErrorX ~(0.58) See the calculate_IMU_error()
    acclData.Y = (atan(-1 * AccX / sqrt(pow(AccY, 2) + pow(AccZ, 2))) * 180 / 3.1416) + 1.58; // AccErrorY ~(-1.58)

    return acclData;
}

void data_monitor(int sensor){
    if(sensor == 0){// monitor gyroscope sensor
        previousTime = currentTime;        // Previous time is stored before the actual time read
        currentTime = millis();            // Current time actual time read
        elapsedTime = (currentTime - previousTime) / 1000; // Divide by 1000 to get seconds

        i2c_port->beginTransmission(MPU);
        i2c_port->write(0x43); // Gyro data first register address 0x43
        i2c_port->endTransmission(false);
        i2c_port->requestFrom(MPU, 6, true); // Read 4 registers total, each axis value is stored in 2 registers

        GyroX = (i2c_port->read() << 8 | i2c_port->read()) / 131.0; // For a 250deg/s range we have to divide first the raw value by 131.0
        GyroY = (i2c_port->read() << 8 | i2c_port->read()) / 131.0;
        GyroZ = (i2c_port->read() << 8 | i2c_port->read()) / 131.0;
        // Correct the outputs with the calculated error values
        GyroX = GyroX + 0.56; // GyroErrorX ~(-0.56)
        GyroY = GyroY - 2; // GyroErrorY ~(2)
        GyroZ = GyroZ + 0.79; // GyroErrorZ ~ (-0.8)
        // Currently the raw values are in degrees per seconds, deg/s, so we need to multiply by sendonds (s) to get the angle in degrees
        gyroAngleX = GyroX * elapsedTime; // deg/s * s = deg
        gyroAngleY = GyroY * elapsedTime;
        yaw =  abs(GyroZ * elapsedTime);

        /*
        int scaleFactor = elapsedTime;
        gyroAngleX = gyroAngleX + GyroX * scaleFactor;
        gyroAngleY = gyroAngleY + GyroY * scaleFactor;
        yaw =  yaw + GyroZ * scaleFactor;
        */
        // Complementary filter - combine acceleromter and gyro angle values
        roll = abs(0.96 * gyroAngleX + 0.04 * accAngleX);
        pitch = abs(0.96 * gyroAngleY + 0.04 * accAngleY);

        // Print the values on the serial monitor
        Serial.print(roll);
        Serial.print("/: ");
        Serial.print(pitch);
        Serial.print("/: ");
        Serial.println(yaw);

    }
    else{ // monitor accelerometer sensor
        i2c_port->beginTransmission(MPU);
        i2c_port->write(0x3B); // Start with register 0x3B (ACCEL_XOUT_H)
        i2c_port->endTransmission(false);
        i2c_port->requestFrom(MPU, 6, true); // Read 6 registers total, each axis value is stored in 2 registers

        //For a range of +-2g, need to divide the raw values by 16384
        AccX = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0; // X-axis value
        AccY = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0; // Y-axis value
        AccZ = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0; // Z-axis value

        // Calculating Roll and Pitch from the accelerometer data
        accAngleX = (atan(AccY / sqrt(pow(AccX, 2) + pow(AccZ, 2))) * 180 / 3.1416) - 0.58; // AccErrorX ~(0.58) See the calculate_IMU_error()
        accAngleY = (atan(-1 * AccX / sqrt(pow(AccY, 2) + pow(AccZ, 2))) * 180 / 3.1416) + 1.58; // AccErrorY ~(-1.58)

        Serial.print("/: ");
        Serial.print(accAngleX);
        Serial.print("/: ");
        Serial.println(accAngleY);

    }
}

// uint8_t HeartRate_Analyzer() {
//   const byte WINDOW_SIZE = 3;
//   byte samples[WINDOW_SIZE];
//   byte sample_index = 0;
//   unsigned long last_beat = 0; //Time at which the last beat occurred
//
//   byte bpm;
//   byte avg_bpm = 0;
//
//   while(sample_index < WINDOW_SIZE) {
//     long irValue = sensor.getIR();
//
//     if(checkForBeat(irValue)) {
//       if(last_beat > 0) {
//         bpm = 60 / ((millis() - last_beat) / 1000.0);
//
//         if(bpm < 255 && bpm > 20) {
//           samples[sample_index++] = (byte)bpm;
//           avg_bpm += bpm / WINDOW_SIZE;
//         }
//       }
//
//       last_beat = millis();
//     }
//   }
//
//   return avg_bpm;
// }

void calculate_IMU_error() {
  // We can call this funtion in the setup section to calculate the accelerometer and gyro data error. From here we will get the error values used in the above equations printed on the Serial Monitor.
  // Note that we should place the IMU flat in order to get the proper values, so that we then can the correct values
  // Read accelerometer values 200 times
  while (c < 200) {
    i2c_port->beginTransmission(MPU);
    i2c_port->write(0x3B);
    i2c_port->endTransmission(false);
    i2c_port->requestFrom(MPU, 6, true);
    AccX = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0 ;
    AccY = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0 ;
    AccZ = (i2c_port->read() << 8 | i2c_port->read()) / 16384.0 ;
    // Sum all readings
    AccErrorX = AccErrorX + ((atan((AccY) / sqrt(pow((AccX), 2) + pow((AccZ), 2))) * 180 / 3.1416));
    AccErrorY = AccErrorY + ((atan(-1 * (AccX) / sqrt(pow((AccY), 2) + pow((AccZ), 2))) * 180 / 3.1416));
    c++;
  }
  //Divide the sum by 200 to get the error value
  AccErrorX = AccErrorX / 200;
  AccErrorY = AccErrorY / 200;
  c = 0;
  // Read gyro values 200 times
  while (c < 200) {
    i2c_port->beginTransmission(MPU);
    i2c_port->write(0x43);
    i2c_port->endTransmission(false);
    i2c_port->requestFrom(MPU, 6, true);
    GyroX = i2c_port->read() << 8 | i2c_port->read();
    GyroY = i2c_port->read() << 8 | i2c_port->read();
    GyroZ = i2c_port->read() << 8 | i2c_port->read();
    // Sum all readings
    GyroErrorX = GyroErrorX + (GyroX / 131.0);
    GyroErrorY = GyroErrorY + (GyroY / 131.0);
    GyroErrorZ = GyroErrorZ + (GyroZ / 131.0);
    c++;
  }
  //Divide the sum by 200 to get the error value
  GyroErrorX = GyroErrorX / 200;
  GyroErrorY = GyroErrorY / 200;
  GyroErrorZ = GyroErrorZ / 200;
  // Print the error values on the Serial Monitor
  Serial.print("AccErrorX: ");
  Serial.println(AccErrorX);
  Serial.print("AccErrorY: ");
  Serial.println(AccErrorY);
  Serial.print("GyroErrorX: ");
  Serial.println(GyroErrorX);
  Serial.print("GyroErrorY: ");
  Serial.println(GyroErrorY);
  Serial.print("GyroErrorZ: ");
  Serial.println(GyroErrorZ);
}

//https://howtomechatronics.com/tutorials/arduino/arduino-and-mpu6050-accelerometer-and-gyroscope-tutorial/
