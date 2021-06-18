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
import com.google.firebase.database.ValueEventListener;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
                symptoms.put(symptom,formatter.format(date));
                CreateNewUser ("jon","snow",symptoms,symptom);

            }
        });
    }
    public void CreateNewUser (String firstName, String lastName,Map<String,Object> symptoms,String symptom) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        //String key = myRef.child("Patient").child("symptoms").getKey();
        Patient user = new Patient(firstName,lastName,symptoms);
        Map<String,Object> userProfile = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("Patient", userProfile);

        myRef.child("Patient").updateChildren(childUpdates);
    }



}