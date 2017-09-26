package com.simplysmart.app.fragment;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.simplysmart.app.R;
import com.simplysmart.app.activity.HelpDeskScreenActivity;
import com.simplysmart.app.activity.NotificationScreenActivity;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.model.login.AccessPolicy;

/**
 * Created by shekhar on 4/8/15.
 * HomeMenuScreen works as a dashboard screen with all menu options.
 */
public class HomeMenuScreen extends BaseFragment {

    private RelativeLayout llParent;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_menu_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        initializeView();

        DebugLog.d("GlobalData.getInstance().getApiKey() : " + GlobalData.getInstance().getApiKey());
        DebugLog.d("GlobalData.getInstance().getAuthToken() : " + GlobalData.getInstance().getAuthToken());
    }

    @Override
    protected int getHeaderColor() {
        return getActivity().getResources().getColor(R.color.bg_color);
    }

    @Override
    protected String getHeaderTitle() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(GlobalData.getInstance().getUpdatedUnitName());
        IntentFilter intentFilter = new IntentFilter("UnitName");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onUnitSelection, intentFilter);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onUnitSelection);
        super.onDestroy();
    }

    private void initializeView() {

        ImageView plannerButton = (ImageView) rootView.findViewById(R.id.menu_planner);
        ImageView helpdeskButton = (ImageView) rootView.findViewById(R.id.menu_helpdesk);
        ImageView notificationButton = (ImageView) rootView.findViewById(R.id.menu_notification);
        ImageView ewalletButton = (ImageView) rootView.findViewById(R.id.menu_ewallet);
        ImageView electricityMeterButton = (ImageView) rootView.findViewById(R.id.menu_electricity);
        ImageView waterMeterButton = (ImageView) rootView.findViewById(R.id.menu_water);

        llParent = (RelativeLayout) rootView.findViewById(R.id.llParent);

        AccessPolicy policy = GlobalData.getInstance().getAccessPolicy();

        if (policy != null) {

            electricityMeterButton.setOnClickListener(electricityClick);
            plannerButton.setOnClickListener(plannerClick);
            helpdeskButton.setOnClickListener(helpdeskClick);
            ewalletButton.setOnClickListener(ewalletClick);
            waterMeterButton.setOnClickListener(waterClick);

        } else {
            electricityMeterButton.setOnClickListener(electricityClick);
            plannerButton.setOnClickListener(plannerClick);
            helpdeskButton.setOnClickListener(helpdeskClick);
            ewalletButton.setOnClickListener(ewalletClick);
            waterMeterButton.setOnClickListener(waterClick);
        }
        notificationButton.setOnClickListener(notificationClick);
    }

    private final View.OnClickListener plannerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            getFragmentManager().beginTransaction().replace(R.id.frame_container, new PlannerScreen(),
                    PlannerScreen.class.getSimpleName())
                    .addToBackStack(PlannerScreen.class.getSimpleName()).commit();
        }
    };
    private final View.OnClickListener ewalletClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().beginTransaction().replace(R.id.frame_container, new EwalletScreen(),
                    EwalletScreen.class.getSimpleName()).addToBackStack(null).commit();
        }
    };
    private final View.OnClickListener electricityClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            getFragmentManager().beginTransaction().replace(R.id.frame_container, new ElectricityMeterScreen(),
                    ElectricityMeterScreen.class.getSimpleName())
                    .addToBackStack(ElectricityMeterScreen.class.getSimpleName()).commit();
        }
    };
    private final View.OnClickListener waterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().beginTransaction().replace(R.id.frame_container, new WaterMeterScreen(),
                    WaterMeterScreen.class.getSimpleName())
                    .addToBackStack(WaterMeterScreen.class.getSimpleName()).commit();
        }
    };

    private final View.OnClickListener helpdeskClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent newIntent = new Intent(getActivity(), HelpDeskScreenActivity.class);
            startActivity(newIntent);
        }
    };
    private final View.OnClickListener notificationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent newIntent = new Intent(getActivity(), NotificationScreenActivity.class);
            startActivity(newIntent);
        }
    };

    private final BroadcastReceiver onUnitSelection = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() == 0) {
                DebugLog.d("message received : " + intent.getStringExtra("name"));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(intent.getStringExtra("name"));
            }
        }
    };

}
