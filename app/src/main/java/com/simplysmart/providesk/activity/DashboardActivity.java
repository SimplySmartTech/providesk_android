package com.simplysmart.providesk.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.login.AccessPolicy;
import com.simplysmart.lib.model.login.Resident;
import com.simplysmart.lib.request.CreateRequest;
import com.simplysmart.providesk.R;
import com.simplysmart.providesk.adapter.CallPagerAdapter;
import com.simplysmart.providesk.adapter.DrawerAdapter;
import com.simplysmart.providesk.config.AppConstant;
import com.simplysmart.providesk.config.GlobalData;
import com.simplysmart.providesk.dialog.AlertDialogLogout;
import com.simplysmart.providesk.dialog.AlertDialogUpdateVersion;
import com.simplysmart.providesk.fragment.ElectricityMeterScreen;
import com.simplysmart.providesk.fragment.EwalletScreen;
import com.simplysmart.providesk.fragment.HomeMenuScreen;
import com.simplysmart.providesk.fragment.PlannerScreen;
import com.simplysmart.providesk.fragment.SensorInfoList;
import com.simplysmart.providesk.fragment.SensorInfoTodayList;
import com.simplysmart.providesk.fragment.WaterMeterScreen;
import com.simplysmart.providesk.gcm.QuickstartPreferences;
import com.simplysmart.providesk.model.Items;
import com.simplysmart.providesk.services.FetchCategories;
import com.simplysmart.providesk.util.VersionComprator;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shekhar on 4/8/15.
 */
public class DashboardActivity extends AppCompatActivity {

    public static final String TAG = "Payment Status";

    public static DrawerLayout mDrawerLayout;
    private ListView mDrawerList = null;
    private CircleImageView profilePhoto;
    private TextView header_unit_name, header_unit_icon, header_arrow_icon, footer_sign_out, footer_item_icon, profileText;
    private boolean isAccountMenu = false;
    private String[] unitArray;
    private String[] menuArray;
    private String[] iconArray;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean isSensorTabAdded = false;
    private HashMap<String, Fragment> menuCLickList = new HashMap<>();
    private ViewPager mPager;

    private boolean isRunning = true;

    private boolean updatePaymentBroadcastFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        if (NetworkUtilities.isInternet(this)) {
            isRunning = true;
            GetVersionCode getVersionCode = new GetVersionCode();
            getVersionCode.execute();
        }

        menuCLickList.put("home", new HomeMenuScreen());
        menuCLickList.put("payment", new EwalletScreen());
        menuCLickList.put("electricity", new ElectricityMeterScreen());
        menuCLickList.put("water", new WaterMeterScreen());
        menuCLickList.put("notification", null);
        menuCLickList.put("helpdesk", null);
        menuCLickList.put("planner", new PlannerScreen());
        menuCLickList.put("sensors_dummy", null);

