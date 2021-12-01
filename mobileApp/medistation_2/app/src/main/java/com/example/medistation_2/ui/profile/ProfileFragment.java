package com.example.medistation_2.ui.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProfileViewModel mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        //Button section
        Button profileUserInfoSaveButton = view.findViewById(R.id.profileSaveButton);
        profileUserInfoSaveButton.setOnClickListener(v -> {
            dbHelper dbHelperCall = new dbHelper();
            dbHelperCall.AddSimpleStringData("/Patient/name",((EditText) view.findViewById(R.id.profileNameInput)).getText().toString());
            dbHelperCall.AddSimpleStringData("/Patient/email/",((EditText) view.findViewById(R.id.profileEmailInput)).getText().toString());
            dbHelperCall.AddSimpleStringData("/Patient/emergencyName", ((EditText) view.findViewById(R.id.profileEmergencyNameInput)).getText().toString());
            dbHelperCall.AddSimpleStringData("/Patient/emergencyNumber",((EditText) view.findViewById(R.id.profileEmergencyNumberInput)).getText().toString());
        });
        //Save past medication record to database
        Button profilePastMedicationSaveButton = view.findViewById(R.id.profilePastMedicationButton);
        profilePastMedicationSaveButton.setOnClickListener(v -> {
            dbHelper dbHelperCall = new dbHelper();
            String [] rowNumberDB = {"1","2","3","4","5","6","7","8"};
            String [] rowNumber = { "Two","Three","Four","Five","Six","Seven","Eight","Nine"};
            for (int i=0;i<=7;i++){
                int medNameRID = view.getResources().getIdentifier("profileRow"+rowNumber[i]+"MedInput","id", requireActivity().getPackageName());
                int medDosageRID = view.getResources().getIdentifier("profileRow"+rowNumber[i]+"DosageInput","id", requireActivity().getPackageName());
                int medDurationRID = view.getResources().getIdentifier("profileRow"+rowNumber[i]+"DurationInput","id", requireActivity().getPackageName());
                dbHelperCall.AddSimpleStringData("/Patient/pastMedications/row"+rowNumberDB[i]+"/medName",((EditText) view.findViewById(medNameRID)).getText().toString());
                dbHelperCall.AddSimpleStringData("/Patient/pastMedications/row"+rowNumberDB[i]+"/medDosage",((EditText) view.findViewById(medDosageRID)).getText().toString());
                dbHelperCall.AddSimpleStringData("/Patient/pastMedications/row"+rowNumberDB[i]+"/medDuration",((EditText) view.findViewById(medDurationRID)).getText().toString());
            }
        });

        //save symptom to database
        Button profileSymptomSaveButton = view.findViewById(R.id.profileSymptomsButton);
        profileSymptomSaveButton.setOnClickListener(v -> {
            dbHelper dbHelperCall = new dbHelper();
            String symptomName = ((EditText) view.findViewById(R.id.profileSymptomsNameInput)).getText().toString().toLowerCase();
            String symptomHour = ((Spinner) requireActivity().findViewById(R.id.profileHourDropList)).getSelectedItem().toString().toLowerCase();
            String symptomMinute = ((Spinner) requireActivity().findViewById(R.id.profileMinuteDropList)).getSelectedItem().toString().toLowerCase();
            String symptomSeverity =((Spinner) requireActivity().findViewById(R.id.profileSeverityDropDownList)).getSelectedItem().toString().toLowerCase();
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy,MM,dd", Locale.getDefault());
            String formattedDate = df.format(currentTime);
           dbHelperCall.AddSimpleStringData("/Patient/symptom/"+symptomName+"/"+formattedDate+","+symptomHour+","+symptomMinute,symptomSeverity);
        });

        populateUserData(view);
        setupDropDownMenu(view);
    }

    public void setupDropDownMenu (View view) {
        //set up drop down list
        Spinner severityDropDownList = view.findViewById(R.id.profileSeverityDropDownList);
        Spinner hourDropDownList = view.findViewById(R.id.profileHourDropList);
        Spinner minuteDropDownList = view.findViewById(R.id.profileMinuteDropList);
        String[] severity = new String[]{"Severity","Mild","Moderate","Severe","Severe"};
        String[] hour = new String[]{"Hour","00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String[] minute = new String[]{
                "Min",
                "00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
                "24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","45","46","47","48",
                "49","50","51","52","53","54","55","56","57","58","59"};
        List<String> severityList = new ArrayList<>(Arrays.asList(severity));
        List<String> hourList = new ArrayList<>(Arrays.asList(hour));
        List<String> minuteList = new ArrayList<>(Arrays.asList(minute));
        //Create severity drop down menu
        ArrayAdapter<String> severityMenuArrayAdapter = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, severityList) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0; }
            @Override
            public View getDropDownView(int position,View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        //Create hour drop down menu
        ArrayAdapter<String> hourMenuArrayAdapter = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, hourList) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0; }
            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        ArrayAdapter<String> minuteMenuArrayAdapter = new ArrayAdapter<String> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, minuteList) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0; }
            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        severityMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        severityDropDownList.setAdapter(severityMenuArrayAdapter);
        hourMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        hourDropDownList.setAdapter(hourMenuArrayAdapter);
        minuteMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        minuteDropDownList.setAdapter(minuteMenuArrayAdapter);
    }

    public void populateUserData (@NonNull View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();
        //display current name of user stored in the database
        rootDbRef.child("Patient").child("name").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
            }
            else {
                String simpleDataValue = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
                Log.d(TAG, simpleDataValue);
                EditText nameOfUser = view.findViewById(R.id.profileNameInput);
                nameOfUser.setText(simpleDataValue);
            }
        });
        //display current phone number of user stored in the database
        rootDbRef.child("Patient").child("email").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
            }
            else {
                String simpleDataValue = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
                Log.d(TAG, simpleDataValue);
                EditText emailOfUser = view.findViewById(R.id.profileEmailInput);
                emailOfUser.setText(simpleDataValue);
            }
        });
        //display current emergency name stored in the database
        rootDbRef.child("Patient").child("emergencyName").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
            }
            else {
                String simpleDataValue = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
                EditText emergencyName = view.findViewById(R.id.profileEmergencyNameInput);
                emergencyName.setText(simpleDataValue);
            }
        });
        //display current emergency phone stored in the database
        rootDbRef.child("Patient").child("emergencyNumber").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
            }
            else {
                String simpleDataValue = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
                EditText emergencyNumber = view.findViewById(R.id.profileEmergencyNumberInput);
                emergencyNumber.setText(simpleDataValue);
            }
        });
        //past medication display section
        //row2
        rootDbRef.child("Patient/pastMedications/row2/medName").get().addOnCompleteListener(task -> {
            EditText row2MedicationName = view.findViewById(R.id.profileRowTwoMedInput);
            row2MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row2/medDosage").get().addOnCompleteListener(task -> {
            EditText row2Dosage = view.findViewById(R.id.profileRowTwoDosageInput);
            row2Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row2/medDuration").get().addOnCompleteListener(task -> {
            EditText row2Duration = view.findViewById(R.id.profileRowTwoDurationInput);
            row2Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //row3
        rootDbRef.child("Patient/pastMedications/row3/medName").get().addOnCompleteListener(task -> {
            EditText row3MedicationName = view.findViewById(R.id.profileRowThreeMedInput);
            row3MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));

        });
        rootDbRef.child("Patient/pastMedications/row3/medDosage").get().addOnCompleteListener(task -> {
            EditText row3Dosage = view.findViewById(R.id.profileRowThreeDosageInput);
            row3Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row3/medDuration").get().addOnCompleteListener(task -> {
            EditText row3Duration = view.findViewById(R.id.profileRowThreeDurationInput);
            row3Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //row4
        rootDbRef.child("Patient/pastMedications/row4/medName").get().addOnCompleteListener(task -> {
            EditText row4MedicationName = view.findViewById(R.id.profileRowFourMedInput);
            row4MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row4/medDosage").get().addOnCompleteListener(task -> {
            EditText row4Dosage =  view.findViewById(R.id.profileRowFourDosageInput);
            row4Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row4/medDuration").get().addOnCompleteListener(task -> {
            EditText row4Duration = view.findViewById(R.id.profileRowFourDurationInput);
            row4Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //row5
        rootDbRef.child("Patient/pastMedications/row5/medName").get().addOnCompleteListener(task -> {
            EditText row4MedicationName =  view.findViewById(R.id.profileRowFiveMedInput);
            row4MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row5/medDosage").get().addOnCompleteListener(task -> {
            EditText row5Dosage = view.findViewById(R.id.profileRowFiveDosageInput);
            row5Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row5/medDuration").get().addOnCompleteListener(task -> {
            EditText row5Duration = view.findViewById(R.id.profileRowFiveDurationInput);
            row5Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //row6
        rootDbRef.child("Patient/pastMedications/row6/medName").get().addOnCompleteListener(task -> {
            EditText row6MedicationName = view.findViewById(R.id.profileRowSixMedInput);
            row6MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row6/medDosage").get().addOnCompleteListener(task -> {
            EditText row6Dosage = view.findViewById(R.id.profileRowSixDosageInput);
            row6Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row6/medDuration").get().addOnCompleteListener(task -> {
            EditText row6Duration = view.findViewById(R.id.profileRowSixDurationInput);
            row6Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //row7
        rootDbRef.child("Patient/pastMedications/row7/medName").get().addOnCompleteListener(task -> {
            EditText row7MedicationName = view.findViewById(R.id.profileRowSevenMedInput);
            row7MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row7/medDosage").get().addOnCompleteListener(task -> {
            EditText row7Dosage = view.findViewById(R.id.profileRowSevenDosageInput);
            row7Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row7/medDuration").get().addOnCompleteListener(task -> {
            EditText row7Duration = view.findViewById(R.id.profileRowSevenDurationInput);
            row7Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //row8
        rootDbRef.child("Patient/pastMedications/row8/medName").get().addOnCompleteListener(task -> {
            EditText row8MedicationName = view.findViewById(R.id.profileRowEightMedInput);
            row8MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row8/medDosage").get().addOnCompleteListener(task -> {
            EditText row8Dosage = view.findViewById(R.id.profileRowEightDosageInput);
            row8Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row8/medDuration").get().addOnCompleteListener(task -> {
            EditText row8Duration = view.findViewById(R.id.profileRowEightDurationInput);
            row8Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        //row9
        rootDbRef.child("Patient/pastMedications/row9/medName").get().addOnCompleteListener(task -> {
            EditText row9MedicationName = view.findViewById(R.id.profileRowNineMedInput);
            row9MedicationName.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row9/medDosage").get().addOnCompleteListener(task -> {
            EditText row9Dosage = view.findViewById(R.id.profileRowNineDosageInput);
            row9Dosage.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
        rootDbRef.child("Patient/pastMedications/row9/medDuration").get().addOnCompleteListener(task -> {
            EditText row9Duration = view.findViewById(R.id.profileRowNineDurationInput);
            row9Duration.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
        });
    }

}