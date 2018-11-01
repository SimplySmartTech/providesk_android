package com.simplysmart.providesk.fragment;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.bots.Bot;
import com.simplysmart.lib.model.bots.BotData;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.bots.BotStatus;
import com.simplysmart.lib.model.bots.LastPayment;
import com.simplysmart.lib.request.CreateRequest;
import com.simplysmart.providesk.R;
import com.simplysmart.providesk.activity.ElectricityBillDetailsScreenActivity;
import com.simplysmart.providesk.config.AppConstant;
import com.simplysmart.providesk.config.GlobalData;
import com.simplysmart.providesk.custom.ProgressBarView;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by shekhar on 4/8/15.
 * Electrical Meter Screen
 */
public class ElectricityMeterScreen extends BaseFragment {

    private View rootView;
    private Typeface textTypeface;
    private Typeface iconTypeface;
    private TextView valBalance, valDaysLeft, txtDaysLeft, valUnitUsed, valDailyAvg;
    private ProgressBarView progressBarView;
    private int val = 0;
    private ProgressBar progressBar;
    private LinearLayout content_layout, content_layout_postpaid;
    private TextView no_data_found;
    private TextView button_demo, text_demo;
    private TextView messagePrepaid;
    private TextView readingText, readingValue;
    private BotResponse botResponse;


    //Postpaid widgets
    private TextView currentReadingIcon, currentReadingText, currentReadingValue;
    private TextView unbilledAmountIcon, unbilledAmountText, unbilledAmountValue;
    private TextView unitConsumedIcon, unitConsumedText, unitConsumedValue;
    private TextView latestBillText, latestBillValue, latestUnitsUsed;
    private TextView lastPaymentText, lastPaymentValue;

    private TextView daysLeft, daysLeftLabel;
    private TextView lastBillLabel, lastPaymentLabel;

    private TextView txt_postpaid_msg;

    private TextView billShowDetail;
    private String billId = "";

