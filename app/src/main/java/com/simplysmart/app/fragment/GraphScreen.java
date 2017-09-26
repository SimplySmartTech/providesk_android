package com.simplysmart.app.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.app.util.ParseDateFormat;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.graphs.DurationAverage;
import com.simplysmart.lib.model.graphs.GraphData;
import com.simplysmart.lib.request.CreateRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by shekhar on 21/8/15.
 * A GraphScreen to show usage graph (Trends)
 */
public class GraphScreen extends BaseFragment {

    private View rootView;
    private BarChart mChart;
    private TextView lastMButton, threeMButton, sixMButton, twelveMButton;
    private TextView forBillButton, forUnitButton, YaxisLogo;
    private YAxis leftAxis;

    private int color = 0;
    private int graph_line_user_average_color = 0;
    private int graph_line_township_average_color = 0;

    private String logoText;
    private ProgressBar progressBar;
    private RelativeLayout content_layout, graphLayout;
    private TextView no_data_found;

    private int monthCount = 3;
    private String yAxisType = "BILL";
    private String botType = "";


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_graph_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Set default color
        color = ContextCompat.getColor(_activity, R.color.bw_color_red);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Set color based on referrer
            String category = bundle.getString(REFERRER_SCREEN);
            if (category != null && category.equalsIgnoreCase(getString(R.string.title_electricity))) {
                color = ContextCompat.getColor(_activity, R.color.bw_color_orange);
                logoText = _activity.getString(R.string.icon_thunder);
                botType = AppConstant.ELECTRICITY_BOT;
            } else if (category != null && category.equalsIgnoreCase(_activity.getString(R.string.title_water))) {
                color = ContextCompat.getColor(_activity, R.color.bw_color_blue);
                logoText = _activity.getString(R.string.icon_water);
                botType = AppConstant.WATER_BOT;
            }
        }
        super.onActivityCreated(savedInstanceState);

        initializeWidget();
        initGraphProperties();

        threeMButton.performClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("UnitName");
        LocalBroadcastManager.getInstance(_activity).registerReceiver(onUnitIDSelection, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(_activity).unregisterReceiver(onUnitIDSelection);
    }

    private void initializeWidget() {

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        content_layout = (RelativeLayout) rootView.findViewById(R.id.content_layout);
        graphLayout = (RelativeLayout) rootView.findViewById(R.id.graphLayout);
        no_data_found = (TextView) rootView.findViewById(R.id.no_data_found);

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        forBillButton = (TextView) rootView.findViewById(R.id.btn_for_bill);
        forUnitButton = (TextView) rootView.findViewById(R.id.btn_for_unit);
        lastMButton = (TextView) rootView.findViewById(R.id.btn_for_last);
        threeMButton = (TextView) rootView.findViewById(R.id.btn_for_three);
        sixMButton = (TextView) rootView.findViewById(R.id.btn_for_six);
        twelveMButton = (TextView) rootView.findViewById(R.id.btn_for_twelve);
        YaxisLogo = (TextView) rootView.findViewById(R.id.y_axis_logo);

        graph_line_user_average_color = ContextCompat.getColor(_activity, R.color.graph_line_user_average);
        graph_line_township_average_color = ContextCompat.getColor(_activity, R.color.graph_line_township_average);

        forBillButton.setOnClickListener(billGraphClick);
        forUnitButton.setOnClickListener(unitGraphClick);
        lastMButton.setOnClickListener(lastMClick);
        threeMButton.setOnClickListener(threeMClick);
        sixMButton.setOnClickListener(sixMClick);
        twelveMButton.setOnClickListener(twelveMClick);

        Typeface iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);

        YaxisLogo.setTypeface(iconTypeface);
        YaxisLogo.setText(_activity.getResources().getString(R.string.icon_rupee));
    }

    private final View.OnClickListener billGraphClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            forBillButton.setBackgroundColor(color);
            forUnitButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));

            yAxisType = "BILL";
            getGraphInfoRequest(monthCount);
            YaxisLogo.setText(_activity.getString(R.string.Rs));
        }
    };

    private final View.OnClickListener unitGraphClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            forBillButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            forUnitButton.setBackgroundColor(color);

            yAxisType = "UNIT";
            getGraphInfoRequest(monthCount);
            YaxisLogo.setText(logoText);
        }
    };

    private final View.OnClickListener lastMClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            lastMButton.setBackgroundColor(color);
            threeMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            sixMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            twelveMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));

            monthCount = 1;
            getGraphInfoRequest(monthCount);

        }
    };

    private final View.OnClickListener threeMClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            lastMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            threeMButton.setBackgroundColor(color);
            sixMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            twelveMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));

            monthCount = 3;
            getGraphInfoRequest(monthCount);
        }
    };

    private final View.OnClickListener sixMClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            lastMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            threeMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            sixMButton.setBackgroundColor(color);
            twelveMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));

            monthCount = 6;
            getGraphInfoRequest(monthCount);
        }
    };

    private final View.OnClickListener twelveMClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            lastMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            threeMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            sixMButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            twelveMButton.setBackgroundColor(color);

            monthCount = 12;
            getGraphInfoRequest(monthCount);
        }
    };

    private void getGraphInfoRequest(Integer duration) {

        if (NetworkUtilities.isInternet(_activity)) {
            progressBar.setVisibility(View.VISIBLE);
            graphLayout.setVisibility(View.GONE);
            CreateRequest.getInstance().showTrends(GlobalData.getInstance().getDemoUnitId(), botType, duration, new ApiCallback<BotResponse>() {
                @Override
                public void onSuccess(BotResponse response) {
                    progressBar.setVisibility(View.GONE);
                    graphLayout.setVisibility(View.VISIBLE);
                    parseGraphResponse(response);
                }

                @Override
                public void onFailure(String error) {
                    progressBar.setVisibility(View.GONE);
                    no_data_found.setText(error);
                }
            });
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
            progressBar.setVisibility(View.GONE);
        }
    }

    private final BroadcastReceiver onUnitIDSelection = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            DebugLog.d("message received unit name: " + intent.getStringExtra("name"));
            DebugLog.d("message received unit id: " + intent.getStringExtra("id"));

            if (NetworkUtilities.isInternet(_activity)) {
                getGraphInfoRequest(monthCount);
            } else {
                showMyDialog(_activity.getString(R.string.app_name), getString(R.string.error_no_internet_connection), "OK");
            }
        }
    };

    public void parseGraphResponse(BotResponse response) {

        String durationAverageVal = "0";
        GraphData trends = response.getData().getBot().getTrends();
        DurationAverage durationAverage = response.getData().getBot().getDuration_average();

        //default value for 3 month selection
        ArrayList<String> months = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();
        ArrayList<Float> yValuesBackup = new ArrayList<>();

        if (trends != null && trends.getBill() != null) {
            for (int i = 0; i < trends.getBill().size(); i++) {
                try {
                    if (yAxisType.equalsIgnoreCase("BILL")) {

                        yValues.add(Float.parseFloat(trends.getBill().get(i).getValue()));
                        if (monthCount != 1) {
                            months.add(ParseDateFormat.getMonthFromDate(trends.getBill().get(i).getDate()));
                        } else {
                            months.add(ParseDateFormat.getMonthDate(trends.getBill().get(i).getDate()));
                            i += 1;
                        }
                        durationAverageVal = durationAverage.getBill();

                    } else {

                        yValues.add(Float.parseFloat(trends.getUnits().get(i).getValue().getRegular()));

                        if (trends.getUnits().get(i).getValue().getBackup() != null) {
                            yValuesBackup.add(Float.parseFloat(trends.getUnits().get(i).getValue().getBackup()));
                        }

                        if (monthCount != 1) {
                            months.add(ParseDateFormat.getMonthFromDate(trends.getUnits().get(i).getDate()));
                        } else {
                            months.add(ParseDateFormat.getMonthDate(trends.getUnits().get(i).getDate()));
                            i += 1;
                        }

                        durationAverageVal = durationAverage.getUnits();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            setGraphFilterData(yValues, yValuesBackup, months, durationAverageVal);
        }
    }

    private void initGraphProperties() {

        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setNoDataTextDescription("");
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getAxisRight().setEnabled(false);

        leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLimitLinesBehindData(true);

        XAxis bottomAxis = mChart.getXAxis();
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setSpaceBetweenLabels(0);
        bottomAxis.setAvoidFirstLastClipping(true);

        //Make Bill Button Selected for first time
        forBillButton.setBackgroundColor(color);
        forUnitButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));

    }


    private void setGraphFilterData(ArrayList<Float> yValues, ArrayList<Float> yValuesBackup, ArrayList<String> months,
                                    String durationAverage) {

        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float floatValue) {
                return String.format(Locale.getDefault(), "%.0f", floatValue);
            }
        };
