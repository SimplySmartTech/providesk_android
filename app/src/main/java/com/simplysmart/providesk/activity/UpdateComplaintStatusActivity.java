package com.simplysmart.providesk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.model.helpdesk.Complaint;
import com.simplysmart.lib.model.helpdesk.ComplaintUpdateRequest;
import com.simplysmart.lib.model.helpdesk.MessageResponseClass;
import com.simplysmart.lib.model.helpdesk.PermittedActions;
import com.simplysmart.lib.request.CreateRequest;
import com.simplysmart.providesk.R;

import java.util.ArrayList;

public class UpdateComplaintStatusActivity extends BaseActivity {

    private Spinner permittedActionSpinner;
    private Complaint complaint;
    private EditText commentEditText;

    private ArrayList<String> events = new ArrayList<>();
    private ArrayList<Boolean> comment_required = new ArrayList<>();

    private RelativeLayout parentLayout;
    private Button createSubTicketButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_complaint_status);

        complaint = getIntent().getParcelableExtra("complaint");

        permittedActionSpinner = (Spinner) findViewById(R.id.permissions_spinner);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);

        createSubTicketButton = (Button) findViewById(R.id.createSubTicketButton);

        for (PermittedActions permittedActions : complaint.getPermitted_events()) {
            events.add(permittedActions.getEvent());
            comment_required.add(permittedActions.isComment_required());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, events);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        permittedActionSpinner.setAdapter(adapter);

        permittedActionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        commentEditText = (EditText) findViewById(R.id.comment_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Update");

        if (complaint.getAasm_state() != null && complaint.getAasm_state().equalsIgnoreCase("inprogress")) {
            createSubTicketButton.setVisibility(View.VISIBLE);
        } else {
            createSubTicketButton.setVisibility(View.GONE);
        }

        createSubTicketButton.setOnClickListener(createSubTicketClick);
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

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    public void callUpdate(View view) {

        String event = permittedActionSpinner.getSelectedItem().toString();
        int eventPosition = permittedActionSpinner.getSelectedItemPosition();

        Complaint complaintObject = new Complaint();
        complaintObject.setId(complaint.getId());
        complaintObject.setPriority(complaint.getPriority());
        complaintObject.setOf_type(complaint.getOf_type());
        complaintObject.setUnit_info(complaint.getUnit_info());
        complaintObject.setCategory_name(complaint.getCategory_name());
        complaintObject.setState_action(event);

        ComplaintUpdateRequest complaintUpdateRequest = new ComplaintUpdateRequest();

        String reasonText = commentEditText.getText().toString();

        if (comment_required.get(eventPosition) && commentEditText.getText().toString().trim().isEmpty()) {
            showSnackBar(parentLayout, "Please enter comment.");
            return;
        }

        switch (event) {
            case "Resolve":
                complaintObject.setResolved_reason(reasonText);

                break;
            case "Block":
                complaintObject.setBlocked_reason(reasonText);

                break;
            case "Reject":
                complaintObject.setRejected_reason(reasonText);

                break;
            case "Close":
                complaintObject.setClosed_reason(reasonText);
                break;

            case "Start":
                complaintObject.setStarted_reason(reasonText);
                break;

        }

        complaintUpdateRequest.setComplaint(complaintObject);

        showActivitySpinner();

        CreateRequest.getInstance().updateComplaintStatus(complaintObject.getId(), complaintUpdateRequest, new ApiCallback<MessageResponseClass>() {
            @Override
            public void onSuccess(MessageResponseClass response) {
                dismissActivitySpinner();
//                if (response.isSuccessful()) {
                    Toast.makeText(UpdateComplaintStatusActivity.this, "Complaint status is updated successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent("updateDetails");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                    finish();
//                } else {
//                    Toast.makeText(UpdateComplaintStatusActivity.this, "Response Error" + response.code(), Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onFailure(String error) {
                dismissActivitySpinner();
                Toast.makeText(UpdateComplaintStatusActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

    }

    View.OnClickListener createSubTicketClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent updateStatusActivity = new Intent(UpdateComplaintStatusActivity.this, CreateSubComplaintActivity.class);
            updateStatusActivity.putExtra("complaint", complaint);
            startActivity(updateStatusActivity);
        }
    };


}
