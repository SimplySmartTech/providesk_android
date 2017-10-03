package com.simplysmart.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.simplysmart.app.R;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.common.CommonResponse;
import com.simplysmart.lib.model.login.ChangePasswordRequest;
import com.simplysmart.lib.model.login.Resident;
import com.simplysmart.lib.request.CreateRequest;


/**
 * Created by shekhar on 04/01/16.
 */
public class ChangePasswordActivity extends BaseActivity {

    private LinearLayout ll_parent;
    private EditText currentPassword, newPassword, confirmPassword;
    private String screenFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Change password");

        SharedPreferences UserInfo = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonUnitInfo = UserInfo.getString("unit_info", "");
        Resident residentData = gson.fromJson(jsonUnitInfo, Resident.class);

        GlobalData.getInstance().setAuthToken(UserInfo.getString("auth_token", ""));
        GlobalData.getInstance().setApiKey(UserInfo.getString("api_key", ""));
        GlobalData.getInstance().setResidentId(UserInfo.getString("resident_id", ""));

        String siteId = "";
        if (residentData.getSites() != null && residentData.getSites().size() > 0) {
            siteId = residentData.getSites().get(0).getId();
        }

        CreateRequest.getInstance().loadSessionData(UserInfo.getString("api_key", ""), UserInfo.getString("auth_token", ""), UserInfo.getString("subdomain", ""), siteId);

        if (getIntent() != null && getIntent().getExtras() != null) {
            screenFrom = getIntent().getStringExtra("screenFrom");
        }

        initViews();
    }

    @Override
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

    @Override
    protected int getStatusBarColor() {
        return R.color.colorPrimary;
    }

    private void initViews() {

        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        TextView buttonSubmit = (TextView) findViewById(R.id.buttonSubmit);
        TextView buttonSkip = (TextView) findViewById(R.id.buttonSkip);

        currentPassword = (EditText) findViewById(R.id.currentPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        buttonSubmit.setOnClickListener(loginClick);
        buttonSkip.setOnClickListener(skipClick);

        if (screenFrom.equalsIgnoreCase("profile")) {
            buttonSkip.setVisibility(View.GONE);
        } else {
            buttonSkip.setVisibility(View.VISIBLE);
        }
    }

    private final View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (validateChangePassword(currentPassword.getText().toString().trim(),
                    newPassword.getText().toString().trim(),
                    confirmPassword.getText().toString().trim())) {
                changePasswordRequest(currentPassword.getText().toString().trim(), newPassword.getText().toString().trim());
            }
        }
    };

    private final View.OnClickListener skipClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences ResetUserPreferences = getSharedPreferences("AppSettingsPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = ResetUserPreferences.edit();
            editor.putBoolean("newInstallation", false);
            editor.apply();
            Intent i = new Intent(ChangePasswordActivity.this, HelpDeskScreenActivity.class);
            startActivity(i);
            finish();
        }
    };

    private boolean validateChangePassword(String currentPassword, String newPassword, String confirmPassword) {

        if (currentPassword.trim().isEmpty() && newPassword.trim().isEmpty() && confirmPassword.trim().isEmpty()) {
            showSnackBar(ll_parent, getString(R.string.error_enter_mandatory));
            return false;
        } else if (currentPassword.trim().isEmpty()) {
            showSnackBar(ll_parent, getString(R.string.error_current_password));
            return false;
        } else if (currentPassword.trim().length() < 6) {
            showSnackBar(ll_parent, "current password should be 6 digits");
            return false;
        } else if (newPassword.trim().isEmpty()) {
            showSnackBar(ll_parent, getString(R.string.error_new_password));
            return false;
        } else if (newPassword.trim().isEmpty()) {
            showSnackBar(ll_parent, "New password should be 6 digits");
            return false;
        } else if (confirmPassword.trim().isEmpty()) {
            showSnackBar(ll_parent, getString(R.string.error_confirm_password));
            return false;
        } else if (!newPassword.trim().equalsIgnoreCase(confirmPassword)) {
            showSnackBar(ll_parent, getString(R.string.error_match_password));
            return false;
        }
        return true;
    }

    private void changePasswordRequest(String currentPassword, String newPassword) {

        if (NetworkUtilities.isInternet(ChangePasswordActivity.this)) {

            ChangePasswordRequest changePassword = new ChangePasswordRequest();
            Resident resident = new Resident();
            resident.setCurrent_password(currentPassword);
            resident.setPassword(newPassword);
            resident.setPassword_confirmation(newPassword);
            changePassword.setResident(resident);

            showActivitySpinner();
            CreateRequest.getInstance().changePassword(GlobalData.getInstance().getResidentId(), changePassword, new ApiCallback<CommonResponse>() {
                @Override
                public void onSuccess(CommonResponse response) {

                    dismissActivitySpinner();
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                    Intent i;
                    if (screenFrom.equalsIgnoreCase("profile")) {
                        SharedPreferences userInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        userInfo.edit().clear().apply();
                        i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    } else {
                        SharedPreferences ResetUserPreferences = getSharedPreferences("AppSettingsPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = ResetUserPreferences.edit();
                        editor.putBoolean("newInstallation", false);
                        editor.apply();
                        i = new Intent(ChangePasswordActivity.this, DashboardActivity.class);
                    }
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    dismissActivitySpinner();
                }
            });

        } else {
            DebugLog.d(getString(R.string.error_no_internet_connection));
        }
    }

}