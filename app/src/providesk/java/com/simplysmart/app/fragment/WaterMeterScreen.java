package com.simplysmart.app.fragment;

import android.animation.ValueAnimator;
import android.app.Activity;
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

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.app.custom.ProgressBarView;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.config.ServiceGenerator;
import com.simplysmart.lib.endpint.DemoApiInterface;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.bots.Bot;
import com.simplysmart.lib.model.bots.BotData;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.bots.BotStatus;
import com.simplysmart.lib.request.CreateRequest;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by shekhar on 4/8/15.
 * Water Meter Screen.
 */
public class WaterMeterScreen extends BaseFragment {

    private Activity _activity;
    private View rootView;
    private Typeface textTypeface;
    private Typeface iconTypeface;
    private TextView valBalance, valDaysLeft, valUnitUsed, valDailyAvg;
    private ProgressBarView progressBarView;
    private int val = 0;
    private ProgressBar progressBar;
    private LinearLayout content_layout, content_layout_postpaid;
    private TextView no_data_found;
    private TextView button_demo, text_demo;
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

    private String rupee_icon = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
        textTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
        iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);
        rupee_icon = _activity.getResources().getString(R.string.icon_rupee);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_water_meter_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeView();

        if (NetworkUtilities.isInternet(_activity)) {
            getWaterBotInfoRequest();
//            callBotStatusRequest("cutoff_status");
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bw_color_blue);
    }

    @Override
    public String getHeaderTitle() {
        return GlobalData.getInstance().getUpdatedUnitName();
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


    private void initializeView() {

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        no_data_found = (TextView) _activity.findViewById(R.id.no_data_found);

        content_layout = (LinearLayout) rootView.findViewById(R.id.content_layout);
        content_layout_postpaid = (LinearLayout) rootView.findViewById(R.id.content_layout_postpaid);

        button_demo = (TextView) _activity.findViewById(R.id.button_demo);
        text_demo = (TextView) _activity.findViewById(R.id.text_demo);

        readingText = (TextView) _activity.findViewById(R.id.readingText);
        readingValue = (TextView) _activity.findViewById(R.id.readingValue);

        ImageView btnUsageGraph = (ImageView) rootView.findViewById(R.id.btn_usage_graph);
        LinearLayout graphLayoutPostpaid = (LinearLayout) _activity.findViewById(R.id.graphLayoutPostpaid);

        TextView notificationMessage = (TextView) rootView.findViewById(R.id.txt_notification_msg);

        TextView txtBalance = (TextView) rootView.findViewById(R.id.txt_balance);
        TextView txtDaysLeft = (TextView) rootView.findViewById(R.id.txt_days_left);

        TextView txtUnitUsed = (TextView) rootView.findViewById(R.id.txt_unit_used);
        TextView txtDailyAvg = (TextView) rootView.findViewById(R.id.txt_daily_avg);
        TextView txtUsageGraph = (TextView) rootView.findViewById(R.id.txt_usage_graph);

        valDaysLeft = (TextView) rootView.findViewById(R.id.val_days_left);
        valBalance = (TextView) rootView.findViewById(R.id.val_balance);
        valUnitUsed = (TextView) rootView.findViewById(R.id.val_unit_used);
        valDailyAvg = (TextView) rootView.findViewById(R.id.val_daily_avg);

        notificationMessage.setTypeface(textTypeface);
        txtBalance.setTypeface(textTypeface);

        txtUnitUsed.setTypeface(textTypeface);
        txtDailyAvg.setTypeface(textTypeface);
        txtUsageGraph.setTypeface(textTypeface);

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


    }

    View.OnClickListener demoButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (button_demo.getText().toString().equalsIgnoreCase("recharge")) {

                //Cal API for recharge electricity
                if (NetworkUtilities.isInternet(_activity)) {
                    button_demo.setVisibility(View.VISIBLE);
                    text_demo.setVisibility(View.VISIBLE);
                    button_demo.setEnabled(false);
                    callBotStatusRequest("recharge");
                } else {
                    showMyDialog(_activity.getString(R.string.app_name), _activity.getString(R.string.error_no_internet_connection), "OK");
                }
            } else {

                //Cal API for cutoff electricity
                button_demo.setVisibility(View.VISIBLE);
                text_demo.setVisibility(View.VISIBLE);
                button_demo.setEnabled(false);
                animateBatteryMeter(progressBarView, 10);
                startCountAnimation(valBalance, Integer.parseInt(valBalance.getText().toString()), 0, ((10 * 330) - (progressBarView.getValue() * 330)));
                startCountAnimation(valDaysLeft, Integer.parseInt(valDaysLeft.getText().toString()), 0, (10 * 330) - (progressBarView.getValue() * 330));

                final FragmentManager fm = getFragmentManager();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (NetworkUtilities.isInternet(_activity)) {

                            if (fm.getBackStackEntryCount() > 0 && fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName() != null &&
                                    fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()
                                            .equalsIgnoreCase(WaterMeterScreen.class.getSimpleName())) {

                                callBotStatusRequest("cutoff");
                            }
                        } else {
                            showMyDialog(_activity.getString(R.string.app_name), getString(R.string.error_no_internet_connection), "OK");
                        }
                    }
                }, (10 * 330) - (progressBarView.getValue() * 330));
            }
        }
    };

    private final View.OnClickListener graphClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Pass the title to graph, to set proper color
            Bundle bundle = new Bundle();
            bundle.putString(REFERRER_SCREEN, _activity.getString(R.string.title_water));
            Fragment graphScreen = new GraphScreen();
            graphScreen.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, graphScreen).addToBackStack(null).commit();
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
                }
                LocalBroadcastManager.getInstance(_activity).sendBroadcast(new Intent("update"));
            }
        }, delay, period);

    }

    private void startCountAnimation(final TextView textView, double start, double stop, int duration) {

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues((int) start, (int) stop);
        if (duration < 0) {
            animator.setDuration(0);
        } else {
            animator.setDuration(duration);
        }

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("" + (int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void getWaterBotInfoRequest() {

        progressBar.setVisibility(View.VISIBLE);
        CreateRequest.getInstance().getBotDetails(GlobalData.getInstance().getDemoUnitId(), AppConstant.WATER_BOT, new ApiCallback<BotResponse>() {
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
                    parseElectricityResponse(response);
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
                currentReadingValue.setText(bot.getCurrent_meter_reading() + " ltr");
            }

            if (bot.getUnits_consumed() != null && !bot.getUnits_consumed().isEmpty()) {
                unitConsumedValue.setText(bot.getUnits_consumed() + " ltr");
            }

            if (bot.getUnbilled_amount() != null && !bot.getUnbilled_amount().isEmpty()) {
                unbilledAmountValue.setText(rupee_icon + bot.getUnbilled_amount());
                unbilledAmountValue.setTypeface(iconTypeface, Typeface.BOLD);
            }

            if (bot.getDays_remaining() != null && !bot.getDays_remaining().isEmpty()) {
                daysLeft.setText(bot.getDays_remaining() + " Days");
            }

            if (bot.getLast_bill() != null) {
                latestBillValue.setText(rupee_icon + bot.getLast_bill().getAmount());
                latestBillValue.setTypeface(iconTypeface, Typeface.BOLD);

                latestBillText.setText(bot.getLast_bill().getGenerated_at());
                latestUnitsUsed.setText(bot.getLast_bill().getUnits_consumed() + " ltr water consumed last month");
            }

            if (bot.getLast_payment() != null) {
                lastPaymentValue.setText(rupee_icon + bot.getLast_payment().getAmount());
                lastPaymentValue.setTypeface(iconTypeface, Typeface.BOLD);

                lastPaymentText.setText(bot.getLast_payment().getGenerated_at());
            }

            if (bot.getMessage() != null) {
                txt_postpaid_msg.setText(bot.getMessage());
            }
        }
    }

    private void getWaterBotInfoRequestDummy() {

        progressBar.setVisibility(View.VISIBLE);
        CreateRequest.getInstance().getBotDetails(GlobalData.getInstance().getDemoUnitId(), AppConstant.WATER_BOT, new ApiCallback<BotResponse>() {
            @Override
            public void onSuccess(BotResponse response) {
                progressBar.setVisibility(View.GONE);
                content_layout.setVisibility(View.VISIBLE);
                botResponse = response;
                valDailyAvg.setText(response.getData().getBot().getDaily_average() + "L");
                valBalance.setText("0");

                parseElectricityResponse(response);
                botResponse = response;
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                no_data_found.setText(error);
            }
        });
    }

    private void parseElectricityResponse(BotResponse response) {

        progressBarView.setValue(0);
        progressBarView.invalidate();

        BotData data = response.getData();
        Bot bot = data.getBot();

        if (bot.isInfo_found()) {
            valBalance.setText(bot.getBalance());
            valDaysLeft.setText(bot.getTotal_available_days());
            valUnitUsed.setText(bot.getUnits_remaining());
            valDailyAvg.setText(bot.getDaily_average() + "L");

            double water_bar_balance = (Math.round(Double.parseDouble(bot.getUnits_remaining()) / Double.parseDouble(bot.getTotal_available_units()) * 10));

            int bar_limit = 10 - (int) water_bar_balance;
            int period = 330;

            animateBatteryMeter(progressBarView, bar_limit);

            startCountAnimation(valBalance, Double.parseDouble(bot.getBalance()),
                    Double.parseDouble(bot.getBalance_remaining()), period * bar_limit);

            startCountAnimation(valDaysLeft, Double.parseDouble(bot.getTotal_available_days()),
                    Double.parseDouble(bot.getDays_remaining()), period * bar_limit);

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

            //Cal API for get water meter status
            if (NetworkUtilities.isInternet(_activity)) {
                getWaterBotInfoRequest();
            } else {
                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.VISIBLE);
                no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
            }
        }
    };

    private void callBotStatusRequest(String request_for) {

        progressBar.setVisibility(View.VISIBLE);
        no_data_found.setVisibility(View.GONE);

        DemoApiInterface apiInterface = ServiceGenerator.createService(DemoApiInterface.class);
        Call<BotStatus> botStatusCall = apiInterface.botStatus(
                GlobalData.getInstance().getDemoUnitId(),
                AppConstant.WATER_BOT,
                request_for,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        botStatusCall.enqueue(new Callback<BotStatus>() {

            @Override
            public void onResponse(Call<BotStatus> call, retrofit2.Response<BotStatus> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    DebugLog.d("status.getMessage() : " + response.body().getMessage());
                    DebugLog.d("status.getCutoff() : " + response.body().getCutoff());
                    DebugLog.d("status.getBot_alive() : " + response.body().getBot_alive());
                    DebugLog.d("status.getCurrent_reading() : " + response.body().getCurrent_reading());

                    updateUI(response.body());
                }
            }

            @Override
            public void onFailure(Call<BotStatus> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateUI(BotStatus status) {

        if (status.getMessage() != null && status.getMessage().equalsIgnoreCase("Recharge successful")) {

            if (botResponse != null) {
                parseElectricityResponse(botResponse);
            } else {
                getWaterBotInfoRequestDummy();
            }
            button_demo.setVisibility(View.VISIBLE);
            text_demo.setVisibility(View.VISIBLE);
            button_demo.setEnabled(true);
            button_demo.setText("CUTOFF");
            button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_red));

            if (status.getCurrent_reading() != null && !status.getCurrent_reading().equalsIgnoreCase("")) {
                readingText.setText("Current Reading ");
                readingValue.setText(status.getCurrent_reading() + " Ltr");
            }

        } else if (status.getMessage() != null && status.getMessage().equalsIgnoreCase("Cutoff successful")) {
            button_demo.setVisibility(View.VISIBLE);
            text_demo.setVisibility(View.VISIBLE);
            button_demo.setEnabled(true);
            button_demo.setText("RECHARGE");
            button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_dark_green));

            if (status.getCurrent_reading() != null && !status.getCurrent_reading().equalsIgnoreCase("")) {
                readingText.setText("Current Reading ");
                readingValue.setText(status.getCurrent_reading() + " Ltr");
            }

            valBalance.setText("0");
            valUnitUsed.setText("0");
            valDaysLeft.setText("0");
            progressBarView.setBarColor1(_activity.getResources().getColor(R.color.bw_color_blue));
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
                readingValue.setText(status.getCurrent_reading() + " Ltr");
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
                progressBarView.setBarColor1(ContextCompat.getColor(_activity, R.color.bw_color_blue));
                parseElectricityResponse(botResponse);
            } else {
                getWaterBotInfoRequestDummy();
            }
            button_demo.setVisibility(View.VISIBLE);
            text_demo.setVisibility(View.VISIBLE);
            button_demo.setEnabled(true);
            button_demo.setText("CUTOFF");
            button_demo.setBackgroundColor(_activity.getResources().getColor(R.color.bw_color_red));
            button_demo.setTextColor(_activity.getResources().getColor(R.color.bw_color_white));

            if (status.getCurrent_reading() != null && !status.getCurrent_reading().equalsIgnoreCase("")) {
                readingText.setText("Current Reading ");
                readingValue.setText(status.getCurrent_reading() + " Ltr");
            }
        }
    }
}