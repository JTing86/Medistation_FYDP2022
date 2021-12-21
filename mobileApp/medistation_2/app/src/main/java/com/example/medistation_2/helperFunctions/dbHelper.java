package com.example.medistation_2.helperFunctions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class dbHelper extends AppCompatActivity {

    private static final String TAG = dbHelper.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addSimpleData(String path, String data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }
    public void addSimpleData(String path, int data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }
    public void addSimpleData(String path, boolean data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }
    public void deleteNode (String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.removeValue();
    }
    public void addJSONData (String path, JSONObject data){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        Map<String, String> dbData = new HashMap<>();
        dbData.put("1","1");
        dbRef.setValue(dbData);
    }
}
