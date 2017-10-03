package com.simplysmart.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simplysmart.app.R;
import com.simplysmart.app.config.StringConstants;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.common.CommonResponse;
import com.simplysmart.lib.model.login.AccessPolicy;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.login.OtpRequest;
import com.simplysmart.lib.model.login.Resident;
import com.simplysmart.lib.request.CreateRequest;

import java.util.concurrent.TimeUnit;

public class OtpVerificationScreen extends BaseActivity implements View.OnClickListener {

    private BottomSheetBehavior mBottomSheetBehavior;
    private MyTimer timerTask;
    private boolean isTimerRunning = false;
    private TextView otpTimer;
    private TextView waitingLabel;
    private ProgressBar horizontalBar;

    private LinearLayout bottomLayout;
    private RelativeLayout parentLayout;
    private TextView enterManually;
    private TextView resendOtp;

    private String userId = "";
    private String subDomain = "";

    private EditText inputDigitOne, inputDigitTwo, inputDigitThree, inputDigitFour, inputDigitFive, inputDigitSix;
    private TextView resendButton;
    private View bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification_screen);

        LocalBroadcastManager.getInstance(this).registerReceiver(mySmsReceiver, new IntentFilter("smsreceiver"));

        if (getIntent() != null && getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("userId");
            subDomain = getIntent().getStringExtra("subDomain");
        }

        bindViews();

        startCountdownTimer();
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    private void bindViews() {

        bottomSheet = findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        otpTimer = (TextView) findViewById(R.id.otpTimer);
        waitingLabel = (TextView) findViewById(R.id.waitingLabel);

        enterManually = (TextView) findViewById(R.id.enterManually);
        resendOtp = (TextView) findViewById(R.id.resendOtp);
        resendButton = (TextView) findViewById(R.id.resendButton);

        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);

        inputDigitOne = (EditText) findViewById(R.id.inputDigitOne);
        inputDigitTwo = (EditText) findViewById(R.id.inputDigitTwo);
        inputDigitThree = (EditText) findViewById(R.id.inputDigitThree);
        inputDigitFour = (EditText) findViewById(R.id.inputDigitFour);
        inputDigitFive = (EditText) findViewById(R.id.inputDigitFive);
        inputDigitSix = (EditText) findViewById(R.id.inputDigitSix);

        horizontalBar = (ProgressBar) findViewById(R.id.horizontalBar);

        parentLayout.setOnClickListener(this);
        resendButton.setOnClickListener(this);
        enterManually.setOnClickListener(this);
        resendOtp.setOnClickListener(this);

        inputDigitOne.clearFocus();
        inputDigitOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputDigitTwo.requestFocus();
                }
            }
        });

        inputDigitTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputDigitThree.requestFocus();
                } else if (editable.length() == 0) {
                    inputDigitOne.requestFocus();
                    inputDigitOne.setSelection(inputDigitOne.getText().length());
                }
            }
        });

        inputDigitThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputDigitFour.requestFocus();
                    inputDigitFour.setCursorVisible(true);
                } else if (editable.length() == 0) {
                    inputDigitTwo.requestFocus();
                    inputDigitTwo.setSelection(inputDigitTwo.getText().length());
                }
            }
        });

        inputDigitFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputDigitFive.requestFocus();
                    inputDigitFive.setCursorVisible(true);
                } else if (editable.length() == 0) {
                    inputDigitThree.requestFocus();
                    inputDigitThree.setSelection(inputDigitThree.getText().length());
                }
            }
        });

        inputDigitFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputDigitSix.requestFocus();
                    inputDigitSix.setCursorVisible(true);
                } else if (editable.length() == 0) {
                    inputDigitFour.requestFocus();
                    inputDigitFour.setSelection(inputDigitFour.getText().length());
                }
            }
        });

        inputDigitSix.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                inputDigitFour.setCursorVisible(true);
                return false;
            }
        });

        inputDigitSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    CommonMethod.hideKeyboard(OtpVerificationScreen.this);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    verifyOtpRequest(inputDigitOne.getText().toString().trim()
                            + inputDigitTwo.getText().toString().trim()
                            + inputDigitThree.getText().toString().trim()
                            + inputDigitFour.getText().toString().trim()
                            + inputDigitFive.getText().toString().trim()
                            + inputDigitSix.getText().toString().trim());

                    if (isTimerRunning) {
                        isTimerRunning = false;
                        timerTask.cancel();
                    }
                } else if (editable.length() == 0) {
                    inputDigitFive.requestFocus();
                    inputDigitFive.setSelection(inputDigitFive.getText().length());
                }
            }
        });
    }

    private void startCountdownTimer() {
        timerTask = new MyTimer(30 * 1000, 1000);
        timerTask.start();
        isTimerRunning = true;
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isTimerRunning) {
            timerTask.cancel();
            isTimerRunning = false;
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mySmsReceiver);
    }

    BroadcastReceiver mySmsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (isTimerRunning) {
                timerTask.cancel();
                String message = intent.getStringExtra("SMS_MESSAGE");
                DebugLog.d("message : " + message);

                if (NetworkUtilities.isInternet(OtpVerificationScreen.this)) {
                    verifyOtpRequest(message);
                } else {
//                    showSnackBar(parentLayout, "Please check your internet connection", false);
                }
            }

