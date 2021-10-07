package com.example.medistation_2.helperFunctions;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medistation_2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dbHelper extends AppCompatActivity {

    private static final String TAG = dbHelper.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void readSimpleData(String keyValue) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Patient").child(keyValue).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String simpleDataValue = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, String.valueOf(simpleDataValue));
                    EditText nameOfUser =(EditText)findViewById(R.id.profileNameInput);
                    nameOfUser.setText(simpleDataValue);

                }
            }
        });
    }

    public void AddSimpleData (String path, String data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(path);
        dbRef.setValue(data);
    }
}