//        leftAxis.setAxisMaxValue(leftAxisValue);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setValueFormatter(valueFormatter);

        setData(yValues, yValuesBackup, months, durationAverage);
    }

    private void setData(ArrayList<Float> yValues, ArrayList<Float> yValuesBackup, ArrayList<String> months,
                         String durationAverage) {

        ArrayList<BarEntry> MainGraphLine = new ArrayList<>();

        for (int i = 0; i < months.size(); i++) {
            MainGraphLine.add(new BarEntry(yValues.get(i), i));
        }

        ArrayList<Entry> userAvgGraphLine = new ArrayList<>();
        for (int i = 0; i < months.size(); i++) {
            userAvgGraphLine.add(new Entry(Float.parseFloat(durationAverage), i));
        }

        //set for main line graph
        BarDataSet set1 = new BarDataSet(MainGraphLine, "EB Consumption");
//        set1.setFillColor(color);
        set1.setColor(color);
        set1.setValueTextColor(Color.DKGRAY);
//        set1.setCircleColor(color);
//        set1.setLineWidth(2f);
//        set1.setCircleSize(4f);
//        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
//        set1.setFillAlpha(65);
//        set1.setDrawFilled(true);
        set1.setDrawValues(true);
        set1.setBarSpacePercent(50f);
//        set1.setDrawCubic(true);
//        set1.setCubicIntensity(0.2f);

        //data set for user average consumption
        LineDataSet set2 = new LineDataSet(userAvgGraphLine, "Your Average");
        set2.setColor(graph_line_user_average_color);
        set2.setLineWidth(2f);
        set2.setDrawCircleHole(false);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        set2.enableDashedLine(5, 5, 5);
        set2.setDrawCubic(true);
        set2.setCubicIntensity(0.2f);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
//        dataSets.add(set2);

        ArrayList<BarEntry> backUpGraphLine = new ArrayList<>();

        if (yValuesBackup != null && yValuesBackup.size() > 0) {

            for (int i = 0; i < months.size(); i++) {
                backUpGraphLine.add(new BarEntry(yValuesBackup.get(i), i));
            }
            //data set for unit (in township) average consumption
            BarDataSet set3 = new BarDataSet(backUpGraphLine, "DG Consumption");
            set3.setColor(graph_line_township_average_color);
//            set3.setFillColor(graph_line_township_average_color);
            set3.setColor(graph_line_township_average_color);
            set3.setValueTextColor(Color.DKGRAY);
//            set3.setCircleColor(graph_line_township_average_color);
//            set3.setLineWidth(2f);
//            set3.setCircleSize(4f);
//            set3.setDrawCircleHole(false);
            set3.setValueTextSize(9f);
//            set3.setFillAlpha(65);
//            set3.setDrawFilled(true);
//            set3.setDrawValues(true);
//            set3.setDrawCubic(true);
//            set3.setCubicIntensity(0.2f);

            dataSets.add(set3);
        }

        // create a data object with the data sets
        BarData data = new BarData(months, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        mChart.setData(data);
        mChart.animateX(1000, Easing.EasingOption.EaseInOutQuart);
        mChart.invalidate();

        Legend legend = mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

    }

    @Override
    public int getHeaderColor() {
        return color;
    }

    @Override
    public String getHeaderTitle() {
        return GlobalData.getInstance().getUpdatedUnitName();
    }
}
