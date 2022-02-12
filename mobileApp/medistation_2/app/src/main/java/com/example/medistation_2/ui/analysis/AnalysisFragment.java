package com.example.medistation_2.ui.analysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.example.medistation_2.helperFunctions.createData;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
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
    public static ArrayList<Long> temperatureDate;
    private ArrayList<Long> heartRateValue;
    public static ArrayList<Long> heartRateDate;
    private ArrayList<Double> sleepQualityValue;
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
        createData.heartRate(30);
        createData.temperatureData(30);
        createData.sleepQuality(30);
        temperatureValue = new ArrayList<>();
        temperatureDate = new ArrayList<>();
        heartRateDate = new ArrayList<>();
        heartRateValue = new ArrayList<>();
        sleepQualityValue = new ArrayList<>();
        lineChart = view.findViewById(R.id.analysisGraph);
        setupDropDownMenu(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setupDropDownMenu(View view) {
        //set up drop down list
        Spinner userInfoLeftMenu = view.findViewById(R.id.analysisUserInfoLeftDropDown);
        Spinner userInfoRightMenu = view.findViewById(R.id.analysisUserInfoRightDropDown);
        Button generateGraph = view.findViewById(R.id.analysisGenerateGraph);
        generateGraph.setOnClickListener(v -> {
            clearFunction();
            String rightItemSelected = userInfoLeftMenu.getSelectedItem().toString();
            String leftItemSelected = userInfoRightMenu.getSelectedItem().toString();
            switch (leftItemSelected) {
                case "Heart Rate":
                    createHeartRateData(view, leftItemSelected, rightItemSelected, false);
                    break;
                case "Temperature":
                    createTemperatureData(view, leftItemSelected, rightItemSelected, false);
                    break;
                case "Sleep Quality":
                    createSleepQualityData(view, leftItemSelected, rightItemSelected, false);
                    break;
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
            createGraph(item1, item2);
        } else {
            rootDbRef.child("temp").get().addOnCompleteListener(task -> {
                HashMap<String, Object> temperatureData;
                ArrayList<Long> tempDateTemp;
                ArrayList<Long> tempValueTemp;
                temperatureData = (HashMap<String, Object>) Objects.requireNonNull(task.getResult()).getValue();
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
                    createGraph(item1, item2);
                } else {
                    switch (item2) {
                        case "Heart Rate":
                            createHeartRateData(view, item1, item2, true);
                            break;
                        case "Temperature":
                            createTemperatureData(view, item1, item2, true);
                            break;
                        case "Sleep Quality":
                            createSleepQualityData(view, item1, item2, true);
                            break;
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createHeartRateData(View view, String item1, String item2, boolean createGraph) {
        if (createGraph && item1.equals("Heart Rate")) {
            createGraph(item1, item2);
        } else {
            rootDbRef.child("heartRate").get().addOnCompleteListener(task -> {
                ArrayList<Map<String, Object>> allHeartData = (ArrayList<Map<String, Object>>) task.getResult().getValue();
                for (int i = 0; i < Objects.requireNonNull(allHeartData).size(); i++) {
                    HashMap<String, Object> dailyHeartRateData = (HashMap<String, Object>) allHeartData.get(i);
                    ArrayList<Long> dailyHeartRateValue = (ArrayList<Long>) dailyHeartRateData.get("value");
                    long currentTimeStamp = (long) dailyHeartRateData.get("date");
                    long dailyHeartRateAverage = 0;
                    if (currentTimeStamp < currentDayUpperBound && currentTimeStamp >= currentLowerBound) {
                        for (int j = 0; j < Objects.requireNonNull(dailyHeartRateValue).size(); j++) {
                            dailyHeartRateAverage = dailyHeartRateValue.get(i) + dailyHeartRateAverage;
                        }
                        dailyHeartRateAverage = dailyHeartRateAverage / dailyHeartRateValue.size();
                        heartRateValue.add(dailyHeartRateAverage);
                        heartRateDate.add(currentTimeStamp);
                    }
                }
                if (createGraph) {
                    createGraph(item1, item2);
                } else {
                    switch (item2) {
                        case "Heart Rate":
                            createHeartRateData(view, item1, item2, true);
                            break;
                        case "Temperature":
                            createTemperatureData(view, item1, item2, true);
                            break;
                        case "Sleep Quality":
                            createSleepQualityData(view, item1, item2, true);
                            break;
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createSleepQualityData(View view, String item1, String item2, boolean createGraph) {
        if (createGraph && item1.equals("Sleep Quality")) {
            createGraph(item1, item2);
        } else {
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
                }
                if (createGraph) {
                    createGraph(item1, item2);
                } else {
                    switch (item2) {
                        case "Heart Rate":
                            createHeartRateData(view, item1, item2, true);
                            break;
                        case "Temperature":
                            createTemperatureData(view, item1, item2, true);
                            break;
                        case "Sleep Quality":
                            createSleepQualityData(view, item1, item2, true);
                            break;
                    }
                }
            });
        }
    }


    private void createGraph(String item1, String item2) {
        List<Entry> lineEntriesSet1 = null;
        List<Entry> lineEntriesSet2 = null;

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
        }
        switch (item2) {
            case "Heart Rate":
                lineEntriesSet2 = getDataSetLong(heartRateValue);
                break;
            case "Temperature":
                lineEntriesSet2 = getDataSetLong(temperatureValue);
                break;
            case "Sleep Quality":
                lineEntriesSet2 = getDataSetDouble(sleepQualityValue);
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
        lineChart.animateXY(1000, 1000);
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
        temperatureDate.clear();
        heartRateDate.clear();
        heartRateValue.clear();
        sleepQualityValue.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void findCurrentTime(int timeRange) {
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(currentTime) + " 00:00:00.000";
        currentDayUpperBound = dbHelper.toEpochTime(formattedDate) / 1000;
        currentLowerBound = currentDayUpperBound - timeRange * 86400L;
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
            ArrayList<Long> dateList;
            if (AnalysisFragment.temperatureDate.isEmpty()) {
                dateList = AnalysisFragment.heartRateDate;
            } else
                dateList = AnalysisFragment.temperatureDate;

            Long date = dateList.get((int) e.getX());
            String dateInString = dbHelper.fromEpochTime(date * 1000);
            dataLabel.setGravity(Gravity.CENTER);
            dataLabel.setText(dateInString.substring(0, 4) + "," + dateInString.substring(4, 6) + "," + dateInString.substring(6, 8) + "\n" + Utils.formatNumber(e.getY(), 0, true));

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