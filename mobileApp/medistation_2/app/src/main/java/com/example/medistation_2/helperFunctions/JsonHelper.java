package com.example.medistation_2.helperFunctions;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

public class JsonHelper {
    private static final String TAG = JsonHelper.class.getSimpleName() ;

    public static JSONObject Encoder (String key, String value, JSONObject item ) throws JSONException {
        item.put(key,value);
        return item;
    }
    public  static JSONObject Encoder (String key, int value,JSONObject item ) throws JSONException {
        item.put(key,value);
        return item;
    }
    public static  JSONObject Encoder (String key, boolean value ,JSONObject item ) throws JSONException {
        item.put(key,value);
        return item;
    }
    public static  JSONObject Encoder (String key, int[] value,JSONObject item ) throws JSONException {
        item.put(key,value);
        return item;
    }
    public   static JSONObject Encoder (String key, String[] value,JSONObject item ) throws JSONException {
        item.put(key,value);
        return item;
    }
    public static JSONObject Encoder (String key, double[] value,JSONObject item ) throws JSONException {
        item.put(key,value);
        return item;
    }
    public static JSONObject Encoder (String key, JSONArray value, JSONObject item) throws JSONException {
        item.put(key,value);
        return item;
    }
    public static String stringDecoder (String key, String data) throws JSONException {
        JSONObject JsonData = new JSONObject(data);
        return JsonData.getString(key);
    }
    public static int intDecoder (String key, String data) throws JSONException {
        JSONObject JsonData = new JSONObject(data);
        return JsonData.getInt(key);
    }
    public static boolean booleanDecoder (String key, JSONObject data) throws JSONException {
        return data.getBoolean(key);
    }
    public static List listDecoder (String key, String arrayName, String data) throws JSONException {
        JSONObject JsonData = new JSONObject(data);
        JSONArray dataArray = JsonData.getJSONArray(arrayName);
        List <String> returnValue = new ArrayList<>();
        for (int i = 0;i<dataArray.length();i++){
            JSONObject arrayElement = dataArray.getJSONObject(i);
            returnValue.add(arrayElement.getString(key));
        }
        for (int i = 0;i<dataArray.length();i++){
            Log.d(TAG,String.valueOf(returnValue.get(i)));
        }
        return returnValue;
    }
}
