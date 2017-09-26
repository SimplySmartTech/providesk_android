package com.simplysmart.app.fragment;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.activity.AddNewComplaintActivity;
import com.simplysmart.app.activity.ComplaintDetailScreenActivity;
import com.simplysmart.app.activity.ComplaintFeedbackActivity;
import com.simplysmart.app.adapter.HelpdeskListAdapter;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.helpdesk.HelpDeskData;
import com.simplysmart.lib.model.helpdesk.HelpDeskResponse;
import com.simplysmart.lib.request.CreateRequest;


/**
 * Created by shekhar on 17/1/17.
 */
public class HelpDeskScreenPhone extends BaseFragment {

    private View view;
    private TextView openButton;
    private TextView closeButton;
    private ListView helpdeskOpenList;
    private TextView unitName;
    private ProgressBar progressBar;
    private TextView no_data_found;
    private HelpDeskData helpDeskData;
    private HelpdeskListAdapter listAdapter;

    //Pagination Configuration
    private Integer page_no = 1;
    private String complaint_status = "Pending";
    private boolean loadingMore = false;
    private View footer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_helpdesk_screen, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        page_no = 1;
        complaint_status = "Pending";
        loadingMore = false;
        initializeView();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(_activity).registerReceiver(onComplaintModified, new IntentFilter("onComplaintModified"));
    }

    @Override
    public void onResume() {
        super.onResume();
        unitName.setText(GlobalData.getInstance().getUpdatedUnitName());
        LocalBroadcastManager.getInstance(_activity).registerReceiver(onUnitSelection, new IntentFilter("UnitName"));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(_activity).unregisterReceiver(onUnitSelection);
        LocalBroadcastManager.getInstance(_activity).unregisterReceiver(onComplaintModified);
        super.onDestroy();
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bg_color);
    }

    @Override
    public String getHeaderTitle() {
        return _activity.getString(R.string.title_helpdesk);
    }

    private void initializeView() {

        helpdeskOpenList = (ListView) view.findViewById(R.id.helpdesk_open_listview);
        openButton = (TextView) view.findViewById(R.id.btn_open);
        closeButton = (TextView) view.findViewById(R.id.btn_close);
        TextView addButton = (TextView) view.findViewById(R.id.btn_add);
        TextView helpdesk_logo = (TextView) view.findViewById(R.id.helpdesk_logo);
        unitName = (TextView) view.findViewById(R.id.unit_name);
        no_data_found = (TextView) _activity.findViewById(R.id.no_data_found);

        openButton.setOnClickListener(openListClick);
        closeButton.setOnClickListener(closeListClick);
        addButton.setOnClickListener(addNewClick);

        Typeface iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);
        helpdesk_logo.setTypeface(iconTypeface);
        addButton.setTypeface(iconTypeface);
        openButton.setTypeface(iconTypeface);
        closeButton.setTypeface(iconTypeface);

        helpdesk_logo.setText(_activity.getString(R.string.icon_helpdesk));
        openButton.setText(_activity.getString(R.string.icon_unlock) + " " + _activity.getString(R.string.txt_open));
        closeButton.setText(_activity.getString(R.string.icon_lock) + " " + _activity.getString(R.string.txt_close));
        addButton.setText(_activity.getString(R.string.icon_add));

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);

        //add the footer before adding the adapter, else the footer will not load!
        footer = ((LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_progress_dialog, null, false);

        helpdeskOpenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listAdapter.getData().get(position).getAasm_state().equalsIgnoreCase("resolved")) {
                    Intent newIntent = new Intent(_activity, ComplaintFeedbackActivity.class);
                    newIntent.putExtra("complaint_id", listAdapter.getData().get(position).getId());
                    _activity.startActivity(newIntent);

                } else {
                    if (NetworkUtilities.isInternet(_activity)) {
                        Intent newIntent = new Intent(_activity, ComplaintDetailScreenActivity.class);
                        newIntent.putExtra("complaint_id", listAdapter.getData().get(position).getId());
                        _activity.startActivity(newIntent);
                    } else {
                        displayMessage(_activity.getString(R.string.error_no_internet_connection));
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

                    if (footer != null)
                        helpdeskOpenList.removeFooterView(footer);

                    if (NetworkUtilities.isInternet(_activity)) {

                        if (page_no == 1)
                            progressBar.setVisibility(View.VISIBLE);

                        helpdeskOpenList.addFooterView(footer);
                        getComplaintListRequest(complaint_status, page_no);

                    } else {
                        if (page_no == 1) {
                            no_data_found.setVisibility(View.VISIBLE);
                            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
                        }
                        helpdeskOpenList.removeFooterView(footer);
                    }
                }
            }
        });
    }

    private void getComplaintListRequest(String state, int page_no) {

        CreateRequest.getInstance().getComplaintList(state, page_no, new ApiCallback<HelpDeskResponse>() {
            @Override
            public void onSuccess(HelpDeskResponse response) {
                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.GONE);
                parseComplaintListResponse(response);
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                DebugLog.d("Error [" + error + "]");
            }
        });
    }

    private void parseComplaintListResponse(HelpDeskResponse result) {
        helpDeskData = result.getData();
        if (footer != null)
            helpdeskOpenList.removeFooterView(footer);

        if (helpDeskData.hasComplaints()) {

            if (listAdapter == null || listAdapter.isEmpty()) {
                listAdapter = new HelpdeskListAdapter(_activity, helpDeskData.getComplaintLists());
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

    private final View.OnClickListener openListClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            openButton.setEnabled(false);
            closeButton.setEnabled(true);
            complaint_status = "Pending";
            page_no = 1;

            openButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bg_color));
            closeButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));

            requestButtonClick();
        }
    };

    private final View.OnClickListener closeListClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            openButton.setEnabled(true);
            closeButton.setEnabled(false);
            complaint_status = "Resolved";
            page_no = 1;

            openButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            closeButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bg_color));

            requestButtonClick();
        }
    };

    private void requestButtonClick() {

        if (listAdapter != null && !listAdapter.isEmpty()) {
            listAdapter.clearData();
            helpdeskOpenList.setAdapter(null);
        }
        loadingMore = false;
        no_data_found.setVisibility(View.GONE);
        handlePagination();
    }

    private final View.OnClickListener addNewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newIntent = new Intent(_activity, AddNewComplaintActivity.class);
            _activity.startActivity(newIntent);
        }
    };

    private final BroadcastReceiver onUnitSelection = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0 && fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName() != null &&
                    fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(HelpDeskScreenPhone.class.getSimpleName())) {

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(_activity.getString(R.string.title_helpdesk));
                unitName.setText(intent.getStringExtra("name"));
            }
        }
    };

    private final BroadcastReceiver onComplaintModified = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DebugLog.d("listening onComplaintModified ");
            handlePagination();
        }
    };
}
