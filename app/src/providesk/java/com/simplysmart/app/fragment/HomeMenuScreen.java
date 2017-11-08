package com.simplysmart.app.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.simplysmart.app.R;
import com.simplysmart.app.activity.HelpDeskScreenActivity;
import com.simplysmart.app.activity.NotificationScreenActivity;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.login.AccessPolicy;
import com.simplysmart.lib.model.login.Resident;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by shekhar on 4/8/15.
 * HomeMenuScreen works as a dashboard screen with all menu options.
 */
public class HomeMenuScreen extends BaseFragment {

    private Activity _activity;
    private ImageView logo;
    private RelativeLayout llParent;
    private LinearLayout poweredBy;

    private HashMap<String, ImageView> menuCLickList = new HashMap<>();
    private HashMap<String, View.OnClickListener> menuClickNameList = new HashMap<>();

    private Resident residentData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home_menu_screen, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Drawable headerIcon = getResources().getDrawable(R.drawable.header_bg__dashboard_bg);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(headerIcon);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        SharedPreferences UserInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonUnitInfo = UserInfo.getString("unit_info", "");
        residentData = gson.fromJson(jsonUnitInfo, Resident.class);

        initializeView();

        DebugLog.d("GlobalData.getInstance().getApiKey() : " + GlobalData.getInstance().getApiKey());
        DebugLog.d("GlobalData.getInstance().getAuthToken() : " + GlobalData.getInstance().getAuthToken());
    }

    @Override
    protected int getHeaderColor() {
        return 0;
    }

    @Override
    protected String getHeaderTitle() {
        return GlobalData.getInstance().getUpdatedUnitName();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(GlobalData.getInstance().getUpdatedUnitName());
        IntentFilter intentFilter = new IntentFilter("UnitName");
        LocalBroadcastManager.getInstance(_activity).registerReceiver(onUnitSelection, intentFilter);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(_activity).unregisterReceiver(onUnitSelection);
        super.onDestroy();
    }

    private void initializeView() {

        ImageView plannerButton = (ImageView) _activity.findViewById(R.id.menu_planner);
        ImageView helpdeskButton = (ImageView) _activity.findViewById(R.id.menu_helpdesk);
        ImageView notificationButton = (ImageView) _activity.findViewById(R.id.menu_notification);
        ImageView ewalletButton = (ImageView) _activity.findViewById(R.id.menu_ewallet);
        ImageView electricityMeterButton = (ImageView) _activity.findViewById(R.id.menu_electricity);
        ImageView waterMeterButton = (ImageView) _activity.findViewById(R.id.menu_water);

        //TODO: Kept it hardcoded for lodha parking demo
        if (residentData.getMobile().equalsIgnoreCase("9999999998")) {
            electricityMeterButton.setImageResource(R.drawable.shopping);
            waterMeterButton.setImageResource(R.drawable.amenities);
            plannerButton.setImageResource(R.drawable.parking);
        } else {
            electricityMeterButton.setImageResource(R.drawable.electricity);
            waterMeterButton.setImageResource(R.drawable.water);
            plannerButton.setImageResource(R.drawable.planner);
        }

        menuCLickList.put("payment", ewalletButton);
        menuCLickList.put("electricity", electricityMeterButton);
        menuCLickList.put("water", waterMeterButton);
        menuCLickList.put("notification", notificationButton);
        menuCLickList.put("helpdesk", helpdeskButton);
        menuCLickList.put("planner", plannerButton);

        menuClickNameList.put("payment", ewalletClick);
        menuClickNameList.put("electricity", electricityClick);
        menuClickNameList.put("water", waterClick);
        menuClickNameList.put("notification", notificationClick);
        menuClickNameList.put("helpdesk", helpdeskClick);
        menuClickNameList.put("planner", plannerClick);

        logo = (ImageView) _activity.findViewById(R.id.logo);
        llParent = (RelativeLayout) _activity.findViewById(R.id.llParent);

        poweredBy = (LinearLayout) _activity.findViewById(R.id.poweredBy);

        AccessPolicy policy = GlobalData.getInstance().getAccessPolicy();

        if (policy != null) {

            if (!AppSessionData.getInstance().getSubdomain().equalsIgnoreCase("demo")
                    && policy.getLogo_url() != null
                    && !policy.getLogo_url().equalsIgnoreCase("")) {

                Picasso.with(_activity).load(policy.getLogo_url())
                        .placeholder(R.drawable.loading_border_trans)
                        .error(R.drawable.loading_border_trans).into(logo);
                poweredBy.setVisibility(View.VISIBLE);
            }
            //TODO: Kept it hardcoded for lodha parking demo
            else if (residentData.getMobile().equalsIgnoreCase("9999999998")) {
                Picasso.with(_activity).load(R.drawable.lodha_chess_parking)
                        .placeholder(R.drawable.loading_border_trans)
                        .error(R.drawable.loading_border_trans).into(logo);
                poweredBy.setVisibility(View.VISIBLE);
            } else {
                logo.setImageResource(R.drawable.app_logo_main);
                poweredBy.setVisibility(View.GONE);
            }

            if (policy.getBackground() != null && !policy.getBackground().equalsIgnoreCase("")) {
                try {
                    llParent.setBackgroundColor(Color.parseColor(policy.getBackground()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //TODO: Kept it hardcoded for lodha parking demo
            else if (residentData.getMobile().equalsIgnoreCase("9999999998")) {
                llParent.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                llParent.setBackgroundResource(R.drawable.bw_dashboard_bg);
            }

            electricityMeterButton.setClickable(false);
            electricityMeterButton.setImageAlpha(50);
            electricityMeterButton.setOnClickListener(null);

            plannerButton.setClickable(false);
            plannerButton.setImageAlpha(50);
            plannerButton.setOnClickListener(null);

            ewalletButton.setClickable(false);
            ewalletButton.setImageAlpha(50);
            ewalletButton.setOnClickListener(null);

            waterMeterButton.setClickable(false);
            waterMeterButton.setImageAlpha(50);
            waterMeterButton.setOnClickListener(null);

            helpdeskButton.setClickable(false);
            helpdeskButton.setImageAlpha(50);
            helpdeskButton.setOnClickListener(null);

            if (GlobalData.getInstance().getAccessPolicy() != null) {

                AccessPolicy accessPolicy = GlobalData.getInstance().getAccessPolicy();

                if (accessPolicy.getMenu() != null && accessPolicy.getMenu().size() > 0) {

                    for (int i = 0; i < accessPolicy.getMenu().size(); i++) {

                        if (menuCLickList.get(accessPolicy.getMenu().get(i).getName().toLowerCase()) != null) {

                            menuCLickList.get(accessPolicy.getMenu().get(i).getName().toLowerCase())
                                    .setOnClickListener(menuClickNameList.get(accessPolicy.getMenu().get(i).getName().toLowerCase()));

                            menuCLickList.get(accessPolicy.getMenu().get(i).getName().toLowerCase()).setClickable(true);
                            menuCLickList.get(accessPolicy.getMenu().get(i).getName().toLowerCase()).setImageAlpha(255);
                        }
                    }
                }
            } else {
                electricityMeterButton.setOnClickListener(electricityClick);
                plannerButton.setOnClickListener(plannerClick);
                helpdeskButton.setOnClickListener(helpdeskClick);
                ewalletButton.setOnClickListener(ewalletClick);
                waterMeterButton.setOnClickListener(waterClick);
            }
        } else {
            electricityMeterButton.setOnClickListener(electricityClick);
            plannerButton.setOnClickListener(plannerClick);
            helpdeskButton.setOnClickListener(helpdeskClick);
            ewalletButton.setOnClickListener(ewalletClick);
            waterMeterButton.setOnClickListener(waterClick);

            logo.setImageResource(R.drawable.app_logo_main);
            poweredBy.setVisibility(View.GONE);
        }

        notificationButton.setOnClickListener(notificationClick);

    }

    private final View.OnClickListener plannerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //TODO: Kept it hardcoded for lodha parking demo
            if (!residentData.getMobile().equalsIgnoreCase("9999999998")) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new PlannerScreen(),
                        PlannerScreen.class.getSimpleName())
                        .addToBackStack(PlannerScreen.class.getSimpleName()).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new ParkingScreen(),
                        ParkingScreen.class.getSimpleName())
                        .addToBackStack(ParkingScreen.class.getSimpleName()).commit();
            }
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

            //TODO: Kept it hardcoded for lodha parking demo
            if (!residentData.getMobile().equalsIgnoreCase("9999999998")) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new ElectricityMeterScreen(),
                        ElectricityMeterScreen.class.getSimpleName())
                        .addToBackStack(ElectricityMeterScreen.class.getSimpleName()).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new ShoppingScreen(),
                        ShoppingScreen.class.getSimpleName())
                        .addToBackStack(ShoppingScreen.class.getSimpleName()).commit();
            }
        }
    };
    private final View.OnClickListener waterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //TODO: Kept it hardcoded for lodha parking demo
            if (!residentData.getMobile().equalsIgnoreCase("9999999998")) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new WaterMeterScreen(),
                        WaterMeterScreen.class.getSimpleName())
                        .addToBackStack(WaterMeterScreen.class.getSimpleName()).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new AmenitiesScreen(),
                        AmenitiesScreen.class.getSimpleName())
                        .addToBackStack(AmenitiesScreen.class.getSimpleName()).commit();
            }
        }
    };

    private final View.OnClickListener helpdeskClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            getFragmentManager().beginTransaction().replace(R.id.frame_container, new HelpDeskScreenOpen(),
//                    HelpDeskScreenOpen.class.getSimpleName())
//                    .addToBackStack(HelpDeskScreenOpen.class.getSimpleName()).commit();

            Intent newIntent = new Intent(getActivity(), HelpDeskScreenActivity.class);
            startActivity(newIntent);
        }
    };
    private final View.OnClickListener notificationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            getFragmentManager().beginTransaction().replace(R.id.frame_container, new NotificationScreenPhone(),
//                    NotificationScreenPhone.class.getSimpleName()).addToBackStack(null).commit();

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
