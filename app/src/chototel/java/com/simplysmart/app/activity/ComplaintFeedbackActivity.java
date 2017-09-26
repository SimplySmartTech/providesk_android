package com.simplysmart.app.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.helpdesk.Complaint;
import com.simplysmart.lib.model.helpdesk.ComplaintDetailResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintFeedback;
import com.simplysmart.lib.model.helpdesk.ComplaintFeedbackRequest;
import com.simplysmart.lib.model.helpdesk.ComplaintFeedbackResponse;
import com.simplysmart.lib.request.CreateRequest;

/**
 * Created by chandrashekhar on 4/8/15.
 */
public class ComplaintFeedbackActivity extends BaseActivity {

    private Typeface textTypeface;
    private String complaint_id = "";
    private LinearLayout ll_layout;
    private Complaint complaint;
    private EditText editComment;
    private CheckBox check_not_satisfy;
    private RatingBar rate_value;
    private TextView complaintStatus, textSubcategory, textUnitNo, textComplaintNo, btn_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(GlobalData.getInstance().getUpdatedUnitName());

        initializeView();
        CommonMethod.hideKeyboard(this);

        if (getIntent().getExtras() != null) {
            complaint_id = getIntent().getStringExtra("complaint_id");
            getComplaintDetail(complaint_id);
        }
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

    private void initializeView() {

        textTypeface = Typeface.createFromAsset(getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);

        ll_layout = (LinearLayout) findViewById(R.id.ll_layout);

        TextView category_logo = (TextView) findViewById(R.id.category_logo);
        TextView unit_logo = (TextView) findViewById(R.id.unit_logo);

        textSubcategory = (TextView) findViewById(R.id.txt_subcategory);
        textUnitNo = (TextView) findViewById(R.id.txt_unit_no);
        textComplaintNo = (TextView) findViewById(R.id.complaint_no);
        complaintStatus = (TextView) findViewById(R.id.complaint_status);
        btn_submit = (TextView) findViewById(R.id.btn_submit);

        editComment = (EditText) findViewById(R.id.edit_comment);
        check_not_satisfy = (CheckBox) findViewById(R.id.check_not_satisfy);
        rate_value = (RatingBar) findViewById(R.id.rate_value);

        Typeface iconTypeface = Typeface.createFromAsset(getAssets(), AppConstant.FONT_BOTSWORTH);
        complaintStatus.setTypeface(iconTypeface);
        textSubcategory.setTypeface(textTypeface);
        textUnitNo.setTypeface(textTypeface);
        textComplaintNo.setTypeface(textTypeface);
        editComment.setTypeface(textTypeface);

        category_logo.setText(getString(R.string.icon_electricity));
        unit_logo.setText(getString(R.string.icon_myflat));


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFeedbackRequest(complaint_id);
            }
        });

    }

    private void getComplaintDetail(String compliant_id) {

        CreateRequest.getInstance().getComplaintDetails(compliant_id, new ApiCallback<ComplaintDetailResponse>() {
            @Override
            public void onSuccess(ComplaintDetailResponse response) {
                parseComplaintDetailResponse(response);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void parseComplaintDetailResponse(ComplaintDetailResponse result) {
        complaint = result.getData().getComplaint();
        if (complaint != null) {
            setComplaintInfo();
        }
    }

    private void setComplaintInfo() {
        complaintStatus.setText(getString(R.string.icon_assign) + complaint.getAasm_state());
        textSubcategory.setText(complaint.getSub_category_name());
        textComplaintNo.setText("# " + complaint.getNumber());
        textUnitNo.setText(complaint.getUnit_info());
    }

    private void callFeedbackRequest(String complaint_id) {

        ComplaintFeedbackRequest feedbackRequest = new ComplaintFeedbackRequest();
        ComplaintFeedback request = new ComplaintFeedback();
        request.setClosed_reason(editComment.getText().toString().trim());

        if (check_not_satisfy.isChecked()) {
            request.setState_action("Reject");
            request.setIs_resident_satisfied("false");
        } else {
            request.setState_action("Close");
            request.setIs_resident_satisfied("true");
        }

        feedbackRequest.setComplaint(request);

        if (NetworkUtilities.isInternet(ComplaintFeedbackActivity.this)) {

            showActivitySpinner();

            CreateRequest.getInstance().giveFeedback(complaint_id, feedbackRequest, new ApiCallback<ComplaintFeedbackResponse>() {
                @Override
                public void onSuccess(ComplaintFeedbackResponse response) {
                    dismissActivitySpinner();
                    if (response != null) {
                        Toast.makeText(ComplaintFeedbackActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("onComplaintModified");
                        LocalBroadcastManager.getInstance(ComplaintFeedbackActivity.this).sendBroadcast(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(String error) {
                    dismissActivitySpinner();
                    try {
                        Toast.makeText(ComplaintFeedbackActivity.this, trimMessage(error, "message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ComplaintFeedbackActivity.this, getString(R.string.error_in_network), Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
//            apiInterface.doFeedbackComplaint(
//                    AppSessionData.getInstance().getSubdomain(),
//                    complaint_id,
//                    feedbackRequest,
//                    new retrofit.Callback<ComplaintFeedbackResponse>() {
//                        @Override
//                        public void success(ComplaintFeedbackResponse data, Response response) {
//                            dismissActivitySpinner();
//                            if (data != null) {
//                                Toast.makeText(ComplaintFeedbackActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent("onComplaintModified");
//                                LocalBroadcastManager.getInstance(ComplaintFeedbackActivity.this).sendBroadcast(intent);
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            dismissActivitySpinner();
//                            try {
//                                if (error.getResponse() != null && error.getResponse().getBody() != null) {
//                                    String message = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
//                                    Toast.makeText(ComplaintFeedbackActivity.this, trimMessage(message, "message"), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(ComplaintFeedbackActivity.this, getString(R.string.error_in_network), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Toast.makeText(ComplaintFeedbackActivity.this, getString(R.string.error_in_network), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
        } else {
            DebugLog.d(getString(R.string.error_no_internet_connection));
        }

    }

}
