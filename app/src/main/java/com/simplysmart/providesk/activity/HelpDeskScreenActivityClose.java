package com.simplysmart.providesk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.helpdesk.HelpDeskData;
import com.simplysmart.lib.model.helpdesk.HelpDeskResponse;
import com.simplysmart.lib.request.CreateRequest;
import com.simplysmart.providesk.R;
import com.simplysmart.providesk.adapter.HelpdeskListAdapter;


/**
 * Created by shekhar on 11/8/15.
 */
public class HelpDeskScreenActivityClose extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView helpdeskOpenList;
    //    private ProgressBar progressBar;
    private TextView no_data_found;
    private HelpDeskData helpDeskData;
    private HelpdeskListAdapter listAdapter;

    //Pagination Configuration
    private Integer page_no = 1;
    private String complaint_status = "Resolved";
    private boolean loadingMore = false;
    private View footer;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_helpdesk_close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Complaint History");

        page_no = 1;
        loadingMore = false;
        initializeView();

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

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(onComplaintModified, new IntentFilter("onComplaintModified"));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onComplaintModified);
        super.onDestroy();
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
                break;
        }
        return true;
    }

    private void initializeView() {

        helpdeskOpenList = (ListView) findViewById(R.id.helpdesk_open_listview);
        no_data_found = (TextView) findViewById(R.id.no_data_found);

//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        //add the footer before adding the adapter, else the footer will not load!
        footer = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_progress_dialog, null, false);

        helpdeskOpenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listAdapter.getData().get(position).getAasm_state().equalsIgnoreCase("resolved")) {
                    Intent newIntent = new Intent(HelpDeskScreenActivityClose.this, ComplaintFeedbackActivity.class);
                    newIntent.putExtra("complaint_id", listAdapter.getData().get(position).getId());
                    startActivity(newIntent);

                } else {
                    if (NetworkUtilities.isInternet(HelpDeskScreenActivityClose.this)) {
                        Intent newIntent = new Intent(HelpDeskScreenActivityClose.this, ComplaintDetailScreenActivity.class);
                        newIntent.putExtra("complaint_id", listAdapter.getData().get(position).getId());
                        startActivity(newIntent);
                    } else {
//                        displayMessage(_activity.getString(R.string.error_no_internet_connection));
                    }
                }
            }
        });
        handlePagination();
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

//                    if (footer != null)
//                        helpdeskOpenList.removeFooterView(footer);

                    if (NetworkUtilities.isInternet(HelpDeskScreenActivityClose.this)) {

//                        if (page_no == 1)
//                            progressBar.setVisibility(View.VISIBLE);

//                        helpdeskOpenList.addFooterView(footer);
                        getComplaintListRequest(complaint_status, page_no);

                    } else {
                        if (page_no == 1) {
                            no_data_found.setVisibility(View.VISIBLE);
                            no_data_found.setText(getString(R.string.error_no_internet_connection));
                        }
//                        helpdeskOpenList.removeFooterView(footer);
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
//                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.GONE);
                parseComplaintListResponse(response);
            }

            @Override
            public void onFailure(String error) {
                swipeRefreshLayout.setRefreshing(false);
//                progressBar.setVisibility(View.GONE);
                DebugLog.d("Error [" + error + "]");
            }
        });
    }

    private void parseComplaintListResponse(HelpDeskResponse result) {
        helpDeskData = result.getData();
//        if (footer != null)
//            helpdeskOpenList.removeFooterView(footer);

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

//    private void requestButtonClick() {
//        if (listAdapter != null && !listAdapter.isEmpty()) {
//            listAdapter.clearData();
//            helpdeskOpenList.setAdapter(null);
//        }
//        loadingMore = false;
//        no_data_found.setVisibility(View.GONE);
//        handlePagination();
//    }

    private final BroadcastReceiver onComplaintModified = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DebugLog.d("listening onComplaintModified ");
            handlePagination();
        }
    };

    @Override
    public void onRefresh() {
        if (NetworkUtilities.isInternet(HelpDeskScreenActivityClose.this)) {
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

    @Override
    protected int getStatusBarColor() {
        return 0;
    }
}
