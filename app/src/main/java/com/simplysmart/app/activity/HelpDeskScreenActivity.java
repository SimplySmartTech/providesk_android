package com.simplysmart.app.activity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.adapter.HelpdeskListAdapter;
import com.simplysmart.app.fragment.HelpDeskScreenClose;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.helpdesk.HelpDeskData;
import com.simplysmart.lib.model.helpdesk.HelpDeskResponse;
import com.simplysmart.lib.request.CreateRequest;

/**
 * Created by shekhar on 23/4/17.
 */
public class HelpDeskScreenActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private boolean isHome;

    private ListView helpdeskOpenList;
    private TextView no_data_found;
    private HelpDeskData helpDeskData;
    private HelpdeskListAdapter listAdapter;

    //Pagination Configuration
    private Integer page_no = 1;
    private String complaint_status = "Pending";
    private boolean loadingMore = false;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean once;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpdesk_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Helpdesk");

        isHome = true;
        initializeView();

        page_no = 1;
        loadingMore = false;

        //Used to listen broadcast receiver
        once = true;

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        handlePagination();
                    }
                }
        );
    }

    private void initializeView() {

        helpdeskOpenList = (ListView) findViewById(R.id.helpdesk_open_listview);
        no_data_found = (TextView) findViewById(R.id.no_data_found);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        helpdeskOpenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listAdapter.getData().get(position).getAasm_state().equalsIgnoreCase("resolved")) {
                    Intent newIntent = new Intent(HelpDeskScreenActivity.this, ComplaintFeedbackActivity.class);
                    newIntent.putExtra("complaint_id", listAdapter.getData().get(position).getId());
                    startActivity(newIntent);

                } else {
                    if (NetworkUtilities.isInternet(HelpDeskScreenActivity.this)) {
                        Intent newIntent = new Intent(HelpDeskScreenActivity.this, ComplaintDetailScreenActivity.class);
                        newIntent.putExtra("complaint_id", listAdapter.getData().get(position).getId());
                        startActivity(newIntent);
                    }
                }
            }
        });

        FloatingActionButton newButton = (FloatingActionButton) findViewById(R.id.newButton);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(HelpDeskScreenActivity.this, CreateComplaintActivity.class);
                startActivity(newIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(onComplaintModified, new IntentFilter("onComplaintModified"));
        LocalBroadcastManager.getInstance(this).registerReceiver(UpdateDetails, new IntentFilter("UpdateDetails"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NetworkUtilities.isInternet(HelpDeskScreenActivity.this)) {
            if (listAdapter != null && !listAdapter.isEmpty()) {
                listAdapter.clearData();
                helpdeskOpenList.setAdapter(null);
            }
            page_no = 1;
            loadingMore = false;
            swipeRefreshLayout.setRefreshing(true);
            handlePagination();
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onComplaintModified);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(UpdateDetails);
        super.onDestroy();
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.colorPrimary;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_helpdesk, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem reset = menu.findItem(R.id.action_history);
        reset.setVisible(isHome);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                isHome = true;
                invalidateOptionsMenu();
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
                break;
            case R.id.action_history:
                isHome = false;
                invalidateOptionsMenu();
                Fragment historyComplaint = new HelpDeskScreenClose();
                getFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.llContent, historyComplaint).commitAllowingStateLoss();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        isHome = true;
        invalidateOptionsMenu();
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void handlePagination() {

        helpdeskOpenList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int lastInScreen = firstVisibleItem + visibleItemCount;

                if ((lastInScreen == totalItemCount && !loadingMore)) {

                    loadingMore = true;

                    if (NetworkUtilities.isInternet(HelpDeskScreenActivity.this)) {
                        getComplaintListRequest(complaint_status, page_no);
                    } else {
                        if (page_no == 1) {
                            no_data_found.setVisibility(View.VISIBLE);
                            no_data_found.setText(getString(R.string.error_no_internet_connection));
                        }
                    }
                }
            }
        });
    }

    private void getComplaintListRequest(String state, int page_no) {

        CreateRequest.getInstance().getComplaintList(state, page_no, new ApiCallback<HelpDeskResponse>() {
            @Override
            public void onSuccess(HelpDeskResponse response) {
                swipeRefreshLayout.setRefreshing(false);
                no_data_found.setVisibility(View.GONE);
                parseComplaintListResponse(response);
            }

            @Override
            public void onFailure(String error) {
                swipeRefreshLayout.setRefreshing(false);
                DebugLog.d("Error [" + error + "]");
            }
        });
    }

    private void parseComplaintListResponse(HelpDeskResponse result) {
        helpDeskData = result.getData();
        if (helpDeskData.hasComplaints()) {

            if (listAdapter == null || listAdapter.isEmpty()) {
                listAdapter = new HelpdeskListAdapter(this, helpDeskData.getComplaintLists());
                helpdeskOpenList.setAdapter(listAdapter);
            } else {
                listAdapter.addData((helpDeskData.getComplaintLists()));
                listAdapter.notifyDataSetChanged();
            }
            page_no = page_no + 1;
            loadingMore = false;
        } else {
            if (page_no == 1) {
                no_data_found.setVisibility(View.VISIBLE);
                no_data_found.setText("No data found");
            }
            loadingMore = true;
        }
    }

    private final BroadcastReceiver onComplaintModified = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DebugLog.d("listening onComplaintModified ");
            handlePagination();
        }
    };

    @Override
    public void onRefresh() {
        if (NetworkUtilities.isInternet(HelpDeskScreenActivity.this)) {
            if (listAdapter != null && !listAdapter.isEmpty()) {
                listAdapter.clearData();
                helpdeskOpenList.setAdapter(null);
            }
            page_no = 1;
            loadingMore = false;
            swipeRefreshLayout.setRefreshing(true);
            handlePagination();
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(this.getString(R.string.error_no_internet_connection));
        }
    }

    private BroadcastReceiver UpdateDetails = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (once) {
                if (NetworkUtilities.isInternet(HelpDeskScreenActivity.this)) {
                    if (listAdapter != null && !listAdapter.isEmpty()) {
                        listAdapter.clearData();
                        helpdeskOpenList.setAdapter(null);
                    }
                    page_no = 1;
                    loadingMore = false;
                    swipeRefreshLayout.setRefreshing(true);
                    handlePagination();
                } else {
                    no_data_found.setVisibility(View.VISIBLE);
                    no_data_found.setText(getString(R.string.error_no_internet_connection));
                }
                once = false;
            }
        }
    };
}
