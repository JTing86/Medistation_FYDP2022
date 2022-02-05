package com.example.medistation_2.helperFunctions;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.medistation_2.ui.analysis.AnalysisFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.O)
public class createData {
    private static ArrayList <Long> timeData;
    private static final String TAG = createData.class.getSimpleName();
    public static void timeData(int numOfDays){
        timeData = new ArrayList<>();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(currentTime) + " 00:00:00.000";
        long currentDateInEpochTime = dbHelper.toEpochTime(formattedDate)/1000;
        for (int i = numOfDays; i > 0;i--){
            timeData.add(currentDateInEpochTime - 86400L*i);
        }
    }
    public static void temperatureData (int numberOfDays){
        timeData(numberOfDays);
        ArrayList <Long> temperatureValue = new ArrayList<>();
        for (int i= 0; i < numberOfDays;i++){
            Random random = new Random();
            long randomNumber = random.nextInt(4);
            temperatureValue.add(randomNumber+37);
        }
        dbHelper.addToDBLongArray("temp/date",timeData);
        dbHelper.addToDBLongArray("temp/value",temperatureValue);
    }
    public static void heartRate (int numberOfDays) {
        timeData(numberOfDays);
        ArrayList <Long> heartRateValue = new ArrayList<>();
        HashMap <String, Object> singleDayHeartRate = new HashMap<>();
        for (int i = 0;i < numberOfDays;i++){
            for (int j= 0;j<24;j++){
                Random random = new Random();
                long randomNumber = random.nextInt(40);
                heartRateValue.add(randomNumber+60);
            }
            singleDayHeartRate.put("date",timeData.get(i));
            singleDayHeartRate.put("value",heartRateValue);
            dbHelper.addToDB("heartRate/"+ i,singleDayHeartRate);
        }
    }
    public static void sleepQuality (int numberOfDays) {
        timeData(numberOfDays);
        Random random = new Random();

        int numberOfSleep =
    }
}
