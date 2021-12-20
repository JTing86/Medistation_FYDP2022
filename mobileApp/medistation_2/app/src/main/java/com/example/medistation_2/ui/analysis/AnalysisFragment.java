package com.example.medistation_2.ui.analysis;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.JsonHelper;
import com.example.medistation_2.helperFunctions.dbHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalysisFragment extends Fragment {

    private static final String TAG = AnalysisFragment.class.getSimpleName();

    public static AnalysisFragment newInstance() {
        return new AnalysisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

    }

}