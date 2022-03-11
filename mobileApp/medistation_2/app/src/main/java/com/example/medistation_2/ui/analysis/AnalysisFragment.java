package com.example.medistation_2.ui.analysis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medistation_2.R;
import com.example.medistation_2.helperFunctions.dbHelper;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnalysisFragment extends Fragment {

    private static final String TAG = AnalysisFragment.class.getSimpleName();
    LineChart lineChart;
    private ArrayList<Long> temperatureValue;
    private ArrayList<Long> heartRateValue;
    public static ArrayList<Long> dateList;
    private ArrayList<Double> sleepQualityValue;
    private static long currentUpperBound;
    private static long currentLowerBound;
    private ArrayList<Double> symptomSeverityList;
    public static ArrayList<String> symptomDateList;
    public static ArrayList<Integer> numberOfOccurrence;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootDbRef = database.getReference();
    public static boolean [] whichData;
    HashMap<String, ArrayList<Long>> severity_map;

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
        DatePicker dateSelect = view.findViewById(R.id.graphStartDate);
        LocalDate today = LocalDate.now();
        dateSelect.updateDate(today.getYear(),today.getMonthValue()-2,today.getDayOfMonth());
        temperatureValue = new ArrayList<>();
        dateList = new ArrayList<>();
        heartRateValue = new ArrayList<>();
        sleepQualityValue = new ArrayList<>();
        symptomSeverityList = new ArrayList<Double>(30);
        symptomDateList = new ArrayList<String>(30);
        lineChart = view.findViewById(R.id.analysisGraph);
        whichData = new boolean[4];
        numberOfOccurrence = new ArrayList<>();
        setupDropDownMenu(view);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setupDropDownMenu(View view) {
        //set up drop down list
        Spinner dataSelection = view.findViewById(R.id.dataSelectionMenu);
        Button generateGraph = view.findViewById(R.id.analysisGenerateGraph);
        for (int i = 0; i < 4;i++){
            whichData[i] = false;
        }
        generateGraph.setOnClickListener(v -> {
            findCurrentTime(view);
            clearFunction();
            String ItemSelected = dataSelection.getSelectedItem().toString();
            if (ItemSelected != "Patient Info") {
                switch (ItemSelected) {
                    case "Heart Rate":
                        createHeartRateData();
                        whichData[0]= true;
                        break;
                    case "Temperature":
                        createTemperatureData();
                        whichData[1]= true;
                        break;
                    case "Sleep Quality":
                        createSleepQualityData();
                        whichData[2]= true;
                        break;
                    default:
                        try {
                            whichData[3]= true;
                            createSymptomData(ItemSelected, view);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        String[] data = new String[]{"Patient Info", "Heart Rate", "Temperature", "Sleep Quality"};
        List<String> dataList = new ArrayList<>(Arrays.asList(data));
        Query myTopPostsQuery = rootDbRef.child("symptom/");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    dataList.add(postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        });
        //Create severity drop down menu
        ArrayAdapter<String> dataListArrayAdapter = new ArrayAdapter<String>(requireActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, dataList) {
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
        dataListArrayAdapter.setDropDownViewResource(R.layout.drop_down_menu_spinner);
        dataSelection.setAdapter(dataListArrayAdapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createTemperatureData() {
        rootDbRef.child("temp").get().addOnCompleteListener(task -> {
            HashMap<String, Object> temperatureData;
            ArrayList<Long> tempDateTemp;
            ArrayList<Long> tempValueTemp;
            temperatureData = (HashMap<String, Object>) Objects.requireNonNull(task.getResult()).getValue();
            tempDateTemp = (ArrayList<Long>) temperatureData.get("date");
            tempValueTemp = (ArrayList<Long>) temperatureData.get("value");
            for (int i = 0; i < Objects.requireNonNull(tempDateTemp).size(); i++) {
                if (tempDateTemp.get(i) <= currentUpperBound && tempDateTemp.get(i) >= currentLowerBound) {
                    dateList.add(tempDateTemp.get(i));
                    assert tempValueTemp != null;
                    temperatureValue.add(tempValueTemp.get(i));
                }
            }
            createGraph("Temperature");
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createHeartRateData() {
        rootDbRef.child("heartRate").get().addOnCompleteListener(task -> {
            ArrayList<Map<String, Object>> allHeartData = (ArrayList<Map<String, Object>>) task.getResult().getValue();
            for (int i = 0; i < Objects.requireNonNull(allHeartData).size(); i++) {
                HashMap<String, Object> dailyHeartRateData = (HashMap<String, Object>) allHeartData.get(i);
                Log.d(TAG, String.valueOf(i));
                ArrayList<Long> dailyHeartRateValue = (ArrayList<Long>) dailyHeartRateData.get("value");
                long currentTimeStamp = (long) dailyHeartRateData.get("date");
                long dailyHeartRateAverage = 0;
                if (currentTimeStamp < currentUpperBound && currentTimeStamp >= currentLowerBound) {
                    for (int j = 0; j < Objects.requireNonNull(dailyHeartRateValue).size(); j++) {
                        dailyHeartRateAverage = dailyHeartRateValue.get(j) + dailyHeartRateAverage;
                    }
                    dailyHeartRateAverage = dailyHeartRateAverage / dailyHeartRateValue.size();
                    heartRateValue.add(dailyHeartRateAverage);
                    dateList.add(currentTimeStamp);
                }
            }
            createGraph("Heart Rate");
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createSleepQualityData() {
        rootDbRef.child("sleep").get().addOnCompleteListener(task -> {
            ArrayList<Map<String, Object>> allSleepQuality = (ArrayList<Map<String, Object>>) task.getResult().getValue();
            for (int i = 0; i < Objects.requireNonNull(allSleepQuality).size(); i++) {
                HashMap<String, Object> dailySleepQuality = (HashMap<String, Object>) allSleepQuality.get(i);
                ArrayList<Long> dailySleepQualityValueList = (ArrayList<Long>) dailySleepQuality.get("quality");
                ArrayList<Long> startTimes = (ArrayList<Long>) dailySleepQuality.get("start");
                ArrayList<Long> endTimes = (ArrayList<Long>) dailySleepQuality.get("end");
                ArrayList<Long> sleepDuration = new ArrayList<>();
                double sleepQuality = 0.0;
                long totalSleepDuration = 0;
                for (int j = 0; j < Objects.requireNonNull(startTimes).size(); j++) {
                    assert endTimes != null;
                    sleepDuration.add(endTimes.get(j) - startTimes.get(j));
                    totalSleepDuration = Math.toIntExact(endTimes.get(j) - startTimes.get(j) + totalSleepDuration);
                }
                for (int k = 0; k < sleepDuration.size(); k++) {
                    sleepQuality = ((double) sleepDuration.get(k) / (double) totalSleepDuration) * (double) dailySleepQualityValueList.get(k) + sleepQuality;
                }
                sleepQualityValue.add(sleepQuality);
                dateList.add(startTimes.get(0));
            }
            createGraph("Sleep Quality");
        });
    }

    private void createGraph(String item1) {
        List<Entry> lineEntriesSet1 = null;
        switch (item1) {
            case "Heart Rate":
                lineEntriesSet1 = getDataSetLong(heartRateValue);
                break;
            case "Temperature":
                lineEntriesSet1 = getDataSetLong(temperatureValue);
                break;
            case "Sleep Quality":
                lineEntriesSet1 = getDataSetDouble(sleepQualityValue);
                break;
            default:
                lineEntriesSet1 = getDataSetDouble(symptomSeverityList);
        }
        LineDataSet lineDataSet1 = new LineDataSet(lineEntriesSet1, item1);
        lineDataSet1.setDrawValues(false);
        LineData lineData = new LineData(lineDataSet1);
        lineChart.getDescription().setTextSize(12);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateXY(10, 10);
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(5);
        lineChart.getXAxis().setLabelCount(lineDataSet1.getEntryCount());
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);
        CustomMPLineChartMarkerView mv = new CustomMPLineChartMarkerView(this.getContext());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);

    }

    private List<Entry> getDataSetLong(ArrayList<Long> data) {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Long xDataValue = data.get(i);
            lineEntries.add(new Entry(i, xDataValue.floatValue()));
        }
        return lineEntries;
    }

    private List<Entry> getDataSetDouble(ArrayList<Double> data) {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Double xDataValue = data.get(i);
            lineEntries.add(new Entry(i, xDataValue.floatValue()));
        }
        return lineEntries;
    }

    public void clearFunction() {
        lineChart.clear();
        temperatureValue.clear();
        dateList.clear();
        heartRateValue.clear();
        sleepQualityValue.clear();
        symptomSeverityList.clear();
        symptomDateList.clear();
        for (int i = 0; i < 4;i++){
            whichData[i] = false;
        }
        numberOfOccurrence.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void findCurrentTime(View view) {
        DatePicker graphStartDatePicker = (DatePicker) view.findViewById(R.id.graphStartDate);
        String year = String.valueOf(graphStartDatePicker.getYear());
        String month = String.valueOf(graphStartDatePicker.getMonth() + 1);
        String day = String.valueOf(graphStartDatePicker.getDayOfMonth());
        if (month.length() == 1)
            month = "0" + month;
        if (day.length() == 1)
            day = "0" + day;
        String formattedDate = year + month + day + " 00:00:00.000";
        currentUpperBound = dbHelper.toEpochTime(formattedDate) / 1000;
        currentLowerBound = currentUpperBound - 30 * 86400L;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createSymptomData(String symptomName, View view) throws InterruptedException {
        DatePicker graphStartDatePicker = (DatePicker) view.findViewById(R.id.graphStartDate);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = database.getReference();

        LocalDate date_selected = LocalDate.of(graphStartDatePicker.getYear(), graphStartDatePicker.getMonth() + 1, graphStartDatePicker.getDayOfMonth());

        Thread getter_thread = new Thread(() -> rootDbRef.child("symptom/" + symptomName).get().addOnCompleteListener(task -> {
            severity_map = (HashMap<String, ArrayList<Long>>) task.getResult().getValue();

        }));
        getter_thread.start();

        new Handler().postDelayed(() -> {
            LocalDate tmp = date_selected;

            for (int i = 0; i < 30; i++) {
                symptomDateList.add(tmp.toString());
                if (severity_map.get(tmp.toString()) != null) {
                    Log.d(TAG, tmp.toString());
                    ArrayList<Long> symptomValues = severity_map.get(tmp.toString());
                    numberOfOccurrence.add(symptomValues.size());
                    double average = 0;
                    for (int j = 0; j < symptomValues.size(); j++) {
                        average = average + symptomValues.get(j);
                    }
                    symptomSeverityList.add((average / symptomValues.size()));
                } else {
                    symptomSeverityList.add((double) 0);
                    numberOfOccurrence.add(0);
                }
                tmp = tmp.minusDays(-1);
            }
            createGraph(symptomName);
        }, 1000); //Timer is in ms here.

    }
}


class CustomMPLineChartMarkerView extends MarkerView {
    private static final String TAG = AnalysisFragment.class.getSimpleName();
    private final int DEFAULT_INDICATOR_COLOR = 0xffFD9138;//The default color of the indicator
    private final int ARROW_HEIGHT = dp2px(5); //  The height of the arrow
    private final int ARROW_WIDTH = dp2px(10); //  The width of the arrow
    private final float ARROW_OFFSET = dp2px(2);//Arrow offset
    private final float BG_CORNER = dp2px(2);//Background rounded corners
    private final TextView dataLabel;//text
    private Bitmap bitmapForDot;//Select a picture
    private int bitmapWidth;//Point width
    private int bitmapHeight;//Point high

    public CustomMPLineChartMarkerView(Context context) {
        super(context, R.layout.layout_for_custom_marker_view);
        dataLabel = findViewById(R.id.tvContent);
        //Picture self-replacement

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            dataLabel.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            if (AnalysisFragment.whichData[3]){
                String date = AnalysisFragment.symptomDateList.get((int) e.getX());
                dataLabel.setGravity(Gravity.CENTER);
                dataLabel.setText(date+ "\n" + "Number Of Occurrence = "+ AnalysisFragment.numberOfOccurrence.get((int) e.getX()));
            } else{
                Long date = AnalysisFragment.dateList.get((int) e.getX());
                String dateInString = dbHelper.fromEpochTime(date * 1000);
                dataLabel.setGravity(Gravity.CENTER);
                dataLabel.setText(dateInString.substring(0, 4) + "," + dateInString.substring(4, 6) + "," + dateInString.substring(6, 8) + "\n" + Utils.formatNumber(e.getY(), 0, true));
            }


        }
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        Chart chart = getChartView();
        if (chart == null) {
            super.draw(canvas, posX, posY);
            return;
        }
        //Indicator background brush
        Paint bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(DEFAULT_INDICATOR_COLOR);
        //Cutting brush
        Paint arrowPaint = new Paint();
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setColor(DEFAULT_INDICATOR_COLOR);

        float width = getWidth();
        float height = getHeight();

        int saveId = canvas.save();
        //Move the canvas to the point and draw the point
        canvas.translate(posX, posY);
        //canvas.drawBitmap(bitmapForDot, -bitmapWidth / 2f , -bitmapHeight / 2f ,null);

        //Draw indicator
        Path path = new Path();
        RectF bRectF;
        if (posY < height + ARROW_HEIGHT + ARROW_OFFSET + bitmapHeight / 2f) {//Handling beyond the upper boundary
            //Move the canvas and draw triangles and background
            canvas.translate(0, height + ARROW_HEIGHT + ARROW_OFFSET + bitmapHeight / 2f);
            path.moveTo(0, -(height + ARROW_HEIGHT));
            path.lineTo(ARROW_WIDTH / 2f, -(height - BG_CORNER));
            path.lineTo(-ARROW_WIDTH / 2f, -(height - BG_CORNER));
            path.lineTo(0, -(height + ARROW_HEIGHT));

            bRectF = new RectF(-width / 2, -height, width / 2, 0);

            canvas.drawPath(path, arrowPaint);
            canvas.drawRoundRect(bRectF, BG_CORNER, BG_CORNER, bgPaint);
            canvas.translate(-width / 2f, -height);
        } else {//Did not exceed the upper boundary
            //Move the canvas and draw triangles and background
            canvas.translate(0, -height - ARROW_HEIGHT - ARROW_OFFSET - bitmapHeight / 2f);
            path.moveTo(0, height + ARROW_HEIGHT);
            path.lineTo(ARROW_WIDTH / 2f, height - BG_CORNER);
            path.lineTo(-ARROW_WIDTH / 2f, height - BG_CORNER);
            path.lineTo(0, height + ARROW_HEIGHT);

            bRectF = new RectF(-width / 2, 0, width / 2, height);

            canvas.drawPath(path, arrowPaint);
            canvas.drawRoundRect(bRectF, BG_CORNER, BG_CORNER, bgPaint);
            canvas.translate(-width / 2f, 0);
        }
        draw(canvas);
        canvas.restoreToCount(saveId);
    }

    private int dp2px(int dpValues) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValues,
                getResources().getDisplayMetrics());
    }


}