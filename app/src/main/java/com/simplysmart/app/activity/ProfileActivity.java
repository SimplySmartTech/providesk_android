package com.simplysmart.app.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.dialog.AlertDialogLogout;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.request.CreateRequest;

public class ProfileActivity extends BaseActivity {

    private TextView name;
    private TextView mobile;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Profile");

        name = (TextView) findViewById(R.id.tvNumber5);
        mobile = (TextView) findViewById(R.id.tvNumber1);
        email = (TextView) findViewById(R.id.tvNumber3);

        getUserData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    android.app.FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                } else {
                    onBackPressed();
                }
                return true;

            case R.id.action_change_password:
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                intent.putExtra("screenFrom", "profile");
                startActivity(intent);
                return true;

            case R.id.action_reset_password:
                MyCustomAlertDialog alertDialog = new MyCustomAlertDialog(ProfileActivity.this);
                alertDialog.createCustomDialog();
                return true;

            case R.id.action_logout:
                AlertDialogLogout.newInstance(getString(R.string.app_name),
                        getString(R.string.alert_sign_out), "Not now", "Yes").show(getFragmentManager(), "");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    private void getUserData() {

        SharedPreferences UserInfo = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        name.setText(UserInfo.getString("name", ""));
        mobile.setText(UserInfo.getString("mobile", ""));
        email.setText(UserInfo.getString("email", ""));
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
            userNameInput.setText("");

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

                                if (NetworkUtilities.isInternet(ProfileActivity.this)) {

                                    showActivitySpinner();
                                    CommonMethod.hideKeyboard(ProfileActivity.this);
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
