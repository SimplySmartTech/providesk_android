package com.simplysmart.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.adapter.NotificationListAdapter;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.notification.Notification;
import com.simplysmart.lib.model.notification.NotificationData;
import com.simplysmart.lib.model.notification.NotificationResponse;
import com.simplysmart.lib.request.CreateRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by shekhar on 4/8/15.
 */
public class NotificationScreenPhone extends BaseFragment {

    public static final String NOTIFICATION_DETAIL = "notification_detail";
    public static final String NOTIFICATION_CATEGORY = "category";
    private Activity _activity;
    private View rootView;
    private ListView notificationList;
    private NotificationData notificationData;
    private ProgressBar progressBar;
    private TextView no_data_found;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeView();

        if (NetworkUtilities.isInternet(_activity)) {
            postGenericRequest();
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }

//        notificationList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                NotificationDetailScreenPhone detailScreenPhone = new NotificationDetailScreenPhone();
//                Notification notification = notificationData.getNotifications().get(groupPosition).getData().get(childPosition);
//
//                if (notification != null && notification.getUnread().equalsIgnoreCase("true")) {
//
//                    CreateRequest.getInstance().updateNotificationStatus(GlobalData.getInstance().getResidentId(), notification.getId(), getDateCurrentTimeZone(), new ApiCallback<NotificationResponse>() {
//                        @Override
//                        public void onSuccess(NotificationResponse response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(String error) {
//
//                        }
//                    });
//                }
//
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(NOTIFICATION_DETAIL, notification);
//                bundle.putString(NOTIFICATION_CATEGORY, notificationData.getNotifications().get(groupPosition).getCategory());
//                detailScreenPhone.setArguments(bundle);
//
//                getFragmentManager().beginTransaction().replace(R.id.frame_container, detailScreenPhone).addToBackStack(null).commit();
//                return true;
//            }
//        });

    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bw_color_yellow);
    }

    @Override
    public String getHeaderTitle() {
        return _activity.getString(R.string.title_notification);
    }

    private void initializeView() {
        notificationList = (ListView) rootView.findViewById(R.id.notificationList);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        no_data_found = (TextView) rootView.findViewById(R.id.no_data_found);
    }

    private void postGenericRequest() {

        progressBar.setVisibility(View.VISIBLE);
        no_data_found.setVisibility(View.GONE);
        CreateRequest.getInstance().getNotificationList("unread",GlobalData.getInstance().getResidentId(), new ApiCallback<NotificationResponse>() {
            @Override
            public void onSuccess(NotificationResponse response) {
                progressBar.setVisibility(View.GONE);
                parseNotificationResponse(response);
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                no_data_found.setText(error);
            }
        });
    }

    private void parseNotificationResponse(NotificationResponse response) {

        ArrayList<Notification> notifications = response.getData().getNotifications();

        if (notifications != null && notifications.size() > 0) {
            NotificationListAdapter listAdapter = new NotificationListAdapter(_activity, notificationData.getNotifications());
            notificationList.setAdapter(listAdapter);
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText("No Data Found");
        }
    }

    private String getDateCurrentTimeZone() {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date currentTimeZone = calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
