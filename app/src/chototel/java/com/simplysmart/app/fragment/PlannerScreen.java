package com.simplysmart.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.login.Resident;
import com.simplysmart.lib.request.ChototelCreateRequest;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shekhar on 13/8/15.
 * PlannerScreen
 */
public class PlannerScreen extends BaseFragment {

    private Activity _activity;
    private View rootView;

    private TextView checkInDate, checkOutDate;
    private TextView residentName;
    private TextView residentPhone;
    private TextView residentEmail;

    private CircleImageView profilePhoto;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_planner_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeWidget();

        if (NetworkUtilities.isInternet(_activity)) {
            getProfileRequest();
        }
        getUserPreferences();

    }

    @Override
    protected int getHeaderColor() {
        return _activity.getResources().getColor(R.color.bg_color);
    }

    @Override
    protected String getHeaderTitle() {
        return "Profile";
    }

    private void initializeWidget() {
        residentName = (TextView) rootView.findViewById(R.id.residentName);
        residentPhone = (TextView) rootView.findViewById(R.id.residentPhone);
        residentEmail = (TextView) rootView.findViewById(R.id.residentEmail);

        checkInDate = (TextView) rootView.findViewById(R.id.checkInDate);
        checkOutDate = (TextView) rootView.findViewById(R.id.checkOutDate);

        profilePhoto = (CircleImageView) rootView.findViewById(R.id.profilePhoto);
    }

    private void getProfileRequest() {

        ChototelCreateRequest.getInstance().getProfileInfo(GlobalData.getInstance().getResidentId(), new ApiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse response) {
                setUserData(response);
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    private void setUserData(LoginResponse response) {

        Resident residentData = response.getData().getResident();

        SharedPreferences UserInfo = _activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = UserInfo.edit();

        preferencesEditor.putBoolean("isLogin", true);

        preferencesEditor.putString("name", residentData.getName());
        preferencesEditor.putString("email", residentData.getEmail());
        preferencesEditor.putString("mobile", residentData.getMobile());
        preferencesEditor.putString("checkin_date", residentData.getCheckin_date());
        preferencesEditor.putString("checkout_date", residentData.getCheckout_date());

        preferencesEditor.putString("profile_photo_url", "");

        preferencesEditor.apply();

        getUserPreferences();
    }

    private void getUserPreferences() {

        SharedPreferences UserInfo = _activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        residentName.setText(UserInfo.getString("name", "---"));
        residentPhone.setText(UserInfo.getString("mobile", "---"));
        residentEmail.setText(UserInfo.getString("email", "---"));

        checkInDate.setText(UserInfo.getString("checkin_date", "---"));
        checkOutDate.setText(UserInfo.getString("checkout_date", "---"));

        String photoUrl = UserInfo.getString("profile_photo_url", "");

        if (!photoUrl.equalsIgnoreCase("")) {
            Picasso.with(getActivity()).load(photoUrl).placeholder(R.drawable.circle_white)
                    .error(R.drawable.circle_white).into(profilePhoto);
        }
    }
}
