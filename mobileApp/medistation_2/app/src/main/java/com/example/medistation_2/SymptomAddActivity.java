package com.example.medistation_2;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SymptomAddActivity extends AppCompatActivity {
    private static final String TAG = SymptomAddActivity.class.getSimpleName();
    public Object symptomsList = new Object();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_add);
        main();

    }
    public void main() {
        Button SymptomAddButton = (Button) findViewById(R.id.SymptomAddButton);
        SymptomAddButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symptom = ((EditText) findViewById(R.id.SymptomInput)).getText().toString();
                Log.d(TAG, "onClick: ");
                Map<String,Object> symptoms = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                //CreateNewUser ("jon","snow",symptoms);
                //AddNewSymptoms(symptom,formatter.format(date));
                //returnSymptomTimes(symptom);

            }
        });
    }
    public void CreateNewUser (String firstName, String lastName,Map<String,Object> symptoms) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Patient user = new Patient(firstName,lastName,symptoms);
        Map<String,Object> userProfile = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("Patient", userProfile);

        myRef.updateChildren(childUpdates);
    }
    public void AddNewSymptoms (String symptom, String time) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("/Patient/symptoms/"+symptom);

        DatabaseReference newChildRef = dbRef.push();
        String key = newChildRef.getKey();
        dbRef.child(key+"/time").setValue(time);
    }
    public void returnSymptomTimes (String symptom) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        ValueEventListener queryValueListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("Patient/symptoms/"+symptom).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    Log.d(TAG, "Value = " + next.child("time").getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Query query = myRef.orderByKey();
        query.addListenerForSingleValueEvent(queryValueListener);
    }
}