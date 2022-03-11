package com.example.medistation_2.helperFunctions;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.O)
public class createData {
    private static ArrayList<Long> timeData;
    private static final String TAG = createData.class.getSimpleName();

    public static void timeData(int numOfDays) {
        timeData = new ArrayList<>();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(currentTime) + " 00:00:00.000";
        long currentDateInEpochTime = dbHelper.toEpochTime(formattedDate) / 1000;
        for (int i = numOfDays; i > 0; i--) {
            timeData.add(currentDateInEpochTime - 86400L * i);
        }
    }

    public static void temperatureData(int numberOfDays) {
        timeData(numberOfDays);
        ArrayList<Long> temperatureValue = new ArrayList<>();
        for (int i = 0; i < numberOfDays; i++) {
            Random random = new Random();
            long randomNumber = random.nextInt(4);
            temperatureValue.add(randomNumber + 37);
        }
        dbHelper.addToDBLongArray("temp/date", timeData);
        dbHelper.addToDBLongArray("temp/value", temperatureValue);
    }

    public static void heartRate(int numberOfDays) {
        timeData(numberOfDays);
        ArrayList<Long> heartRateValue = new ArrayList<>();
        HashMap<String, Object> singleDayHeartRate = new HashMap<>();
        for (int i = 0; i < numberOfDays; i++) {
            for (int j = 0; j < 24; j++) {
                Random random = new Random();
                long randomNumber = random.nextInt(40);
                heartRateValue.add(randomNumber + 60);
            }
            singleDayHeartRate.put("date", timeData.get(i));
            singleDayHeartRate.put("value", heartRateValue);
            dbHelper.addToDB("heartRate/" + i, singleDayHeartRate);
            heartRateValue.clear();
        }
    }

    public static void sleepQuality(int numberOfDays) {
        timeData(numberOfDays);
        Random random = new Random();
        HashMap<String, Object> singleDaySleepQuality = new HashMap<>();
        for (int j = 0; j < numberOfDays; j++) {
            long startOfDay = timeData.get(j) + random.nextInt(4 * 3600);
            long numberOfSleep = random.nextInt(3) + 1;
            ArrayList<Long> startTime = new ArrayList<>();
            ArrayList<Long> endTime = new ArrayList<>();
            ArrayList<Long> sleepQualityList = new ArrayList<>();
            for (int i = 0; i < numberOfSleep; i++) {
                long sleepQuality = random.nextInt(3);
                long startOfSleep = random.nextInt(86400) + startOfDay;
                long sleepDuration = random.nextInt(8 * 3600);
                startOfDay = startOfSleep + sleepDuration;
                startTime.add(startOfSleep);
                endTime.add(sleepDuration + startOfSleep);
                sleepQualityList.add(sleepQuality);
            }
            singleDaySleepQuality.put("start", startTime);
            singleDaySleepQuality.put("end", endTime);
            singleDaySleepQuality.put("quality", sleepQualityList);
            dbHelper.addToDB("sleep/" + j, singleDaySleepQuality);
        }

    }

    public static void symptomData(String symptomName,int numberOfDays) {
        Random random = new Random();
        HashMap<String, Object> symptom  = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (int j = 0; j < numberOfDays; j++) {
            int numberOfOccurrence = random.nextInt(20);
            ArrayList <Long> symptomList = new ArrayList<>();
            for (int i = 0; i < numberOfOccurrence; i++){
                long symptomLevel = random.nextInt(3)+1;
                symptomList.add(symptomLevel);
            }
            symptom.put(today.toString(),symptomList);
            dbHelper.addToDB("symptom/"+symptomName,symptom);
            today = today.minusDays(1);
        }

    }
}
