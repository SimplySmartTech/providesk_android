package com.simplysmart.providesk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.activity.ComplaintDetailScreenActivity;
import com.simplysmart.providesk.adapter.NotificationListAdapter;
import com.simplysmart.providesk.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.notification.Notification;
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
public class NotificationScreenPhone extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String NOTIFICATION_DETAIL = "notification_detail";
    public static final String NOTIFICATION_CATEGORY = "category";
    private Activity _activity;
    private View rootView;
    private ListView notificationList;
    private ArrayList<Notification> notificationListData = new ArrayList<>();
    //    private ProgressBar progressBar;
    private TextView no_data_found;

    private SwipeRefreshLayout swipeRefreshLayout;

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

            swipeRefreshLayout.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            postGenericRequest();
                        }
                    }
            );
        } else {
//            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Notification notification = notificationListData.get(i);

                if (notification != null && notification.isUnread()) {
                    markAsRead(notification.getId());
                }

                if (notification.getNoticeable_type() == null) {
                    navigateToDetailScreen(notification);
                    return;
                }
                String notificationType = notification.getNoticeable_type();
                if (notificationType.toLowerCase().equalsIgnoreCase("utility")) {
                    navigateToDetailScreen(notification);
                } else {
                    Intent newIntent = new Intent(_activity, ComplaintDetailScreenActivity.class);
                    newIntent.putExtra("complaint_id", notification.getNoticeable_id());
                    _activity.startActivity(newIntent);
                }
            }
        });
    }

    private void markAsRead(String notificationId) {
        CreateRequest.getInstance()
                .updateNotificationStatus(GlobalData.getInstance().getResidentId(), notificationId, getDateCurrentTimeZone(),
                        new ApiCallback<NotificationResponse>() {
                            @Override
                            public void onSuccess(NotificationResponse response) {

                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });

    }

    private void navigateToDetailScreen(Notification notification) {

        NotificationDetailScreenPhone detailScreenPhone = new NotificationDetailScreenPhone();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTIFICATION_DETAIL, notification);
        detailScreenPhone.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, detailScreenPhone).addToBackStack(null).commit();
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
//        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        no_data_found = (TextView) rootView.findViewById(R.id.no_data_found);
    }

    private void postGenericRequest() {

//        progressBar.setVisibility(View.VISIBLE);
        no_data_found.setVisibility(View.GONE);
        CreateRequest.getInstance().getNotificationList("unread", GlobalData.getInstance().getResidentId(), new ApiCallback<NotificationResponse>() {
            @Override
            public void onSuccess(NotificationResponse response) {
//                progressBar.setVisibility(View.GONE);
                parseNotificationResponse(response);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String error) {
//                progressBar.setVisibility(View.GONE);
                no_data_found.setText(error);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void parseNotificationResponse(NotificationResponse response) {

        ArrayList<Notification> notifications = response.getData().getNotifications();

        if (notifications != null && notifications.size() > 0) {
            notificationListData = notifications;
            NotificationListAdapter listAdapter = new NotificationListAdapter(_activity, notifications);
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

    @Override
    public void onRefresh() {
        if (NetworkUtilities.isInternet(getActivity())) {
            swipeRefreshLayout.setRefreshing(true);
            postGenericRequest();
        } else {
//            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }
    }
}
