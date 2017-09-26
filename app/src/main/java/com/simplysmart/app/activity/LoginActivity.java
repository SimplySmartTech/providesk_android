package com.simplysmart.app.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simplysmart.app.R;
import com.simplysmart.app.config.StringConstants;
import com.simplysmart.app.gcm.QuickstartPreferences;
import com.simplysmart.app.gcm.RegistrationIntentService;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.login.AccessPolicy;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.login.Resident;
import com.simplysmart.lib.request.CreateRequest;

import java.util.ArrayList;

/**
 * Created by shekhar on 4/8/15.
 * Login screen for Application
 */
public class LoginActivity extends BaseActivity {

    private static final String PHONE_REGEX = "^(?:|[0-9]{10}|)$";

    private Context context;
    private EditText editUsername, editPassword;
    private TextView buttonLogin;
    private Spinner companySpinner;
    private RelativeLayout llCompanySpinner;
    private String subDomain = "";
    private RelativeLayout loginInputLayout;
    private TextView btn_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;
        initializeWidgets();
        buttonLogin.setOnClickListener(loginClick);
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    private void initializeWidgets() {

        editUsername = (EditText) findViewById(R.id.user_name);
        editPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (TextView) findViewById(R.id.btn_login);
        companySpinner = (Spinner) findViewById(R.id.companySpinner);
        llCompanySpinner = (RelativeLayout) findViewById(R.id.llCompanySpinner);
        btn_forgot_password = (TextView) findViewById(R.id.btn_forgot_password);

        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                llCompanySpinner.setVisibility(View.GONE);
            }
        });

        loginInputLayout = (RelativeLayout) findViewById(R.id.loginInputLayout);

        if (loginInputLayout != null) {
            loginInputLayout.setVisibility(View.GONE);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginInputLayout.setVisibility(View.VISIBLE);
                }
            }, 150);
        }

        btn_forgot_password.setOnClickListener(forgotPasswordClick);
    }

    private final View.OnClickListener forgotPasswordClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyCustomAlertDialog alertDialog = new MyCustomAlertDialog(LoginActivity.this);
            alertDialog.createCustomDialog();
        }
    };

    private final View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (NetworkUtilities.isInternet(getApplicationContext())) {

                if (llCompanySpinner.getVisibility() == View.VISIBLE) {
                    callCompanyLogin();
                } else {
                    callNormalLogin();
                }
            } else {
                displayMessage(getString(R.string.error_no_internet_connection));
            }
        }
    };

    private void callCompanyLogin() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String gcmToken = sharedPreferences.getString(QuickstartPreferences.GCM_TOKEN, "");

        if (gcmToken.isEmpty()) {
            DebugLog.d("Unable to get gcm token from server.");
            return;
        }
        prepareCompanyLoginRequest(gcmToken);
    }

    private void callNormalLogin() {
        if (CommonMethod.checkPlayServices(LoginActivity.this)) {

            CommonMethod.hideKeyboard(LoginActivity.this);
            showActivitySpinner();
            Intent intent = new Intent(LoginActivity.this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            dismissActivitySpinner();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String gcmToken = sharedPreferences.getString(QuickstartPreferences.GCM_TOKEN, "");

            if (gcmToken.isEmpty()) {
                DebugLog.d("Unable to get gcm token from server.");
                return;
            }
            prepareNormalLoginRequest(gcmToken);
        }
    };

    private void prepareNormalLoginRequest(String gcmToken) {

        DebugLog.d("login registrationId :" + gcmToken);

        if (NetworkUtilities.isInternet(LoginActivity.this)) {

            if (residentLoginValidation(editUsername.getText().toString().trim(),
                    editPassword.getText().toString().trim(), LoginActivity.this)) {
                showActivitySpinner();
                CreateRequest.getInstance().loginRequest(LoginActivity.this,
                        getPackageName(),
                        editUsername.getText().toString().trim(),
                        editPassword.getText().toString().trim(),
                        gcmToken,
                        new ApiCallback<LoginResponse>() {

                            @Override
                            public void onSuccess(final LoginResponse response) {
                                dismissActivitySpinner();
                                if (response.isAuthenticated()) {
                                    llCompanySpinner.setVisibility(View.GONE);

                                    setUserData(response);

                                    SharedPreferences ResetUserPreferences = LoginActivity.this.getSharedPreferences("AppSettingsPreferences", Context.MODE_PRIVATE);
                                    boolean newInstallation = ResetUserPreferences.getBoolean("newInstallation", true);

                                    Intent i;
                                    if (newInstallation) {
                                        i = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                                    } else {
                                        i = new Intent(LoginActivity.this, DashboardActivity.class);
                                    }
                                    startActivity(i);
                                    finish();
                                } else {
                                    llCompanySpinner.setVisibility(View.VISIBLE);

                                    ArrayList<String> arrayList = new ArrayList<>();

                                    arrayList.add(0, "Select Company");
                                    if (response.getCompany_list() != null && response.getCompany_list().size() > 0) {

                                        for (int i = 0; i < response.getCompany_list().size(); i++) {
                                            arrayList.add(response.getCompany_list().get(i).getName());
                                        }

                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(LoginActivity.this,
                                                android.R.layout.simple_spinner_item, arrayList);
                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        companySpinner.setAdapter(spinnerArrayAdapter);

                                        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if (position > 0) {
                                                    subDomain = response.getCompany_list().get(position - 1).getSubdomain();
                                                    DebugLog.d("subDomain : " + subDomain);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                dismissActivitySpinner();
                                displayMessage(error);
                            }
                        });

            }
        } else {
            displayMessage(getString(R.string.error_no_internet_connection));
        }
    }

    private void prepareCompanyLoginRequest(String gcmToken) {

        DebugLog.d("login registrationId :" + gcmToken);

        if (NetworkUtilities.isInternet(LoginActivity.this)) {

            if (residentLoginValidation(editUsername.getText().toString().trim(),
                    editPassword.getText().toString().trim(), LoginActivity.this)) {

                showActivitySpinner();

                CreateRequest.getInstance().loginRequestWithSubDomain(LoginActivity.this,
                        getPackageName(),
                        subDomain,
                        editUsername.getText().toString().trim(),
                        editPassword.getText().toString().trim(),
                        gcmToken,
                        new ApiCallback<LoginResponse>() {

                            @Override
                            public void onSuccess(LoginResponse response) {
                                dismissActivitySpinner();

                                if (response.getData() != null) {

                                    setUserData(response);

                                    SharedPreferences ResetUserPreferences = LoginActivity.this.getSharedPreferences("AppSettingsPreferences", Context.MODE_PRIVATE);
                                    boolean newInstallation = ResetUserPreferences.getBoolean("newInstallation", true);

                                    Intent i;
                                    if (newInstallation) {
                                        i = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                                    } else {
                                        i = new Intent(LoginActivity.this, DashboardActivity.class);
                                    }
                                    startActivity(i);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                dismissActivitySpinner();
                                displayMessage(error);
                            }
                        });
            }
        } else {
            displayMessage(getString(R.string.error_no_internet_connection));
        }
    }

    private void setUserData(LoginResponse response) {

        Intent loginComplete = new Intent(StringConstants.BROADCAST_FINISH_ACTIVITY);
        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(loginComplete);

        Gson gson = new Gson();
        Resident residentData = response.getData().getResident();
        String unitInfo = gson.toJson(residentData);

        AccessPolicy policy = response.getPolicy();
        String accessPolicy = gson.toJson(policy);

        SharedPreferences UserInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = UserInfo.edit();

        preferencesEditor.putBoolean("isLogin", true);

        preferencesEditor.putString("name", residentData.getName());
        preferencesEditor.putString("email", residentData.getEmail());
        preferencesEditor.putString("mobile", residentData.getMobile());
        preferencesEditor.putString("lang", residentData.getLang());
        preferencesEditor.putString("api_key", residentData.getApi_key());
        preferencesEditor.putString("subdomain", response.getSubdomain());
        preferencesEditor.putString("auth_token", residentData.getAuth_token());
        preferencesEditor.putString("resident_id", residentData.getId());
        preferencesEditor.putString("unit_info", unitInfo);
        preferencesEditor.putString("access_policy", accessPolicy);
        preferencesEditor.putString("booking_id", residentData.getBooking_id());
        preferencesEditor.putString("profile_photo_url", "");

        preferencesEditor.apply();

    }

    private boolean residentLoginValidation(String mobile, String Password, Context mContext) {

        if (mobile.equals("")) {
            displayMessage(mContext.getResources().getString(R.string.error_enter_mobile_number));
            return false;
        } else if (!mobile.matches(PHONE_REGEX)) {
            displayMessage(mContext.getResources().getString(R.string.error_invalid_mobile_number));
            return false;
        }
        if (Password.equals("")) {
            displayMessage(mContext.getResources().getString(R.string.error_enter_pin_number));
            return false;
        } else if (Password.trim().length() > 6 || Password.trim().length() < 6) {
            displayMessage(mContext.getResources().getString(R.string.error_invalid_pin_number));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent loginComplete = new Intent(StringConstants.BROADCAST_FINISH_ACTIVITY);
        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(loginComplete);
        finish();
    }

    public class MyCustomAlertDialog {

        final Activity _Activity;
        private Dialog resource_Dialog;
        private EditText userNameInput;
        private LinearLayout dialogLayout;

        public MyCustomAlertDialog(Activity _Activity) {
            this._Activity = _Activity;
        }

        public void createCustomDialog() {

            resource_Dialog = new Dialog(_Activity);
            resource_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            resource_Dialog.setContentView(R.layout.dialog_reset_password);
            resource_Dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams WMLP = resource_Dialog.getWindow().getAttributes();
            WMLP.dimAmount = (float) 0.4;

            resource_Dialog.getWindow().setAttributes(WMLP);
            resource_Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });

            dialogLayout = (LinearLayout) resource_Dialog.findViewById(R.id.dialogLayout);
            userNameInput = (EditText) resource_Dialog.findViewById(R.id.userNameInput);
            if (!editUsername.getText().toString().trim().equalsIgnoreCase("")) {
                userNameInput.setText(editUsername.getText().toString().trim());
            } else {
                userNameInput.setText("");
            }
            userNameInput.setSelection(userNameInput.getText().length());

            dialogLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNameInput.setError(null);
                }
            });

            resource_Dialog.findViewById(R.id.dialogButtonPositive)
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (!userNameInput.getText().toString().trim().equalsIgnoreCase("")) {

                                if (NetworkUtilities.isInternet(LoginActivity.this)) {

                                    showActivitySpinner();
                                    CommonMethod.hideKeyboard(LoginActivity.this);
                                    CreateRequest.getInstance().resetPassword(userNameInput.getText().toString().trim(), new ApiCallback<LoginResponse>() {
                                        @Override
                                        public void onSuccess(LoginResponse response) {
                                            dismissActivitySpinner();
                                            displayMessage(response.getMessage());
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                            dismissActivitySpinner();
                                            displayMessage(error);
                                        }
                                    });
                                    resource_Dialog.dismiss();
                                } else {
                                    userNameInput.setError(getString(R.string.error_no_internet_connection));
                                    userNameInput.requestFocus();
                                    userNameInput.setSelection(userNameInput.length());
                                }
                            } else {
                                userNameInput.setError("Please enter username");
                                userNameInput.requestFocus();
                            }
                        }
                    });

            resource_Dialog.findViewById(R.id.dialogButtonNegative)
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            resource_Dialog.dismiss();
                        }
                    });
            resource_Dialog.show();
        }
    }
}
