package com.example.medistation_2.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private ProfileViewModel mViewModel;
    private EditText nameInput ;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();

        //display current name of user stored in the database
        rootDbRef.child("Patient").child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String simpleDataValue = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, String.valueOf(simpleDataValue));
                    EditText nameOfUser = (EditText) view.findViewById(R.id.profileNameInput);
                    nameOfUser.setText(simpleDataValue);
                }
            }
        });
        //display current phone number of user stored in the database
        rootDbRef.child("Patient").child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String simpleDataValue = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, String.valueOf(simpleDataValue));
                    EditText emailOfUser = (EditText) view.findViewById(R.id.profileEmailInput);
                    emailOfUser.setText(simpleDataValue);
                }
            }
        });
        //display current emergency name stored in the database
        rootDbRef.child("Patient").child("emergencyName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String simpleDataValue = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, String.valueOf(simpleDataValue));
                    EditText emergencyName = (EditText) view.findViewById(R.id.profileEmergencyNameInput);
                    emergencyName.setText(simpleDataValue);
                }
            }
        });
        //display current emergency phone stored in the database
        rootDbRef.child("Patient").child("emergencyNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    String simpleDataValue = String.valueOf(task.getResult().getValue());
                    Log.d(TAG, String.valueOf(simpleDataValue));
                    EditText emergencyNumber = (EditText) view.findViewById(R.id.profileEmergencyNumberInput);
                    emergencyNumber.setText(simpleDataValue);
                }
            }
        });

        Button profileSaveButton = (Button) view.findViewById(R.id.profileSaveButton);
        profileSaveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Save button pressed");
                dbHelper dbHelperCall = new dbHelper();

                dbHelperCall.AddSimpleData("/Patient/name",((EditText) view.findViewById(R.id.profileNameInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/email/",((EditText) view.findViewById(R.id.profileEmailInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/emergencyName", ((EditText) view.findViewById(R.id.profileEmergencyNameInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/emergencyNumber",((EditText) view.findViewById(R.id.profileEmergencyNumberInput)).getText().toString());

            }
        });

    }

}