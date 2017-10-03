package com.simplysmart.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.adapter.NotificationListAdapter;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.global.AppSessionData;
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
 * Created by shekhar on 23/4/17.
 */
public class NotificationScreenActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Notification> notificationListData = new ArrayList<>();

    private ListView notificationList;
    private TextView no_data_found;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NotificationListAdapter listAdapter;

    private boolean isFromPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        if (getIntent() != null && getIntent().getExtras() != null) {
            isFromPush = getIntent().getBooleanExtra("UPDATED_FROM_PUSH", false);
            if (isFromPush) loadUserData();
        }

        initializeView();

        if (NetworkUtilities.isInternet(this)) {

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
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(this.getString(R.string.error_no_internet_connection));
        }

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Notification notification = notificationListData.get(i);
                Intent newIntent;

                if (notification != null && notification.isUnread()) {
                    notificationListData.get(i).setUnread(false);
                    if (listAdapter != null) {
                        listAdapter.notifyDataSetChanged();
                    }
                    markAsRead(notification.getId());
                }

                if (notification.getNoticeable_type() == null) {
                    newIntent = new Intent(NotificationScreenActivity.this, NotificationDetailScreenActivity.class);
                    newIntent.putExtra("notification", notification);
                    startActivity(newIntent);
                    return;
                }

                String notificationType = notification.getNoticeable_type();

                if (notificationType.toLowerCase().equalsIgnoreCase("utility")) {
                    newIntent = new Intent(NotificationScreenActivity.this, NotificationDetailScreenActivity.class);
                    newIntent.putExtra("notification", notification);
                } else {
                    newIntent = new Intent(NotificationScreenActivity.this, ComplaintDetailScreenActivity.class);
                    newIntent.putExtra("complaint_id", notification.getNoticeable_id());
                }
                startActivity(newIntent);
            }
        });

    }

    private void loadUserData() {
        SharedPreferences UserInfo = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        AppSessionData.getInstance().setAuthToken(UserInfo.getString("auth_token", ""));
        AppSessionData.getInstance().setApiKey(UserInfo.getString("api_key", ""));
        AppSessionData.getInstance().setSubdomain(UserInfo.getString("subdomain", ""));
    }

    private void markAsRead(String notificationId) {
        CreateRequest.getInstance().updateNotificationStatus(
                GlobalData.getInstance().getResidentId(), notificationId, getDateCurrentTimeZone(),
                new ApiCallback<NotificationResponse>() {
                    @Override
                    public void onSuccess(NotificationResponse response) {

                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
    }

    private void initializeView() {
        notificationList = (ListView) findViewById(R.id.notificationList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        no_data_found = (TextView) findViewById(R.id.no_data_found);
    }

    private void postGenericRequest() {

        no_data_found.setVisibility(View.GONE);
        CreateRequest.getInstance().getNotificationList("unread",
                GlobalData.getInstance().getResidentId(), new ApiCallback<NotificationResponse>() {
                    @Override
                    public void onSuccess(NotificationResponse response) {
                        parseNotificationResponse(response);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(String error) {
                        no_data_found.setText(error);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void parseNotificationResponse(NotificationResponse response) {

        ArrayList<Notification> notifications = response.getData().getNotifications();

        if (notifications != null && notifications.size() > 0) {
            notificationListData = notifications;
            listAdapter = new NotificationListAdapter(this, notifications);
            notificationList.setAdapter(listAdapter);
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText("No new notifications.");
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
        if (NetworkUtilities.isInternet(this)) {
            swipeRefreshLayout.setRefreshing(true);
            postGenericRequest();
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(this.getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(UpdateNotification,
                new IntentFilter("UpdateNotification"));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(UpdateNotification);
        super.onDestroy();
    }


    @Override
    protected int getStatusBarColor() {
        return R.color.colorPrimaryDark;
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

    private BroadcastReceiver UpdateNotification = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            postGenericRequest();
        }
    };
}
