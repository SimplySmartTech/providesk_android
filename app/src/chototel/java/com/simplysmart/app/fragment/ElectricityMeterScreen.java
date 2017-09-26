package com.simplysmart.app.fragment;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.simplysmart.lib.model.bots.Bot;
import com.simplysmart.lib.model.bots.BotData;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.request.ChototelCreateRequest;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shekhar on 4/8/15.
 * Electrical Meter Screen
 */
public class ElectricityMeterScreen extends BaseFragment {

    private View rootView;
    private Typeface iconTypeface;
    private TextView valBalance, valDaysLeft;
    private ProgressBarView progressBarView;
    private int val = 0;
    private ProgressBar progressBar;
    private LinearLayout content_layout;
    private TextView no_data_found;
    private TextView readingText, readingValue;

    private String rupee_icon = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bg_color);
    }

    @Override
    public String getHeaderTitle() {
        return _activity.getString(R.string.title_electricity);
    }

    private void initializeView() {

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        content_layout = (LinearLayout) rootView.findViewById(R.id.content_layout);

        no_data_found = (TextView) rootView.findViewById(R.id.no_data_found);

        valBalance = (TextView) rootView.findViewById(R.id.val_balance);
        valDaysLeft = (TextView) rootView.findViewById(R.id.val_days_left);
        readingValue = (TextView) rootView.findViewById(R.id.readingValue);

        readingText = (TextView) rootView.findViewById(R.id.readingText);
        TextView txtBalance = (TextView) rootView.findViewById(R.id.txt_balance);
        TextView txtDaysLeft = (TextView) rootView.findViewById(R.id.txt_days_left);

        progressBarView = (ProgressBarView) rootView.findViewById(R.id.bar1);

    }

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

    private void startCountAnimation(final TextView textView, double start, double stop, int duration) {

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues((int) start, (int) stop);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("" + (int) animation.getAnimatedValue());
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
        ChototelCreateRequest.getInstance().getBotDetails(GlobalData.getInstance().getDemoUnitId(),
                GlobalData.getInstance().getBookingId(),
                AppConstant.ELECTRICITY_BOT,
                new ApiCallback<BotResponse>() {
                    @Override
                    public void onSuccess(BotResponse response) {
                        progressBar.setVisibility(View.GONE);
                        content_layout.setVisibility(View.VISIBLE);
                        parsePrepaidResponse(response);
                    }

                    @Override
                    public void onFailure(String error) {
                        progressBar.setVisibility(View.GONE);
                        no_data_found.setVisibility(View.VISIBLE);
                        no_data_found.setText(error);
                    }
                }

        );
    }

    private void parsePrepaidResponse(BotResponse response) {

        progressBarView.setValue(0);
        progressBarView.invalidate();

        BotData data = response.getData();
        Bot bot = data.getBot();

        if (bot.isInfo_found()) {

            valBalance.setText(String.valueOf(bot.getConsumed_amount()));
            valDaysLeft.setText(String.valueOf(bot.getDays_left()));

            if (bot.getConsumed_units() != null && !bot.getConsumed_units().equalsIgnoreCase("")) {
                readingText.setText("Consumption");
                readingValue.setText(bot.getConsumed_units() + " KWh");
            }
        } else {
            content_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText((bot.getMessage() != null && !bot.getMessage().equalsIgnoreCase("") ? bot.getMessage() : "Sorry, no data found!"));
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
}