//            if (message != null) {
//
//                if (message.contains(":")) {
//                    String[] data = message.split(":");
//
//                    if (data.length > 0) {
//                        String code = data[1].trim();
//                        Log.d("sms", "code  : " + code);
//
//                        if (NetworkUtilities.isInternet(OtpVerificationScreen.this)) {
//
//
//                        } else {
////                            (getResources().getString(R.string.app_name), "Please check your internet connection", "", "Ok");
//                        }
//                    } else {
//                        DebugLog.d("sms error split");
//                    }
//                }
//            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parentLayout:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (isTimerRunning) {
                    isTimerRunning = false;
                    timerTask.cancel();
                }
                break;
            case R.id.resendButton:
                callResendOtpRequest();
                break;
            case R.id.enterManually:

                break;
            case R.id.resendOtp:
                callResendOtpRequest();
                break;
        }
    }

    private void callResendOtpRequest() {

        showActivitySpinner();

        CreateRequest.getInstance().generateOtpRequest(userId,
                subDomain,
                new ApiCallback<CommonResponse>() {

                    @Override
                    public void onSuccess(CommonResponse response) {
                        dismissActivitySpinner();


                    }

                    @Override
                    public void onFailure(String error) {
                        dismissActivitySpinner();
                        displayMessage(error);
                    }
                });

//        LoginRequest request = new LoginRequest();
//        UserRegistration session = new UserRegistration();
//
//        String deviceId = CommonMethod.getDeviceId(this);
//        DebugLog.d("device id : " + deviceId);
//
//        session.setDevice_id(deviceId);
//        session.setMobile_number(mobileNumber);
//        request.setUser(session);
//
//        showActivitySpinner();
//        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
//        Call<CommonResponse> loginResponseCall = apiInterface.userOtpResend(request);
//
//        loginResponseCall.enqueue(new Callback<CommonResponse>() {
//
//            @Override
//            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
//                dismissActivitySpinner();
//                if (response.isSuccessful()) {
//                    showSnackBar(parentLayout, response.body().getMessage(), true);
//                } else {
//                    APIError error = ErrorUtils.parseError(response);
//                    showSnackBar(parentLayout, error.getMessage(), false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CommonResponse> call, Throwable t) {
//                dismissActivitySpinner();
//            }
//        });
    }

    private void verifyOtpRequest(String otp) {

        showActivitySpinner();
        CreateRequest.getInstance().verifyOtpRequest(userId,
                subDomain,
                new OtpRequest(otp),
                new ApiCallback<LoginResponse>() {

                    @Override
                    public void onSuccess(LoginResponse response) {
                        dismissActivitySpinner();

                        setUserData(response);

                        SharedPreferences ResetUserPreferences = OtpVerificationScreen.this.getSharedPreferences("AppSettingsPreferences", Context.MODE_PRIVATE);
                        boolean newInstallation = ResetUserPreferences.getBoolean("newInstallation", true);

                        Intent i;
                        if (newInstallation) {
                            i = new Intent(OtpVerificationScreen.this, ChangePasswordActivity.class);
                        } else {
                            i = new Intent(OtpVerificationScreen.this, DashboardActivity.class);
                        }
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        dismissActivitySpinner();
                        displayMessage(error);
                    }
                });
    }

    private void setUserData(LoginResponse response) {

        Intent loginComplete = new Intent(StringConstants.BROADCAST_FINISH_ACTIVITY);
        LocalBroadcastManager.getInstance(OtpVerificationScreen.this).sendBroadcast(loginComplete);

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

    private class MyTimer extends CountDownTimer {

        MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            String hms = String.format("%02d",
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            otpTimer.setText(hms);
            if (millisUntilFinished < 10000) {
                waitingLabel.setText("It is taking long. You may...");
                bottomLayout.setVisibility(View.VISIBLE);
            } else {
                bottomLayout.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onFinish() {
            isTimerRunning = false;
            otpTimer.setText("---");
            otpTimer.setVisibility(View.INVISIBLE);
        }
    }
}
