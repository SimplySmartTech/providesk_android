package com.simplysmart.app.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.lib.model.notification.Notification;

/**
 * Created by shekhar on 23/4/17.
 */
public class NotificationDetailScreenActivity extends BaseActivity {

    private TextView notificationSubject;
    private TextView notificationDesc;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Notification Details");

        initializeView();

        if (getIntent() != null) {
            notification = getIntent().getParcelableExtra("notification");

            if (notification != null) {
                notificationSubject.setText(notification.getSubject());
                notificationDesc.setText(notification.getDescription());
            } else {
                throw new AssertionError();
            }
        }
    }

    private void initializeView() {
        notificationSubject = (TextView) findViewById(R.id.notification_subject);
        notificationDesc = (TextView) findViewById(R.id.notification_desc);
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.bw_color_red;
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
}
