package com.simplysmart.app.fragment;

import android.app.Activity;
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
import com.simplysmart.lib.config.ServiceGenerator;
import com.simplysmart.lib.endpint.DemoApiInterface;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.bots.Bot;
import com.simplysmart.lib.model.bots.BotData;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.bots.BotStatus;
import com.simplysmart.lib.request.ChototelCreateRequest;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by shekhar on 4/8/15.
 * Water Meter Screen.
 */
public class WaterMeterScreen extends BaseFragment {

    private Activity _activity;
    private View rootView;
    private Typeface iconTypeface;
    private TextView valBalance, valDaysLeft;
    private ProgressBarView progressBarView;
    private int val = 0;
    private ProgressBar progressBar;
    private LinearLayout content_layout;
    private TextView no_data_found;
    private TextView readingText, readingValue;
    private TextView daysLeft, daysLeftLabel;

    private String rupee_icon = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
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
        return ContextCompat.getColor(_activity, R.color.bg_color);
    }

    @Override
    public String getHeaderTitle() {
        return _activity.getString(R.string.title_water);
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

        readingText = (TextView) _activity.findViewById(R.id.readingText);
        readingValue = (TextView) _activity.findViewById(R.id.readingValue);

        valDaysLeft = (TextView) rootView.findViewById(R.id.val_days_left);
        valBalance = (TextView) rootView.findViewById(R.id.val_balance);

        progressBarView = (ProgressBarView) _activity.findViewById(R.id.bar1);
    }

    private void getWaterBotInfoRequest() {

        progressBar.setVisibility(View.VISIBLE);
        ChototelCreateRequest.getInstance().getBotDetails(GlobalData.getInstance().getDemoUnitId(), GlobalData.getInstance().getBookingId(), AppConstant.WATER_BOT, new ApiCallback<BotResponse>() {
            @Override
            public void onSuccess(BotResponse response) {
                progressBar.setVisibility(View.GONE);
                content_layout.setVisibility(View.VISIBLE);
                parseElectricityResponse(response);
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.VISIBLE);
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

            valBalance.setText(String.valueOf(bot.getConsumed_amount()));
            valDaysLeft.setText(String.valueOf(bot.getDays_left()));

            if (bot.getConsumed_units() != null && !bot.getConsumed_units().equalsIgnoreCase("")) {
                readingText.setText("Consumption");
                readingValue.setText(bot.getConsumed_units() + " Ltr");
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

            //Cal API for get water meter status
            if (NetworkUtilities.isInternet(_activity)) {
                getWaterBotInfoRequest();
                callBotStatusRequest("cutoff_status");
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
        Call<BotStatus> botStatusCall = apiInterface.botStatus(GlobalData.getInstance().getDemoUnitId(), AppConstant.WATER_BOT, request_for, AppSessionData.getInstance().getSubdomain(),AppSessionData.getInstance().getSite_id());

        botStatusCall.enqueue(new Callback<BotStatus>() {

            @Override
            public void onResponse(Call<BotStatus> call, retrofit2.Response<BotStatus> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    DebugLog.d("status.getMessage() : " + response.body().getMessage());
                    DebugLog.d("status.getCutoff() : " + response.body().getCutoff());
                    DebugLog.d("status.getBot_alive() : " + response.body().getBot_alive());
                    DebugLog.d("status.getCurrent_reading() : " + response.body().getCurrent_reading());
                }
            }

            @Override
            public void onFailure(Call<BotStatus> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}