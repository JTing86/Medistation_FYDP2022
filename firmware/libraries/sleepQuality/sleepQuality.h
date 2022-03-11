/*
    sleepQuality.h - Library for determinging sleep quality for the wristband
*/

#ifndef _SLEEPQUALITY_H_
#define _SLEEPQUALITY_H_

// #include <Arduino.h>
#include <math.h>
#include <Wire.h> // communicate with I2C devices.
#include "MAX30105.h"
#include "heartRate.h"

typedef struct{
    int roll;
    int pitch;
    int yaw;
}GYRO_t;

typedef struct{
    int X;
    int Y;
}ACCL_t;

/*
static float AccX, AccY, AccZ;
static float GyroX, GyroY, GyroZ;
static float accAngleX, accAngleY, gyroAngleX, gyroAngleY, gyroAngleZ;
static float roll, pitch, yaw;
static float AccErrorX, AccErrorY, GyroErrorX, GyroErrorY, GyroErrorZ;
static float elapsedTime, currentTime, previousTime;

static ACCL_t acclData;
static GYRO_t gyroData;
*/
void Sensor_Init(TwoWire &wire);
void SleepQuality_Init(TwoWire &port);

int Demo_SleepQuality_Analyzer(float sensitivity);
int SleepQuality_Analyzer(float sensitivity); //0 for poor sleep, 1 for okay sleep, 2 for deep sleep
int DEMO_Movement_Analyzer(void);
int Movement_Analyzer(void);
uint8_t HeartRate_Analyzer(void);

void Sensor_printRPY(void);
void Sensor_Update(void);
void Sensor_begin(void);
void Sensor_calcGyroOffsets(void);

GYRO_t Gyro_ReadData(void);
ACCL_t ACCL_ReadData(void);


void calculate_IMU_error(void);
void data_monitor(int sensor); // 1 for acc, 0 for gyro



#endif //_SLEEPQUALITY_H_

/*
    TODOs
    - add timestamp to sleep quality data
    - add descriptions to each function
*/
