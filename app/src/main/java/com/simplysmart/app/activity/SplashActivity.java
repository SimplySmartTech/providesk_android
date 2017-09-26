package com.simplysmart.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.StringConstants;

/**
 * Created by Shekhar on 4/8/15.
 * Splash screen for the application.
 */
public class SplashActivity extends BaseActivity {

    private boolean isLogin;
    private static final int SPLASH_TIME_OUT = 1500;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logo);

        //Used for force fully reset application (logout forcefully) set flag true for reset user
        resetApplication(false);

        switchToNextActivity();

    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(finishActivityBroadcast,
                new IntentFilter(StringConstants.BROADCAST_FINISH_ACTIVITY));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishActivityBroadcast);
        super.onDestroy();
    }

    private void switchToNextActivity() {

        SharedPreferences UserInfo = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        isLogin = UserInfo.getBoolean("isLogin", false);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i;
                if (isLogin) {
                    SharedPreferences ResetUserPreferences = SplashActivity.this.getSharedPreferences("AppSettingsPreferences", Context.MODE_PRIVATE);
                    boolean newInstallation = ResetUserPreferences.getBoolean("newInstallation", true);

                    if (newInstallation) {
                        i = new Intent(SplashActivity.this, ChangePasswordActivity.class);
                    } else {
                        i = new Intent(SplashActivity.this, DashboardActivity.class);
                    }
                    startActivity(i);
                    finish();
                } else {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, logo, "appLogo");
                    startActivity(i, options.toBundle());
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void resetApplication(boolean reset) {
        SharedPreferences ResetUserPreferences = this.getSharedPreferences("ResetUserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ResetUserPreferences.edit();
        boolean logoutUser = ResetUserPreferences.getBoolean("logoutUser", true);

        if (reset) {
            if (logoutUser) {
                SharedPreferences UserInfo = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor userInfoEdit = UserInfo.edit();
                userInfoEdit.clear().apply();
                editor.putBoolean("logoutUser", false).apply();
            }
        } else {
            editor.remove("logoutUser").apply();
        }
    }

    private BroadcastReceiver finishActivityBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
}