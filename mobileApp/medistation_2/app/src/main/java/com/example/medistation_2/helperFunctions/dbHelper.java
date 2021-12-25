package com.example.medistation_2.helperFunctions;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class dbHelper extends AppCompatActivity {

    private static final String TAG = dbHelper.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void addToDB(String path, String data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }

    public static void addToDB(String path, int data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }

    public static void addToDB(String path, boolean data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }

    public static void addToDBStrArray(String path, ArrayList<String> data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }
    public static void addToDBIntegerArray(String path, ArrayList<Integer> data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }

    public static void addToDB(String path, Map<String, Object> data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }



    public static void deleteNode(String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.removeValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long toEpochTime(String time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        LocalDateTime ldt = LocalDateTime.parse(time, dtf);
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of("Canada/Eastern");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);
        return ldt.toInstant(zoneOffSet).toEpochMilli();
    }

    public static String fromEpochTime(long epochTime) {
        Date date = new Date(epochTime);
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone("Canada/Eastern"));
        Log.d(TAG, format.format(date));
        return format.format(date);
    }

}