    private String rupee_icon = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        textTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
        iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);
        rupee_icon = _activity.getResources().getString(R.string.icon_rupee);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_electricity_meter_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeView();

        if (NetworkUtilities.isInternet(_activity)) {
            getElectricityInfoRequest();
//            callBotStatusRequest("cutoff_status");
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bw_color_orange);
    }

    @Override
    public String getHeaderTitle() {
        return GlobalData.getInstance().getUpdatedUnitName();
    }

    private void initializeView() {

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        content_layout = (LinearLayout) rootView.findViewById(R.id.content_layout);
        content_layout_postpaid = (LinearLayout) rootView.findViewById(R.id.content_layout_postpaid);

        no_data_found = (TextView) rootView.findViewById(R.id.no_data_found);

        button_demo = (TextView) rootView.findViewById(R.id.button_demo);
        text_demo = (TextView) rootView.findViewById(R.id.text_demo);

        readingText = (TextView) rootView.findViewById(R.id.readingText);
        readingValue = (TextView) rootView.findViewById(R.id.readingValue);

        messagePrepaid = (TextView) rootView.findViewById(R.id.messagePrepaid);

        ImageView btnUsageGraph = (ImageView) rootView.findViewById(R.id.btn_usage_graph);
        LinearLayout graphLayoutPostpaid = (LinearLayout) rootView.findViewById(R.id.graphLayoutPostpaid);

        valBalance = (TextView) rootView.findViewById(R.id.val_balance);
        valDaysLeft = (TextView) rootView.findViewById(R.id.val_days_left);
        TextView txtBalance = (TextView) rootView.findViewById(R.id.txt_balance);

        txtDaysLeft = (TextView) rootView.findViewById(R.id.txt_days_left);

        TextView txtUnitUsed = (TextView) rootView.findViewById(R.id.txt_unit_used);
        TextView txtDailyAvg = (TextView) rootView.findViewById(R.id.txt_daily_avg);
        TextView txtUsageGraph = (TextView) rootView.findViewById(R.id.txt_usage_graph);

        TextView txtUsageGraphPostpaid = (TextView) rootView.findViewById(R.id.txt_usage_graph_postpaid);

        valUnitUsed = (TextView) rootView.findViewById(R.id.val_unit_used);
        valDailyAvg = (TextView) rootView.findViewById(R.id.val_daily_avg);

        billShowDetail = (TextView) rootView.findViewById(R.id.billShowDetail);

        messagePrepaid.setTypeface(textTypeface);
        txtBalance.setTypeface(textTypeface);

        txtUnitUsed.setTypeface(textTypeface);
        txtDailyAvg.setTypeface(textTypeface);
        txtUsageGraph.setTypeface(textTypeface);
        txtUsageGraphPostpaid.setTypeface(textTypeface);

        txtDaysLeft.setTypeface(textTypeface);
        txtBalance.setTypeface(textTypeface);
        valDaysLeft.setTypeface(textTypeface);
        valBalance.setTypeface(textTypeface);

        valDailyAvg.setTypeface(textTypeface);
        valUnitUsed.setTypeface(textTypeface);

        readingText.setTypeface(textTypeface);
        readingValue.setTypeface(textTypeface);

        button_demo.setOnClickListener(demoButtonClick);
        btnUsageGraph.setOnClickListener(graphClick);
        graphLayoutPostpaid.setOnClickListener(graphClick);

        progressBarView = (ProgressBarView) _activity.findViewById(R.id.bar1);

        //Postpaid views
        currentReadingIcon = (TextView) _activity.findViewById(R.id.currentReadingIcon);
        currentReadingText = (TextView) _activity.findViewById(R.id.currentReadingText);
        currentReadingValue = (TextView) _activity.findViewById(R.id.currentReadingValue);

        unbilledAmountIcon = (TextView) _activity.findViewById(R.id.unbilledAmountIcon);
        unbilledAmountText = (TextView) _activity.findViewById(R.id.unbilledAmountText);
        unbilledAmountValue = (TextView) _activity.findViewById(R.id.unbilledAmountValue);

        unitConsumedIcon = (TextView) _activity.findViewById(R.id.unitConsumedIcon);
        unitConsumedText = (TextView) _activity.findViewById(R.id.unitConsumedText);
        unitConsumedValue = (TextView) _activity.findViewById(R.id.unitConsumedValue);

        daysLeft = (TextView) _activity.findViewById(R.id.daysLeft);
        daysLeftLabel = (TextView) _activity.findViewById(R.id.daysLeftLabel);

        lastBillLabel = (TextView) _activity.findViewById(R.id.lastBillLabel);
        lastPaymentLabel = (TextView) _activity.findViewById(R.id.lastPaymentLabel);

        latestBillText = (TextView) _activity.findViewById(R.id.latestBillText);
        latestBillValue = (TextView) _activity.findViewById(R.id.latestBillValue);
        latestUnitsUsed = (TextView) _activity.findViewById(R.id.latestUnitsUsed);

        lastPaymentText = (TextView) _activity.findViewById(R.id.lastPaymentText);
        lastPaymentValue = (TextView) _activity.findViewById(R.id.lastPaymentValue);

        txt_postpaid_msg = (TextView) _activity.findViewById(R.id.txt_postpaid_msg);

        currentReadingIcon.setTypeface(iconTypeface, Typeface.BOLD);
        unbilledAmountIcon.setTypeface(iconTypeface, Typeface.BOLD);
        unitConsumedIcon.setTypeface(iconTypeface, Typeface.BOLD);

        currentReadingText.setTypeface(textTypeface);
        currentReadingValue.setTypeface(textTypeface, Typeface.BOLD);

        unitConsumedText.setTypeface(textTypeface);
        unitConsumedValue.setTypeface(textTypeface, Typeface.BOLD);

        unbilledAmountText.setTypeface(textTypeface);
        unbilledAmountValue.setTypeface(textTypeface, Typeface.BOLD);

        latestBillText.setTypeface(textTypeface);
        latestBillValue.setTypeface(textTypeface);
        latestUnitsUsed.setTypeface(textTypeface);

        daysLeft.setTypeface(textTypeface, Typeface.BOLD);
        daysLeftLabel.setTypeface(textTypeface);

        lastBillLabel.setTypeface(textTypeface, Typeface.BOLD);
        lastPaymentLabel.setTypeface(textTypeface, Typeface.BOLD);

        lastPaymentText.setTypeface(textTypeface);
        lastPaymentValue.setTypeface(textTypeface);
        txt_postpaid_msg.setTypeface(textTypeface);

        billShowDetail.setOnClickListener(showBillDetail);
    }

    View.OnClickListener demoButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (button_demo.getText().toString().equalsIgnoreCase("recharge")) {

//            Cal API for recharge electricity
                if (NetworkUtilities.isInternet(_activity)) {
                    button_demo.setVisibility(View.VISIBLE);
                    text_demo.setVisibility(View.VISIBLE);
                    button_demo.setEnabled(false);
                    callBotStatusRequest("recharge");
                } else {
                    showMyDialog(_activity.getString(R.string.app_name), _activity.getString(R.string.error_no_internet_connection), "OK");
                }
            } else {

//                Cal API for cutoff electricity
                button_demo.setVisibility(View.VISIBLE);
                text_demo.setVisibility(View.VISIBLE);
                button_demo.setEnabled(false);
                animateBatteryMeter(progressBarView, 10);
                startCountAnimation(valBalance, Double.parseDouble(valBalance.getText().toString()), 0, ((10 * 300) - (progressBarView.getValue() * 100)), true);
                startCountAnimation(valDaysLeft, Double.parseDouble(valDaysLeft.getText().toString()), 0, (10 * 300) - (progressBarView.getValue() * 100), false);

                final FragmentManager fm = getFragmentManager();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (NetworkUtilities.isInternet(_activity)) {

                            if (fm.getBackStackEntryCount() > 0 && fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName() != null &&
                                    fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()
                                            .equalsIgnoreCase(ElectricityMeterScreen.class.getSimpleName())) {

                                callBotStatusRequest("cutoff");
                            }
                        } else {
                            showMyDialog(_activity.getString(R.string.app_name), _activity.getString(R.string.error_no_internet_connection), "OK");
                        }
                    }
                }, (10 * 300) - (progressBarView.getValue() * 100));
            }
        }
    };

    private final View.OnClickListener graphClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Pass the title to graph, to set proper color
            Bundle bundle = new Bundle();
            bundle.putString(REFERRER_SCREEN, getActivity().getString(R.string.title_electricity));
            Fragment graphScreen = new GraphScreen();
            graphScreen.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, graphScreen).addToBackStack(null).commit();
        }
    };

    private final View.OnClickListener showBillDetail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!billId.equalsIgnoreCase("")) {
                Intent newIntent = new Intent(getActivity(), ElectricityBillDetailsScreenActivity.class);
                newIntent.putExtra("billId", billId);
                startActivity(newIntent);
            } else {
                showSnackBar(content_layout, "Bill details not available.");
            }
        }
    };

    private void animateBatteryMeter(final ProgressBarView progressBarView, final int bar_limit) {

        int delay = 0;
        int period = 330;

        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                val = progressBarView.getValue();
                val++;

                if (val >= bar_limit) {
                    timer.cancel();
                } else {
                    if (val == 6) {
                        progressBarView.setBarColor1(ContextCompat.getColor(_activity, R.color.bw_color_yellow));
                    } else if (val == 8) {
                        progressBarView.setBarColor1(ContextCompat.getColor(_activity, R.color.bw_color_red));
                    }
                }
                LocalBroadcastManager.getInstance(_activity).sendBroadcast(new Intent("update"));
            }
        }, delay, period);

    }

    private void startCountAnimation(final TextView textView, double start, double stop, int duration, final boolean isDecimal) {

        ValueAnimator animator = new ValueAnimator();
        if (isDecimal) {
            animator.setFloatValues((float) start, (float) stop);
        } else {
            animator.setObjectValues((int) start, (int) stop);
        }
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isDecimal) {
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    String output = formatter.format(animation.getAnimatedValue());

                    try {
                        if (output.contains("-")) {
                            textView.setText("-" + (output.substring(2)).replace(",", ""));
                        } else {
                            textView.setText((output.substring(1)).replace(",", ""));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        textView.setText("0");
                    }
                } else {
                    textView.setText("" + (int) animation.getAnimatedValue());
                }
            }
        });
        animator.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter iff = new IntentFilter("update");
        LocalBroadcastManager.getInstance(_activity).registerReceiver(onNotice, iff);

        IntentFilter intentFilter = new IntentFilter("UnitName");
        LocalBroadcastManager.getInstance(_activity).registerReceiver(onUnitIDSelection, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(_activity).unregisterReceiver(onNotice);
        LocalBroadcastManager.getInstance(_activity).unregisterReceiver(onUnitIDSelection);
    }

    private void getElectricityInfoRequest() {

        progressBar.setVisibility(View.VISIBLE);
        no_data_found.setVisibility(View.GONE);
        CreateRequest.getInstance().getBotDetails(GlobalData.getInstance().getDemoUnitId(), AppConstant.ELECTRICITY_BOT, new ApiCallback<BotResponse>() {
            @Override
            public void onSuccess(BotResponse response) {
                progressBar.setVisibility(View.GONE);

                if (response.getData().getBot().getMode() != null && response.getData().getBot().getMode().equalsIgnoreCase("postpaid")) {
                    content_layout.setVisibility(View.GONE);
                    content_layout_postpaid.setVisibility(View.VISIBLE);

                    parsePostpaidResponse(response);

                } else {
                    content_layout.setVisibility(View.VISIBLE);
                    content_layout_postpaid.setVisibility(View.GONE);

                    botResponse = response;
                    parsePrepaidResponse(response);
                }
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.VISIBLE);
                no_data_found.setText(error);
            }
        });
    }

    private void parsePostpaidResponse(BotResponse response) {
        BotData data = response.getData();
        Bot bot = data.getBot();

        if (bot.isInfo_found()) {

            if (bot.getCurrent_meter_reading() != null && !bot.getCurrent_meter_reading().isEmpty()) {
                currentReadingValue.setText(bot.getCurrent_meter_reading() + " kwh");
            }

            if (bot.getUnits_consumed() != null && !bot.getUnits_consumed().isEmpty()) {
                unitConsumedValue.setText(bot.getUnits_consumed() + " kwh");
            }

            if (bot.getUnbilled_amount() != null && !bot.getUnbilled_amount().isEmpty()) {
                unbilledAmountValue.setText(rupee_icon + bot.getUnbilled_amount());
                unbilledAmountValue.setTypeface(iconTypeface, Typeface.BOLD);
            }

            if (bot.getDays_remaining() != null && !bot.getDays_remaining().isEmpty()) {
                daysLeft.setText(bot.getDays_remaining() + " Days");
            }

            if (bot.getLast_bill() != null) {

                LastPayment lastBill = bot.getLast_bill();

                billId = lastBill.getId();

                latestBillValue.setText(rupee_icon + lastBill.getAmount());
                latestBillValue.setTypeface(iconTypeface, Typeface.BOLD);

                latestBillText.setText(lastBill.getGenerated_at());
                latestUnitsUsed.setText(lastBill.getUnits_consumed() + " kwh units consumed last month");
            }

            if (bot.getLast_payment() != null) {

                LastPayment lastPayment = bot.getLast_payment();

                lastPaymentValue.setText(rupee_icon + lastPayment.getAmount());
                lastPaymentValue.setTypeface(iconTypeface, Typeface.BOLD);

                lastPaymentText.setText(lastPayment.getGenerated_at());
            }

            if (bot.getMessage() != null) {
                txt_postpaid_msg.setText(bot.getMessage());
            }
        }
    }

    private void parsePrepaidResponse(BotResponse response) {

        progressBarView.setValue(0);
        progressBarView.invalidate();

        BotData data = response.getData();
        Bot bot = data.getBot();

        if (bot.isInfo_found()) {

            content_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            if (AppSessionData.getInstance().getSubdomain().equalsIgnoreCase("demo")) {

                if (bot.isCutoff()) {
                    button_demo.setVisibility(View.VISIBLE);
                    text_demo.setVisibility(View.VISIBLE);
                    button_demo.setEnabled(true);
                    button_demo.setText("RECHARGE");
                    button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_dark_green));
                    button_demo.setTextColor(_activity.getResources().getColor(R.color.bw_color_white));

                    valBalance.setText("0");
                    valDaysLeft.setText("0");
                    valUnitUsed.setText("0");
                    progressBarView.setValue(10);
                    progressBarView.invalidate();

                } else {
                    button_demo.setVisibility(View.VISIBLE);
                    text_demo.setVisibility(View.VISIBLE);
                    button_demo.setEnabled(true);
                    button_demo.setText("CUTOFF");
                    button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_red));
                    button_demo.setTextColor(_activity.getResources().getColor(R.color.bw_color_white));

                    valUnitUsed.setText(bot.getUnits_remaining() + " Kwh");

                    double battery_bar_balance = (Math.round(Double.parseDouble(bot.getUnits_remaining()) / Double.parseDouble(bot.getTotal_available_units()) * 10));

                    DebugLog.d("battery_bar_balance : " + battery_bar_balance);

                    int bar_limit = 10 - (int) battery_bar_balance;
                    int period = 330;

                    animateBatteryMeter(progressBarView, bar_limit);

                    startCountAnimation(valBalance, Double.parseDouble(bot.getBalance()), Double.parseDouble(bot.getBalance_remaining()), period * bar_limit, true);
                    startCountAnimation(valDaysLeft, Double.parseDouble(bot.getTotal_available_days()), Double.parseDouble(bot.getDays_remaining()), period * bar_limit, false);

                    if (bot.getCurrent_meter_reading() != null && !bot.getCurrent_meter_reading().equalsIgnoreCase("")) {
                        readingText.setText("Current Reading ");
                        readingValue.setText(bot.getCurrent_meter_reading() + " KWh");
                    }

                    if (bot.getMessage() != null) {
                        messagePrepaid.setText(bot.getMessage());
                    }
                    valDailyAvg.setText(bot.getDaily_average() + " Kwh");
                }
            } else {
                valUnitUsed.setText(bot.getUnits_remaining() + " Kwh");

                double battery_bar_balance = (Math.round(Double.parseDouble(bot.getUnits_remaining()) / Double.parseDouble(bot.getTotal_available_units()) * 10));

                DebugLog.d("battery_bar_balance : " + battery_bar_balance);

                int bar_limit = 10 - (int) battery_bar_balance;
                int period = 330;

                animateBatteryMeter(progressBarView, bar_limit);

                startCountAnimation(valBalance, Double.parseDouble(bot.getBalance()), Double.parseDouble(bot.getBalance_remaining()), period * bar_limit, true);
                startCountAnimation(valDaysLeft, Double.parseDouble(bot.getTotal_available_days()), Double.parseDouble(bot.getDays_remaining()), period * bar_limit, false);

                if (bot.getCurrent_meter_reading() != null && !bot.getCurrent_meter_reading().equalsIgnoreCase("")) {
                    readingText.setText("Current Reading ");
                    readingValue.setText(bot.getCurrent_meter_reading() + " KWh");
                }

                if (bot.getMessage() != null) {
                    messagePrepaid.setText(bot.getMessage());
                }
                valDailyAvg.setText(bot.getDaily_average() + " Kwh");
            }

            if (bot.isShow_days_remaining()) {
                valDaysLeft.setVisibility(View.VISIBLE);
                txtDaysLeft.setVisibility(View.VISIBLE);
                valDaysLeft.setText(bot.getTotal_available_days());
            } else {
                valDaysLeft.setVisibility(View.INVISIBLE);
                txtDaysLeft.setVisibility(View.INVISIBLE);
            }

        } else {
            content_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText("SORRY, UNABLE TO CONNECT!");
        }
    }

    private final BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBarView.setValue(val);
            progressBarView.invalidate();
        }
    };

    private final BroadcastReceiver onUnitIDSelection = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            DebugLog.d("message received unit name: " + intent.getStringExtra("name"));
            DebugLog.d("message received unit id: " + intent.getStringExtra("id"));

            //Cal API for get electricity meter status
            if (NetworkUtilities.isInternet(_activity)) {
                getElectricityInfoRequest();
            } else {
                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.VISIBLE);
                no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
            }
        }
    };

    private void callBotStatusRequest(String request_for) {

//        progressBar.setVisibility(View.VISIBLE);
//        no_data_found.setVisibility(View.GONE);
//
//        DemoApiInterface apiInterface = ServiceGenerator.createService(DemoApiInterface.class);
//        Call<BotStatus> botStatusCall = apiInterface.botStatus(
//                GlobalData.getInstance().getDemoUnitId(),
//                AppConstant.ELECTRICITY_BOT, request_for,
//                AppSessionData.getInstance().getSubdomain(),
//                AppSessionData.getInstance().getSite_id());
//
//        botStatusCall.enqueue(new Callback<BotStatus>() {
//
//            @Override
//            public void onResponse(Call<BotStatus> call, retrofit2.Response<BotStatus> response) {
//
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    DebugLog.d("status.getMessage() : " + response.body().getMessage());
//                    DebugLog.d("status.getCutoff() : " + response.body().getCutoff());
//                    DebugLog.d("status.getBot_alive() : " + response.body().getBot_alive());
//                    DebugLog.d("status.getCurrent_reading() : " + response.body().getCurrent_reading());
//
//                    updateUI(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BotStatus> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }

    private void updateUI(BotStatus status) {

        if (status.getMessage() != null && status.getMessage().equalsIgnoreCase("Recharge successful")) {

            if (botResponse != null) {
                parsePrepaidResponse(botResponse);
            } else {
                getElectricityInfoRequest();
            }
            button_demo.setVisibility(View.VISIBLE);
            text_demo.setVisibility(View.VISIBLE);
            button_demo.setEnabled(true);
            button_demo.setText("CUTOFF");
            button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_red));

            if (status.getCurrent_reading() != null && !status.getCurrent_reading().equalsIgnoreCase("")) {
                readingText.setText("Current Reading ");
                readingValue.setText(status.getCurrent_reading() + " KWh");
            }

        } else if (status.getMessage() != null && status.getMessage().equalsIgnoreCase("Cutoff successful")) {
            button_demo.setVisibility(View.VISIBLE);
            text_demo.setVisibility(View.VISIBLE);
            button_demo.setEnabled(true);
            button_demo.setText("RECHARGE");
            button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_dark_green));

            if (status.getCurrent_reading() != null && !status.getCurrent_reading().equalsIgnoreCase("")) {
                readingText.setText("Current Reading ");
                readingValue.setText(status.getCurrent_reading() + " KWh");
            }

            valBalance.setText("0");
            valUnitUsed.setText("0");
            valDaysLeft.setText("0");
            progressBarView.setBarColor1(_activity.getResources().getColor(R.color.bw_color_green));
            progressBarView.setValue(10);
            progressBarView.invalidate();

            content_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        } else if (status.getBot_alive() != null && status.getBot_alive().equalsIgnoreCase("false")) {

            content_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText("SORRY, UNABLE TO CONNECT!");

        } else if (status.getCutoff() != null && status.getCutoff().equalsIgnoreCase("true")) {
            button_demo.setVisibility(View.VISIBLE);
            text_demo.setVisibility(View.VISIBLE);
            button_demo.setEnabled(true);
            button_demo.setText("RECHARGE");
            button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_dark_green));
            button_demo.setTextColor(_activity.getResources().getColor(R.color.bw_color_white));

            if (status.getCurrent_reading() != null && !status.getCurrent_reading().equalsIgnoreCase("")) {
                readingText.setText("Current Reading ");
                readingValue.setText(status.getCurrent_reading() + " KWh");
            }

            valBalance.setText("0");
            valDaysLeft.setText("0");
            valUnitUsed.setText("0");
            progressBarView.setValue(10);
            progressBarView.invalidate();

            content_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        } else if (status.getCutoff() != null && status.getCutoff().equalsIgnoreCase("false")) {

            if (botResponse != null) {
                progressBarView.setBarColor1(ContextCompat.getColor(_activity, R.color.bw_color_green));
                parsePrepaidResponse(botResponse);
            } else {
                getElectricityInfoRequest();
            }
            button_demo.setVisibility(View.VISIBLE);
            text_demo.setVisibility(View.VISIBLE);
            button_demo.setEnabled(true);
            button_demo.setText("CUTOFF");
            button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_red));
            button_demo.setTextColor(_activity.getResources().getColor(R.color.bw_color_white));

            if (status.getCurrent_reading() != null && !status.getCurrent_reading().equalsIgnoreCase("")) {
                readingText.setText("Current Reading ");
                readingValue.setText(status.getCurrent_reading() + " KWh");
            }
        }
    }

}
