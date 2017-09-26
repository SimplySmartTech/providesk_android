package com.simplysmart.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.lib.model.notification.Notification;

/**
 * Created by shekhar on 4/8/15.
 */
public class NotificationDetailScreenPhone extends BaseFragment {

    private Activity _activity;
    private TextView notificationSubject, notificationDesc;
    private Notification notification;
    private String notificationCategory = "";
    private Typeface textTypeface;
    private RelativeLayout moreDetailButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
        textTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notification_detail_screen, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeView();
        Bundle bundle = getArguments();

        if (bundle != null) {
            notification = bundle.getParcelable(NotificationScreenPhone.NOTIFICATION_DETAIL);
            notificationCategory = bundle.getString(NotificationScreenPhone.NOTIFICATION_CATEGORY);

            if (notificationCategory != null) {
                if (!notificationCategory.equalsIgnoreCase("Electricity") && !notificationCategory.equalsIgnoreCase("Water")) {
                    moreDetailButton.setVisibility(View.INVISIBLE);
                } else {
                    moreDetailButton.setVisibility(View.VISIBLE);
                }
            } else {
                moreDetailButton.setVisibility(View.INVISIBLE);
            }
        }
        if (notification != null) {
            notificationSubject.setText(notification.getSubject());
            notificationDesc.setText(notification.getDescription());
        } else {
            throw new AssertionError();
        }

    }

    @Override
    public int getHeaderColor() {
        return _activity.getResources().getColor(R.color.bg_color);
    }

    @Override
    public String getHeaderTitle() {
        return _activity.getResources().getString(R.string.title_notification_details);
    }

    private void initializeView() {

        notificationSubject = (TextView) _activity.findViewById(R.id.notification_subject);
        notificationDesc = (TextView) _activity.findViewById(R.id.notification_desc);
        TextView notificationLogo = (TextView) _activity.findViewById(R.id.notification_logo);
        TextView more_detail_logo = (TextView) _activity.findViewById(R.id.logo_more_detail);
        moreDetailButton = (RelativeLayout) _activity.findViewById(R.id.more_info_layout);

        moreDetailButton.setOnClickListener(moreInfoClick);

        Typeface iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);
        notificationLogo.setTypeface(iconTypeface);
        more_detail_logo.setTypeface(iconTypeface);
        notificationLogo.setText(_activity.getResources().getString(R.string.icon_notification));
        more_detail_logo.setText(_activity.getResources().getString(R.string.icon_meter));

        notificationSubject.setTypeface(textTypeface);
        notificationDesc.setTypeface(textTypeface);

    }

    private final View.OnClickListener moreInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            openNotificationCategoryScreen(notificationCategory);

        }
    };

    private void openNotificationCategoryScreen(String category) {

        Fragment fragment = null;
        switch (category) {

            case "Electricity":
                fragment = new ElectricityMeterScreen();
                break;

            case "Water":
                fragment = new WaterMeterScreen();
                break;

            default:
                break;
        }
        if (fragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }

}
