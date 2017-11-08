package com.simplysmart.providesk.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.util.ParseDateFormat;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.sensor.SensorItem;
import com.simplysmart.lib.model.sensor.SensorReadingGraphResponse;
import com.simplysmart.lib.request.CreateRequest;

import java.util.ArrayList;

/**
 * Created by shekhar on 4/8/15.
 * Login screen for Application
 */
public class ReadingDetailsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private LinearLayout content_layout;
    private TextView no_data_found;
    private TextView yAxisLogo;
    private SensorItem sensorItem;
    private LineChart graphView;
    private YAxis leftAxis;
    private int color = 0;
    private String date;
    private String xAxisLable = "";

    private TextView readingValue, readingAtValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Reading Details");

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.bw_color_purple));
        }

        if (getIntent().getExtras() != null) {
            date = getIntent().getStringExtra("date");
            sensorItem = getIntent().getParcelableExtra("sensorData");
        }

        initializeWidgets();

        color = ContextCompat.getColor(this, R.color.bw_color_purple);

        if (NetworkUtilities.isInternet(ReadingDetailsActivity.this)) {
            no_data_found.setVisibility(View.GONE);
            getSensorsReadingGraph();
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(getResources().getString(R.string.error_no_internet_connection));
            progressBar.setVisibility(View.GONE);
            graphView.setVisibility(View.GONE);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().show();
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
        }
        return true;
    }

    private void initializeWidgets() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        content_layout = (LinearLayout) findViewById(R.id.content_layout);
        no_data_found = (TextView) findViewById(R.id.no_data_found);
        graphView = (LineChart) findViewById(R.id.graphView);
        yAxisLogo = (TextView) findViewById(R.id.y_axis_logo);

        readingValue = (TextView) findViewById(R.id.readingValue);
        readingAtValue = (TextView) findViewById(R.id.readingAtValue);

        if (sensorItem.getLast_reading() != null && !sensorItem.getLast_reading().equalsIgnoreCase("")) {
            readingValue.setText(sensorItem.getLast_reading());
            if (sensorItem.getUnit() != null) {
                readingValue.setText(sensorItem.getLast_reading() + " " + sensorItem.getUnit());
            }
        } else {
            readingValue.setText("---");
        }

        if (sensorItem.getLast_reading_at() != null && !sensorItem.getLast_reading_at().equalsIgnoreCase("")) {
            readingAtValue.setText(sensorItem.getLast_reading_at());
        } else {
            readingAtValue.setText("---");
        }
    }

    private void getSensorsReadingGraph() {

        progressBar.setVisibility(View.VISIBLE);
        graphView.setVisibility(View.GONE);

        CreateRequest.getInstance().getSensorsReadingGraph(sensorItem.getKey(), date, date, new ApiCallback<SensorReadingGraphResponse>() {

            @Override
            public void onSuccess(SensorReadingGraphResponse response) {
                progressBar.setVisibility(View.GONE);
                graphView.setVisibility(View.VISIBLE);

                if (response != null && response.getData() != null && response.getData().size() > 0) {
                    if (response.getAxis() != null) {
                        xAxisLable = response.getAxis().getX();
                        yAxisLogo.setText(response.getAxis().getY());
                    }
                    setGraphData(response.getData());
                }
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                graphView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setGraphData(ArrayList<ArrayList<String>> graphData) {

        initGraphProperties();

        ArrayList<String> time = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for (int i = 0; i < graphData.size(); i++) {

            if (xAxisLable.equalsIgnoreCase("time")) {
                time.add(ParseDateFormat.getTimeFromTimestamp(graphData.get(i).get(0), "HH:mm"));
            } else {
                time.add(ParseDateFormat.getDateFromTimestamp(graphData.get(i).get(0), "dd MMM"));
            }
            yValues.add(Float.parseFloat(graphData.get(i).get(1)));
        }

        renderGraph(yValues, time);
    }

    private void initGraphProperties() {

        graphView.setDrawGridBackground(false);
        graphView.setDescription("");
        graphView.setNoDataTextDescription("");
        graphView.setHighlightEnabled(true);
        graphView.setTouchEnabled(true);
        graphView.setDragEnabled(true);
        graphView.setScaleEnabled(true);
        graphView.setPinchZoom(true);
        graphView.getAxisRight().setEnabled(false);

        leftAxis = graphView.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLimitLinesBehindData(true);

        XAxis bottomAxis = graphView.getXAxis();
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setSpaceBetweenLabels(0);
        bottomAxis.setAvoidFirstLastClipping(true);
    }

    private void renderGraph(ArrayList<Float> yValues, ArrayList<String> time) {

        ArrayList<Entry> MainGraphLine = new ArrayList<>();

        for (int i = 0; i < time.size(); i++) {
            MainGraphLine.add(new Entry(yValues.get(i), i));
        }

        //set for main line graph
        LineDataSet set1 = new LineDataSet(MainGraphLine, xAxisLable);
        set1.setFillColor(color);
        set1.setColor(color);
        set1.setValueTextColor(Color.DKGRAY);
        set1.setCircleColor(color);
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setDrawFilled(true);
        set1.setDrawValues(true);
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.2f);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        // create a data object with the data sets
        LineData data = new LineData(time, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        graphView.setData(data);        // set data

        graphView.animateX(1000, Easing.EasingOption.EaseInOutQuart);
        graphView.invalidate();

        Legend legend = graphView.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

    }
}
