package com.example.medistation_2.helperFunctions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dbHelper extends AppCompatActivity {

    private static final String TAG = dbHelper.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void AddSimpleStringData(String path, String data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }
    public void AddSimpleBooleanData (String path, boolean data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }
    public void deleteNode (String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.removeValue();
    }
}
