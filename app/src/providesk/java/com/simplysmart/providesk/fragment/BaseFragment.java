package com.simplysmart.providesk.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.activity.HelpDeskScreenActivity;
import com.simplysmart.providesk.custom.CustomProgressDialog;
import com.simplysmart.providesk.dialog.AlertDialogStandard;
import com.simplysmart.lib.common.CommonMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shekhar on 4/8/15.
 */
public abstract class BaseFragment extends Fragment {
    protected Activity _activity;
    static final String REFERRER_SCREEN = "referrer";

    private Dialog spinningDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) _activity).getSupportActionBar().show();
        HelpDeskScreenActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        setHeaderTitle(getHeaderTitle());
        setHeaderColor(getHeaderColor());

        spinningDialog = CustomProgressDialog.showProgressDialog(_activity);
        spinningDialog.setCancelable(false);
    }

    private void setHeaderColor(int color) {
        ((AppCompatActivity) _activity).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    private void setHeaderTitle(String title) {
//        SpannableString titleString = new SpannableString(title);
//        titleString.setSpan(new TypefaceSpan(_activity, AppConstant.FONT_EUROSTILE_REGULAR_MID), 0, titleString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((AppCompatActivity) _activity).getSupportActionBar().setTitle("ProviDesk");
    }

    //Method used to parse error response from server
    private String trimMessage(String json, String key) {
        String trimmedString;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }

    //method used to show error messages
    protected void displayMessage(String errorString) {
        showMyDialog(getString(R.string.app_name), errorString, getString(R.string.ok_button));

    }

    //show common alert dialog
    public void showMyDialog(String title, String message, String positiveButton) {
        AlertDialogStandard newDialog = AlertDialogStandard.newInstance(title, message, "", positiveButton);
        newDialog.show(getFragmentManager(), "show dialog base fragment");
    }

    int roundToNearestCent(int input) {
        return ((input + 99) / 100) * 100;
    }

    protected abstract int getHeaderColor();

    protected abstract String getHeaderTitle();

    protected void showSnackBar(View view, String message) {
        Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">" + message + "</font>"), Snackbar.LENGTH_LONG).show();
        CommonMethod.hideKeyboard(_activity);
    }

    protected void showActivitySpinner() {
        if (spinningDialog != null) spinningDialog.show();
    }

    protected void dismissActivitySpinner() {
        if (spinningDialog != null) spinningDialog.dismiss();
    }
}
