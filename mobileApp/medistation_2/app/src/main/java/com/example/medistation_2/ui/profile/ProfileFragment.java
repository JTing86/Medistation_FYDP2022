package com.example.medistation_2.ui.profile;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        //past medication display section
        //row2
        rootDbRef.child("Patient/pastMedications/row2/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row2MedicationName = (EditText) view.findViewById(R.id.profileRowTwoMedInput);
                row2MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row2/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row2Dosage = (EditText) view.findViewById(R.id.profileRowTwoDosageInput);
                row2Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row2/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row2Duration = (EditText) view.findViewById(R.id.profileRowTwoDurationInput);
                row2Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        Log.d(TAG,"Row3");
        //row3
        rootDbRef.child("Patient/pastMedications/row3/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row3MedicationName = (EditText) view.findViewById(R.id.profileRowThreeMedInput);
                row3MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row3/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row3Dosage = (EditText) view.findViewById(R.id.profileRowThreeDosageInput);
                row3Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row3/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row3Duration = (EditText) view.findViewById(R.id.profileRowThreeDurationInput);
                row3Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        //row4
        rootDbRef.child("Patient/pastMedications/row4/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row4MedicationName = (EditText) view.findViewById(R.id.profileRowFourMedInput);
                row4MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row4/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row4Dosage = (EditText) view.findViewById(R.id.profileRowFourDosageInput);
                row4Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row4/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row4Duration = (EditText) view.findViewById(R.id.profileRowFourDurationInput);
                row4Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        Log.d(TAG,"Row5");
        //row5
        rootDbRef.child("Patient/pastMedications/row5/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row4MedicationName = (EditText) view.findViewById(R.id.profileRowFiveMedInput);
                row4MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row5/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row5Dosage = (EditText) view.findViewById(R.id.profileRowFiveDosageInput);
                row5Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row5/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row5Duration = (EditText) view.findViewById(R.id.profileRowFiveDurationInput);
                row5Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        //row6
        rootDbRef.child("Patient/pastMedications/row6/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row6MedicationName = (EditText) view.findViewById(R.id.profileRowSixMedInput);
                row6MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row6/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row6Dosage = (EditText) view.findViewById(R.id.profileRowSixDosageInput);
                row6Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row6/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row6Duration = (EditText) view.findViewById(R.id.profileRowSixDurationInput);
                row6Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        Log.d(TAG,"Row7");
        //row7
        rootDbRef.child("Patient/pastMedications/row7/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row7MedicationName = (EditText) view.findViewById(R.id.profileRowSevenMedInput);
                row7MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row7/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row7Dosage = (EditText) view.findViewById(R.id.profileRowSevenDosageInput);
                row7Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row7/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row7Duration = (EditText) view.findViewById(R.id.profileRowSevenDurationInput);
                row7Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        //row8
        rootDbRef.child("Patient/pastMedications/row8/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row8MedicationName = (EditText) view.findViewById(R.id.profileRowEightMedInput);
                row8MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row8/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row8Dosage = (EditText) view.findViewById(R.id.profileRowEightDosageInput);
                row8Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row8/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row8Duration = (EditText) view.findViewById(R.id.profileRowEightDurationInput);
                row8Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        Log.d(TAG,"Row9");
        //row9
        rootDbRef.child("Patient/pastMedications/row9/medName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row9MedicationName = (EditText) view.findViewById(R.id.profileRowNineMedInput);
                row9MedicationName.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row9/medDosage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row9Dosage = (EditText) view.findViewById(R.id.profileRowNineDosageInput);
                row9Dosage.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        rootDbRef.child("Patient/pastMedications/row9/medDuration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EditText row9Duration = (EditText) view.findViewById(R.id.profileRowNineDurationInput);
                row9Duration.setText(String.valueOf(task.getResult().getValue()));
            }
        });

        //Buttons
        Button profileSaveButton = (Button) view.findViewById(R.id.profileSaveButton);
        profileSaveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Save button pressed");
                dbHelper dbHelperCall = new dbHelper();

                dbHelperCall.AddSimpleData("/Patient/nfame",((EditText) view.findViewById(R.id.profileNameInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/email/",((EditText) view.findViewById(R.id.profileEmailInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/emergencyName", ((EditText) view.findViewById(R.id.profileEmergencyNameInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/emergencyNumber",((EditText) view.findViewById(R.id.profileEmergencyNumberInput)).getText().toString());

            }
        });
        //Save past medication record to database
        Button profilePastMedicationSaveButton = (Button) view.findViewById(R.id.profilePastMedicationButton);
        profilePastMedicationSaveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Save button pressed");
                dbHelper dbHelperCall = new dbHelper();
                //Row 2
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row2/medName",((EditText) view.findViewById(R.id.profileRowTwoMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row2/medDosage",((EditText) view.findViewById(R.id.profileRowTwoDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row2/medDuration",((EditText) view.findViewById(R.id.profileRowTwoDurationInput)).getText().toString());
                //Row 3
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row3/medName",((EditText) view.findViewById(R.id.profileRowThreeMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row3/medDosage",((EditText) view.findViewById(R.id.profileRowThreeDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row3/medDuration",((EditText) view.findViewById(R.id.profileRowThreeDurationInput)).getText().toString());
                //Row 4
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row4/medName",((EditText) view.findViewById(R.id.profileRowFourMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row4/medDosage",((EditText) view.findViewById(R.id.profileRowFourDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row4/medDuration",((EditText) view.findViewById(R.id.profileRowFourDurationInput)).getText().toString());
                //Row 5
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row5/medName",((EditText) view.findViewById(R.id.profileRowFiveMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row5/medDosage",((EditText) view.findViewById(R.id.profileRowFiveDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row5/medDuration",((EditText) view.findViewById(R.id.profileRowFiveDurationInput)).getText().toString());
                //Row 6
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row6/medName",((EditText) view.findViewById(R.id.profileRowSixMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row6/medDosage",((EditText) view.findViewById(R.id.profileRowSixDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row6/medDuration",((EditText) view.findViewById(R.id.profileRowSixDurationInput)).getText().toString());
                //Row 7
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row7/medName",((EditText) view.findViewById(R.id.profileRowSevenMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row7/medDosage",((EditText) view.findViewById(R.id.profileRowSevenDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row7/medDuration",((EditText) view.findViewById(R.id.profileRowSevenDurationInput)).getText().toString());
                //Row 8
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row8/medName",((EditText) view.findViewById(R.id.profileRowEightMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row8/medDosage",((EditText) view.findViewById(R.id.profileRowEightDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row8/medDuration",((EditText) view.findViewById(R.id.profileRowEightDurationInput)).getText().toString());
                // Row 9
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row9/medName",((EditText) view.findViewById(R.id.profileRowNineMedInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row9/medDosage",((EditText) view.findViewById(R.id.profileRowNineDosageInput)).getText().toString());
                dbHelperCall.AddSimpleData("/Patient/pastMedications/row9/medDuration",((EditText) view.findViewById(R.id.profileRowNineDurationInput)).getText().toString());

            }
        });

        //Hid keyboard when clicking elsewhere
    }
}