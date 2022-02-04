package com.example.medistation_2.ui.analysis;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Map;
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
    private long currentDayUpperBound;
    private long currentLowerBound;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setupDropDownMenu(View view) {
        //set up drop down list
        Spinner userInfoLeftMenu = view.findViewById(R.id.analysisUserInfoLeftDropDown);
        Spinner userInfoRightMenu = view.findViewById(R.id.analysisUserInfoRightDropDown);
        Spinner graphTimeRange = view.findViewById(R.id.analysisGraphTimeRange);
        Button generateGraph = view.findViewById(R.id.analysisGenerateGraph);
        generateGraph.setOnClickListener(v -> {
            String rightItemSelected = ((Spinner) view.findViewById(R.id.analysisUserInfoRightDropDown)).getSelectedItem().toString();
            String leftItemSelected = ((Spinner) view.findViewById(R.id.analysisUserInfoLeftDropDown)).getSelectedItem().toString();
            String timeRange = ((Spinner) view.findViewById(R.id.analysisGraphTimeRange)).getSelectedItem().toString();
            clearFunction();
            if (!rightItemSelected.equals("Patient Info") && !leftItemSelected.equals("Patient Info") && !timeRange.equals("Time Range")) {
                int graphRange = 0;
                if (timeRange.equals("Past 7 Days")) {
                    graphRange = 7;
                } else if (timeRange.equals("Past Month")) {
                    graphRange = 30;
                }
                findCurrentTime(graphRange);
                switch (leftItemSelected) {
                    case "Heart Rate":
                        createHeartRateData(view, leftItemSelected, rightItemSelected, false, graphRange);
                        break;
                    case "Temperature":
                        createTemperatureData(view, leftItemSelected, rightItemSelected, false, graphRange);
                        break;
                    case "Sleep Quality":
                        createSleepQualityDate(view, leftItemSelected, rightItemSelected, false, graphRange);
                        break;
                }
            }
        });

        String[] timeRange = new String[]{"Time Range", "Past 7 Days", "Past Month"};
        String[] userInfo = new String[]{"Patient Info", "Heart Rate", "Temperature", "Sleep Quality"};
        List<String> userInfoList = new ArrayList<>(Arrays.asList(userInfo));
        List<String> timeRangeList = new ArrayList<>(Arrays.asList(timeRange));
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
        ArrayAdapter<String> graphTimeRangeArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, timeRangeList) {
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
        graphTimeRangeArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        userInfoMenuArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        userInfoLeftMenu.setAdapter(userInfoMenuArrayAdapter);
        userInfoRightMenu.setAdapter(userInfoMenuArrayAdapter);
        graphTimeRange.setAdapter(graphTimeRangeArrayAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createTemperatureData(View view, String item1, String item2, boolean createGraph, int timeRange) {
        if (createGraph && item1.equals("Temperature")) {
            createGraph(view, item1, item2,timeRange);
        } else {
            rootDbRef.child("temp").get().addOnCompleteListener(task -> {
                HashMap<String, Object> temperatureData;
                ArrayList<Long> tempDateTemp;
                ArrayList<Long> tempValueTemp;
                temperatureData = (HashMap<String, Object>) task.getResult().getValue();
                tempDateTemp = (ArrayList<Long>) temperatureData.get("date");
                tempValueTemp = (ArrayList<Long>) temperatureData.get("value");
                for (int i = 0; i < Objects.requireNonNull(tempDateTemp).size(); i++) {
                    if (tempDateTemp.get(i) <= currentDayUpperBound && tempDateTemp.get(i) >= currentLowerBound) {
                        temperatureDate.add(tempDateTemp.get(i));
                        assert tempValueTemp != null;
                        temperatureValue.add(tempValueTemp.get(i));
                    }
                }
                if (createGraph) {
                    createGraph(view, item1, item2,timeRange);
                } else {
                    switch (item2) {
                        case "Heart Rate":
                            createHeartRateData(view, item1, item2, true, timeRange);
                            break;
                        case "Temperature":
                            createTemperatureData(view, item1, item2, true, timeRange);
                            break;
                        case "Sleep Quality":
                            createSleepQualityDate(view, item1, item2, true, timeRange);
                            break;
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createHeartRateData(View view, String item1, String item2, boolean createGraph, int timeRange) {
        if (createGraph && item1.equals("Heart Rate")) {
            createGraph(view, item1, item2,timeRange);
        } else {
            rootDbRef.child("heartRate").get().addOnCompleteListener(task -> {
                ArrayList<Map<String,Object>> allHeartData = (ArrayList<Map<String, Object>>) task.getResult().getValue();
                for (int i = 0; i < Objects.requireNonNull(allHeartData).size(); i++) {
                    HashMap<String, Object> dailyHeartRateData= (HashMap<String, Object>) allHeartData.get(i);
                    ArrayList<Long> dailyHeartRateValue = (ArrayList<Long>) dailyHeartRateData.get("value");
                    long currentTimeStamp = (long) dailyHeartRateData.get("date");
                    long dailyHeartRateAverage = 0;
                    if (currentTimeStamp < currentDayUpperBound && currentTimeStamp >= currentLowerBound) {
                        for (int j = 0; j< Objects.requireNonNull(dailyHeartRateValue).size(); j++){
                            dailyHeartRateAverage = dailyHeartRateValue.get(i) + dailyHeartRateAverage;
                        }
                        dailyHeartRateAverage = dailyHeartRateAverage/dailyHeartRateValue.size();
                        heartRateValue.add(dailyHeartRateAverage);
                        heartRateDate.add(currentTimeStamp);
                    }
                }
                if (createGraph) {
                    createGraph(view, item1, item2, timeRange);
                } else {
                    switch (item2) {
                        case "Heart Rate":
                            createHeartRateData(view, item1, item2, true, timeRange);
                            break;
                        case "Temperature":
                            createTemperatureData(view, item1, item2, true, timeRange);
                            break;
                        case "Sleep Quality":
                            createSleepQualityDate(view, item1, item2, true, timeRange);
                            break;
                    }
                }
            });
        }
    }

    public void createSleepQualityDate(View view, String item1, String item2, boolean createGraph, int timeRange) {

    }

    private void createGraph(View view, String item1, String item2, int timeRange) {
        List<Entry> lineEntriesSet1 = null;
        List<Entry> lineEntriesSet2 = null;
        switch (item1) {
            case "Heart Rate":
                lineEntriesSet1 = getDataSet(heartRateValue);
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
        lineDataSet1.setDrawValues(false);

        lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineDataSet2.setDrawValues(false);
        lineDataSet2.setColor(Color.RED);
        List<ILineDataSet> allDataSets = new ArrayList<>();
        allDataSets.add(lineDataSet1);
        allDataSets.add(lineDataSet2);
        LineData lineData = new LineData(allDataSets);
        lineChart.getDescription().setTextSize(12);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity((float) Math.floor(timeRange/6));
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

    public void clearFunction() {
        lineChart.clear();
        temperatureValue.clear();
        temperatureDate.clear();
        heartRateDate.clear();
        heartRateValue.clear();
        sleepQualityValue.clear();
        getSleepQualityDate.clear();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void findCurrentTime (int timeRange){
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(currentTime) + " 00:00:00.000";
        currentDayUpperBound = dbHelper.toEpochTime(formattedDate)/1000;
        currentLowerBound = currentDayUpperBound - timeRange * 86400L;
    }
}