        getUserInfo();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface textTypeface = Typeface.createFromAsset(getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                header_arrow_icon.setText(getString(R.string.icon_bottom_arrow));
                isAccountMenu = false;
                setNavigationMenu(menuArray, iconArray);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle.syncState();

        //Add Header & Footer view to Navigation Drawer.
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.side_navigation_header, mDrawerList, false);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.side_navigation_footer, mDrawerList, false);

        mDrawerList.addHeaderView(header, null, true);
        mDrawerList.addFooterView(footer, null, true);

        header_unit_name = ((TextView) header.findViewById(R.id.header_unit_name));
        footer_sign_out = ((TextView) footer.findViewById(R.id.footer_sign_out));
        header_unit_icon = ((TextView) header.findViewById(R.id.header_unit_icon));
        footer_item_icon = ((TextView) footer.findViewById(R.id.footer_item_icon));
        header_arrow_icon = ((TextView) header.findViewById(R.id.header_arrow_icon));
        profilePhoto = ((CircleImageView) header.findViewById(R.id.profilePhoto));
        profileText = (TextView) header.findViewById(R.id.profileText);

        header_unit_name.setTypeface(textTypeface);
        footer_sign_out.setTypeface(textTypeface);

        if (GlobalData.getInstance().getUnits() != null && GlobalData.getInstance().getUnits().size() > 0) {
            GlobalData.getInstance().setUpdatedUnitName(GlobalData.getInstance().getUnits().get(0).getInfo());
            GlobalData.getInstance().setDemoUnitId(GlobalData.getInstance().getUnits().get(0).getId());
        }
        getSupportActionBar().setTitle(GlobalData.getInstance().getUpdatedUnitName());

        header_unit_name.setText(GlobalData.getInstance().getUpdatedUnitName());

        Typeface iconTypeface = Typeface.createFromAsset(getAssets(), AppConstant.FONT_BOTSWORTH);
        header_unit_icon.setTypeface(iconTypeface);
        header_arrow_icon.setTypeface(iconTypeface);
        footer_item_icon.setTypeface(iconTypeface);

        header_unit_name.setOnClickListener(unitSelectionListener);
        header_unit_icon.setOnClickListener(unitSelectionListener);

        if (AppSessionData.getInstance().getSubdomain() != null && AppSessionData.getInstance().getSubdomain().equalsIgnoreCase("chototel")) {
            profilePhoto.setOnClickListener(unitSelectionListener);
            profileText.setVisibility(View.INVISIBLE);
        } else {
            if (GlobalData.getInstance().getAccessPolicy().getCompany_type() != null &&
                    GlobalData.getInstance().getAccessPolicy().getCompany_type().equalsIgnoreCase("township")) {
                profilePhoto.setOnClickListener(profileClickListener);
                profileText.setOnClickListener(profileClickListener);
                profileText.setVisibility(View.VISIBLE);
            } else {
                profilePhoto.setOnClickListener(unitSelectionListener);
                profileText.setVisibility(View.INVISIBLE);
            }
        }

        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
        lp.width = calculateDrawerWidth();
        mDrawerList.setLayoutParams(lp);

        isAccountMenu = false;
        setNavigationMenu(menuArray, iconArray);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        if (savedInstanceState == null) {
            selectItem(1);
        }
    }

    private int calculateDrawerWidth() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;

        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        Display display = getWindowManager().getDefaultDisplay();
        int width;
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        return width - actionBarHeight;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(@SuppressWarnings("rawtypes") AdapterView parent, View view, int position, long id) {

            if (isAccountMenu) {
                selectAccount(position);
            } else {
                selectItem(position);
            }
        }
    }

    private void selectAccount(int position) {
        try {
            if ((position - 1) >= 0 && (position - 1) <= unitArray.length - 1) {
                header_unit_name.setText(GlobalData.getInstance().getUnits().get(position - 1).getInfo());
                GlobalData.getInstance().setUpdatedUnitName(GlobalData.getInstance().getUnits().get(position - 1).getInfo());
                GlobalData.getInstance().setDemoUnitId(GlobalData.getInstance().getUnits().get(position - 1).getId());

                //send local broadcast message to HomeScreenFragment for update unit name.
                Intent intent = new Intent("UnitName");
                intent.putExtra("name", GlobalData.getInstance().getUnits().get(position - 1).getInfo());
                LocalBroadcastManager.getInstance(DashboardActivity.this).sendBroadcast(intent);

                DebugLog.d("unit name : " + GlobalData.getInstance().getUpdatedUnitName());
                DebugLog.d("unit id demo : " + GlobalData.getInstance().getDemoUnitId());

                getSupportActionBar().setTitle(GlobalData.getInstance().getUpdatedUnitName());

            } else if ((position - 1) >= 0) {
                AlertDialogLogout.newInstance(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.alert_sign_out), "Cancel", "Yes").show(getFragmentManager(), "");
            } else {
                if (isAccountMenu) {
                    isAccountMenu = false;
                    setNavigationMenu(menuArray, iconArray);
                    header_arrow_icon.setText(getString(R.string.icon_bottom_arrow));
                } else {
                    isAccountMenu = true;
                    setNavigationMenu(unitArray, getResources().getStringArray(R.array.unit_icons));
                    header_arrow_icon.setText(getString(R.string.icon_top_arrow));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } finally {
            if ((position - 1) >= 0) {
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        }
    }

    private void selectItem(int position) {

        if ((position - 1) >= 0 && (position - 1) <= menuArray.length - 1) {

            if (menuArray[position - 1].toLowerCase().equalsIgnoreCase("sensors")) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
                        getFragmentManager().popBackStack();
                    }
                }
                setSensorTab();

            } else {

                if (menuArray[position - 1].toLowerCase().equalsIgnoreCase("notification")) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    Intent intent = new Intent(this, NotificationScreenActivity.class);
                    startActivity(intent);
                    return;
                } else if (menuArray[position - 1].toLowerCase().equalsIgnoreCase("helpdesk")) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    Intent intent = new Intent(this, HelpDeskScreenActivity.class);
                    startActivity(intent);
                    return;
                }

                Fragment fragment = menuCLickList.get(menuArray[position - 1].toLowerCase());

                if (fragment != null) {
                    SharedPreferences UserInfo = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String jsonUnitInfo = UserInfo.getString("unit_info", "");
                    Resident residentData = gson.fromJson(jsonUnitInfo, Resident.class);

                    //TODO: Kept it hardcoded for lodha parking demo
                    if (residentData.getMobile().equalsIgnoreCase("9999999998") && fragment.getClass().getSimpleName().equalsIgnoreCase("ElectricityMeterScreen")) {
                        return;
                    } else if (residentData.getMobile().equalsIgnoreCase("9999999998") && fragment.getClass().getSimpleName().equalsIgnoreCase("WaterMeterScreen")) {
                        return;
                    }

                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
                            getFragmentManager().popBackStack();
                        }
                    }
                    FragmentManager fragmentManager = getFragmentManager();
                    if (position == 1) {
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
                    } else {
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit,
                                        R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit)
                                .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName())
                                .addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
                    }
                }
            }
        } else if ((position - 1) >= 0) {

            AlertDialogLogout.newInstance(getResources().getString(R.string.app_name),
                    getResources().getString(R.string.alert_sign_out), "Cancel", "Yes").show(getFragmentManager(), "");
        } else {
            if (isAccountMenu) {
                isAccountMenu = false;
                setNavigationMenu(menuArray, iconArray);
                header_arrow_icon.setText(getString(R.string.icon_bottom_arrow));
            } else {
                isAccountMenu = true;
                setNavigationMenu(unitArray, getResources().getStringArray(R.array.unit_icons));
                header_arrow_icon.setText(getString(R.string.icon_top_arrow));
            }
        }
        if (position != 0) {
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    private void setSensorTab() {

        isSensorTabAdded = true;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        LinearLayout pagerLayout = (LinearLayout) findViewById(R.id.pagerLayout);
        mPager = (ViewPager) findViewById(R.id.pager);

        List<android.support.v4.app.Fragment> fragments = new Vector<>();

        tabLayout.setVisibility(View.VISIBLE);
        mPager.setVisibility(View.VISIBLE);
        pagerLayout.setVisibility(View.VISIBLE);

        tabLayout.removeAllTabs();
        mPager.removeAllViews();

        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Yesterday"));

        fragments.add(android.support.v4.app.Fragment.instantiate(this, SensorInfoTodayList.class.getName()));
        fragments.add(android.support.v4.app.Fragment.instantiate(this, SensorInfoList.class.getName()));

        CallPagerAdapter pagerAdapter = new CallPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(pagerAdapter);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setNavigationMenu(String[] mDrawerTitles, String[] mDrawerIcons) {

        ArrayList<Items> drawerItems = new ArrayList<>();

        for (int i = 0; i < mDrawerTitles.length; i++) {
            drawerItems.add(new Items(mDrawerTitles[i], mDrawerIcons[i]));
        }
        mDrawerList.setAdapter(new DrawerAdapter(getApplicationContext(), drawerItems));
    }

    private final View.OnClickListener unitSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isAccountMenu) {
                isAccountMenu = false;
                setNavigationMenu(menuArray, iconArray);
                header_arrow_icon.setText(getString(R.string.icon_bottom_arrow));
            } else {
                isAccountMenu = true;
                setNavigationMenu(unitArray, getResources().getStringArray(R.array.unit_icons));
                header_arrow_icon.setText(getString(R.string.icon_top_arrow));
            }
        }
    };

    View.OnClickListener profileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
    };

    private void getUserInfo() {

        SharedPreferences UserInfo = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        GlobalData.getInstance().setAuthToken(UserInfo.getString("auth_token", ""));
        GlobalData.getInstance().setApiKey(UserInfo.getString("api_key", ""));
        GlobalData.getInstance().setResidentId(UserInfo.getString("resident_id", ""));

        Gson gson = new Gson();
        String jsonUnitInfo = UserInfo.getString("unit_info", "");
        Resident residentData = gson.fromJson(jsonUnitInfo, Resident.class);

        String jsonAccessPolicy = UserInfo.getString("access_policy", "");
        AccessPolicy policy = gson.fromJson(jsonAccessPolicy, AccessPolicy.class);

        if (policy.getCompany_type().equalsIgnoreCase("township")) {
            GlobalData.getInstance().setUnits(residentData.getUnits());
        } else {
            GlobalData.getInstance().setUnits(residentData.getSites());
        }

        GlobalData.getInstance().setAccessPolicy(policy);

        String siteId = "";
        if (residentData.getSites() != null && residentData.getSites().size() > 0) {
            siteId = residentData.getSites().get(0).getId();
        }
        CreateRequest.getInstance().loadSessionData(UserInfo.getString("api_key", ""), UserInfo.getString("auth_token", ""), UserInfo.getString("subdomain", ""), siteId);

        if (GlobalData.getInstance().getUnits().size() > 0) {
            unitArray = new String[GlobalData.getInstance().getUnits().size()];
        }

        for (int i = 0; i < GlobalData.getInstance().getUnits().size(); i++) {
            DebugLog.d("Unit Info : " + GlobalData.getInstance().getUnits().get(i).getInfo());
            unitArray[i] = GlobalData.getInstance().getUnits().get(i).getInfo();
        }

        if (GlobalData.getInstance().getAccessPolicy().getMenu() != null && GlobalData.getInstance().getAccessPolicy().getMenu().size() > 0) {

            if (GlobalData.getInstance().getAccessPolicy().getCompany_type().equalsIgnoreCase("township")) {
                menuArray = new String[GlobalData.getInstance().getAccessPolicy().getMenu().size() + 1];
                iconArray = new String[GlobalData.getInstance().getAccessPolicy().getMenu().size() + 1];

                menuArray[0] = "home";
                iconArray[0] = getResources().getString(R.string.icon_myflat);

                for (int i = 0; i < GlobalData.getInstance().getAccessPolicy().getMenu().size(); i++) {

                    DebugLog.d("Unit Info : " + GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getName());

                    //TODO: Kept it hardcoded for lodha parking demo
                    if (residentData.getMobile().equalsIgnoreCase("9999999998") && GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getName().equalsIgnoreCase("electricity")) {
                        menuArray[i + 1] = "Shopping";
                    } else if (residentData.getMobile().equalsIgnoreCase("9999999998") && GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getName().equalsIgnoreCase("water")) {
                        menuArray[i + 1] = "Amenities";
                    } else if (residentData.getMobile().equalsIgnoreCase("9999999998") && GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getName().equalsIgnoreCase("planner")) {
                        menuArray[i + 1] = "Parking";
                    } else {
                        menuArray[i + 1] = GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getName();
                    }

                    if (GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon() != null
                            && !GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon().equalsIgnoreCase("")) {

                        //TODO: Kept it hardcoded for lodha parking demo
                        if (residentData.getMobile().equalsIgnoreCase("9999999998") && GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon().equalsIgnoreCase("e602")) {
                            iconArray[i + 1] = "&#xe603;";

                        } else if (residentData.getMobile().equalsIgnoreCase("9999999998") && GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon().equalsIgnoreCase("e60b")) {
                            iconArray[i + 1] = "&#xe606;";

                        } else {
                            iconArray[i + 1] = "&#x" + GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon() + ";";
                        }

                    } else {
                        iconArray[i + 1] = "";
                    }
                }
            } else {
                menuArray = new String[GlobalData.getInstance().getAccessPolicy().getMenu().size()];
                iconArray = new String[GlobalData.getInstance().getAccessPolicy().getMenu().size()];

                for (int i = 0; i < GlobalData.getInstance().getAccessPolicy().getMenu().size(); i++) {

                    DebugLog.d("Unit Info : " + GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getName());
                    menuArray[i] = GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getName();

                    if (GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon() != null
                            && !GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon().equalsIgnoreCase("")) {

                        iconArray[i] = "&#x" + GlobalData.getInstance().getAccessPolicy().getMenu().get(i).getIcon() + ";";
                    } else {
                        iconArray[i] = getResources().getString(R.string.icon_myflat);
                    }
                }
            }
        }
        fetchCategories();
    }

    private void fetchCategories() {
        Intent msgIntent = new Intent(this, FetchCategories.class);
        startService(msgIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        isRunning = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        isRunning = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getFragmentManager();
        DebugLog.d("back count : " + fm.getBackStackEntryCount());

        try {
            if (fm.getBackStackEntryCount() == 1 && isSensorTabAdded) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bw_color_purple)));
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(getResources().getColor(R.color.bw_color_purple));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        if (fm.getBackStackEntryCount() > 0) {
            getSupportActionBar().show();
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + DashboardActivity.this.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            String currentVersion = "";
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                VersionComprator cmp = new VersionComprator();
                int result = cmp.compare(onlineVersion, currentVersion);

                if (result > 0 && isRunning && DashboardActivity.this != null) {
                    AlertDialogUpdateVersion.newInstance("New update available!", getResources().getString(R.string.update_app_message), "Later", "Update")
                            .show(getFragmentManager(), "Show update dialog");
                }
            }
        }
    }

    private void updatePaymentStatus(String paymentStatus, String paymentId) {
        DebugLog.d("paymentStatus : " + paymentStatus);

        if (updatePaymentBroadcastFlag) {
            Intent i = new Intent("updatePayment");
            i.putExtra("status", paymentStatus);
            i.putExtra("paymentId", paymentId);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
            updatePaymentBroadcastFlag = false;
        }
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
