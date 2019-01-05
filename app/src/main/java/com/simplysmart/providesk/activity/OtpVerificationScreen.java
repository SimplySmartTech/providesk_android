package com.simplysmart.providesk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.model.common.CommonResponse;
import com.simplysmart.lib.model.login.AccessPolicy;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.login.OtpRequest;
import com.simplysmart.lib.model.login.Resident;
import com.simplysmart.lib.request.CreateRequest;
import com.simplysmart.providesk.R;
import com.simplysmart.providesk.config.StringConstants;

import java.util.Arrays;

public class OtpVerificationScreen extends BaseActivity implements View.OnClickListener {

    private TextView labelOtpSent2;

    private String userId = "";
    private String mobileNumber = "";
    private String subDomain = "";

    private EditText inputDigitOne, inputDigitTwo, inputDigitThree, inputDigitFour, inputDigitFive, inputDigitSix;
    private TextView resendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification_screen);

        if (getIntent() != null && getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("userId");
            subDomain = getIntent().getStringExtra("subDomain");
            mobileNumber = getIntent().getStringExtra("mobileNumber");
        }
        bindViews();
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    private void bindViews() {

        resendButton = (TextView) findViewById(R.id.resendButton);

        labelOtpSent2 = (TextView)findViewById(R.id.labelOtpSent2);

        inputDigitOne = (EditText) findViewById(R.id.inputDigitOne);
        inputDigitTwo = (EditText) findViewById(R.id.inputDigitTwo);
        inputDigitThree = (EditText) findViewById(R.id.inputDigitThree);
        inputDigitFour = (EditText) findViewById(R.id.inputDigitFour);
        inputDigitFive = (EditText) findViewById(R.id.inputDigitFive);
        inputDigitSix = (EditText) findViewById(R.id.inputDigitSix);

        labelOtpSent2.setText("we've sent an OTP to "+mobileNumber);

        resendButton.setOnClickListener(this);

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

                    verifyOtpRequest(inputDigitOne.getText().toString().trim()
                            + inputDigitTwo.getText().toString().trim()
                            + inputDigitThree.getText().toString().trim()
                            + inputDigitFour.getText().toString().trim()
                            + inputDigitFive.getText().toString().trim()
                            + inputDigitSix.getText().toString().trim());

                } else if (editable.length() == 0) {
                    inputDigitFive.requestFocus();
                    inputDigitFive.setSelection(inputDigitFive.getText().length());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resendButton:
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

                        try {
                            setUserData(response);

                            SharedPreferences ResetUserPreferences = OtpVerificationScreen.this.getSharedPreferences("AppSettingsPreferences", Context.MODE_PRIVATE);
                            boolean newInstallation = ResetUserPreferences.getBoolean("newInstallation", true);

                            Intent i;
                            if (newInstallation) {
                                i = new Intent(OtpVerificationScreen.this, ChangePasswordActivity.class);
                            } else {
                                i = new Intent(OtpVerificationScreen.this, HelpDeskScreenActivity.class);
                            }
                            startActivity(i);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            displayMessage("Error:" + Arrays.toString(e.getStackTrace()));
                        }
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

}
