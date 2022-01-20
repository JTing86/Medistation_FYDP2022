package com.example.medistation_2.ui.analysis;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AnalysisFragment extends Fragment {

    private static final String TAG = AnalysisFragment.class.getSimpleName();
    LineChart lineChart;
    private ArrayList<Long> temperatureValue;
    private ArrayList<Long> temperatureDate;
    private ArrayList<Long> heartRateValue;
    private ArrayList<Long> heartRateDate;
    private ArrayList<Long> sleepQualityValue;
    private ArrayList<Long> getSleepQualityDate;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootDbRef = database.getReference();

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
        temperatureValue = new ArrayList<>();
        temperatureDate = new ArrayList<>();
        heartRateDate = new ArrayList<>();
        heartRateValue = new ArrayList<>();
        sleepQualityValue = new ArrayList<>();
        getSleepQualityDate = new ArrayList<>();
        lineChart = view.findViewById(R.id.analysisGraph);
        setupDropDownMenu(view);
    }

    public void setupDropDownMenu(View view) {
        //set up drop down list
        Spinner userInfoLeftMenu = view.findViewById(R.id.analysisUserInfoLeftDropDown);
        Spinner userInfoRightMenu = view.findViewById(R.id.analysisUserInfoRightDropDown);
        userInfoLeftMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String leftItemSelected = String.valueOf(adapter.getItemAtPosition(position));
                String rightItemSelected = ((Spinner) view.findViewById(R.id.analysisUserInfoRightDropDown)).getSelectedItem().toString();
                if (!rightItemSelected.equals("Patient Info")) {
                    switch (leftItemSelected) {
                        case "Heart Rate":
                            createHeartRateData(view,leftItemSelected, rightItemSelected,false);
                            break;
                        case "Temperature":
                            createTemperatureData(view,leftItemSelected,rightItemSelected,false);
                            break;
                        case "Sleep Quality":
                            createSleepQualityDate(view,leftItemSelected,rightItemSelected,false);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        userInfoRightMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String rightItemSelected = String.valueOf(adapter.getItemAtPosition(position));
                String leftItemSelected = ((Spinner) view.findViewById(R.id.analysisUserInfoLeftDropDown)).getSelectedItem().toString();
                if (!leftItemSelected.equals("Patient Info")) {
                    switch (rightItemSelected) {
                        case "Heart Rate":
                            createHeartRateData(view,leftItemSelected, rightItemSelected,false);
                            break;
                        case "Temperature":
                            createTemperatureData(view,leftItemSelected,rightItemSelected,false);
                            break;
                        case "Sleep Quality":
                            createSleepQualityDate(view,leftItemSelected,rightItemSelected,false);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        String[] userInfo = new String[]{"Patient Info", "Heart Rate", "Temperature", "Sleep Quality"};
        List<String> userInfoList = new ArrayList<>(Arrays.asList(userInfo));
        //Create severity drop down menu
        ArrayAdapter<String> userInfoMenuArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, userInfoList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View dropDownView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, dropDownView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }

        };

        userInfoMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        userInfoLeftMenu.setAdapter(userInfoMenuArrayAdapter);
        userInfoRightMenu.setAdapter(userInfoMenuArrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createTemperatureData(View view, String item1, String item2, boolean createGraph) {
        if (createGraph && item1.equals("Temperature")) {
            createGraph(view, item1, item2);
        }
        else {
            rootDbRef.child("temp").get().addOnCompleteListener(task -> {
                HashMap<String, Object> temperatureData;
                ArrayList<Long> tempDateTemp;
                ArrayList<Long> tempValueTemp;
                Date currentTime = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                String formattedDate = df.format(currentTime) + " 00:00:00.000";
                long currentDayUpperBound = dbHelper.toEpochTime(formattedDate);
                currentDayUpperBound = currentDayUpperBound / 1000;
                long currentDayLowerBound = currentDayUpperBound - 7 * 86400;
                temperatureData = (HashMap<String, Object>) task.getResult().getValue();
                tempDateTemp = (ArrayList<Long>) temperatureData.get("date");
                tempValueTemp = (ArrayList<Long>) temperatureData.get("value");
                for (int i = 0; i < Objects.requireNonNull(tempDateTemp).size(); i++) {
                    Log.d(TAG, String.valueOf(i));
                    if (tempDateTemp.get(i) <= currentDayUpperBound && tempDateTemp.get(i) >= currentDayLowerBound) {
                        temperatureDate.add(tempDateTemp.get(i));
                        assert tempValueTemp != null;
                        temperatureValue.add(tempValueTemp.get(i));
                    }
                }
                if (createGraph) {
                    createGraph(view, item1, item2);
                } else {
                    switch (item2) {
                        case "Heart Rate":
                            createHeartRateData(view, item1, item2, true);
                            break;
                        case "Temperature":
                            createTemperatureData(view, item1, item2, true);
                            break;
                        case "Sleep Quality":
                            createSleepQualityDate(view, item1, item2, true);
                            break;
                    }
                }

            });
        }
    }

    public void createHeartRateData(View view, String item1, String item2, boolean createGraph) {

    }

    public void createSleepQualityDate(View view,String item1, String item2, boolean createGraph) {

    }

    private void createGraph(View view, String item1, String item2) {
        List <Entry> lineEntriesSet1 = null;
        List <Entry> lineEntriesSet2 = null;
        switch (item1) {
            case "Heart Rate":
                lineEntriesSet1 = getDataSet(heartRateDate);
                break;
            case "Temperature":
                lineEntriesSet1 = getDataSet(temperatureValue);
                break;
            case "Sleep Quality":
                lineEntriesSet1 = getDataSet(sleepQualityValue);
                break;
        }
        switch (item2) {
            case "Heart Rate":
                lineEntriesSet2 = getDataSet(heartRateValue);
                break;
            case "Temperature":
                lineEntriesSet2 = getDataSet(temperatureValue);
                break;
            case "Sleep Quality":
                lineEntriesSet2 = getDataSet(sleepQualityValue);
                break;
        }
        LineDataSet lineDataSet1 = new LineDataSet(lineEntriesSet1, item1);
        LineDataSet lineDataSet2 = new LineDataSet(lineEntriesSet2, item2);

        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setHighlightEnabled(true);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setColor(Color.RED);
        lineDataSet1.setCircleColor(Color.YELLOW);
        lineDataSet1.setCircleRadius(6);
        lineDataSet1.setCircleHoleRadius(3);
        lineDataSet1.setDrawHighlightIndicators(true);
        lineDataSet1.setHighLightColor(Color.RED);
        lineDataSet1.setValueTextSize(12);
        lineDataSet1.setValueTextColor(Color.DKGRAY);

        lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineDataSet2.setHighlightEnabled(true);
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setColor(Color.BLUE);
        lineDataSet2.setCircleColor(Color.GREEN);
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleHoleRadius(3);
        lineDataSet2.setDrawHighlightIndicators(true);
        lineDataSet2.setHighLightColor(Color.RED);
        lineDataSet2.setValueTextSize(12);
        lineDataSet2.setValueTextColor(Color.DKGRAY);

        List<ILineDataSet> allDataSets = new ArrayList<>();
        allDataSets.add(lineDataSet1);
        allDataSets.add(lineDataSet2);
        LineData lineData = new LineData(allDataSets);
        lineChart.getDescription().setTextSize(12);
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet1.getEntryCount());
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
    }

    private List<Entry> getDataSet(ArrayList data) {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Long xDataValue = (Long) data.get(i);
            lineEntries.add(new Entry(i, xDataValue.floatValue()));
        }
        return lineEntries;
    }

}