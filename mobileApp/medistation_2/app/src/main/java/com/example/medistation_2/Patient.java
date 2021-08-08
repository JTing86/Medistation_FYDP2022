package com.example.medistation_2;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Patient {
    //private HashMap <String, ArrayList<String>> symptoms = new HashMap<>();
    private String firstName;
    private String lastName;
    public Map<String, Object> symptoms = new HashMap<>();
    private static final String TAG = Patient.class.getSimpleName();

    private Patient () {

    }
    public Patient(String first, String last,Map symptoms) {
        this.symptoms = symptoms;
        this.firstName = first;
        this.lastName = last;
    }

    public String getFirstName () {
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public void setFirstName (String first){
        this.firstName = first;
    }
    public void setLastName (String last) {
        this.lastName = last;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
       result.put("firstName",firstName);
        result.put("lastName",lastName);
        result.put("symptoms",symptoms);
        return result;
    }
    /*public void add (String symptom, String time) {
        String TAG = User.class.getName();
        if (this.symptoms.containsKey(symptom)) {
            Log.d(TAG,"Already has symptom");
            ArrayList <String> temp = this.symptoms.get (symptom);
            temp.add (time);
            this.symptoms.put(symptom,temp);
        }
        else{
            ArrayList <String> temp = new ArrayList<>();
            temp.add(time);
            this.symptoms.put(symptom,temp);
        }
    }
    public HashMap getSymptoms () {
        return this.symptoms;
    }
    public void ConsoleLog () {
        String TAG = User.class.getName();
        for (ArrayList values : this.symptoms.values())
        {
            for (Object keysList: values) {
                Log.d(TAG,keysList.toString());
            }
        }
        for (String values : this.symptoms.keySet())
        {
                Log.d(TAG,values);
        }

    }*/

}
