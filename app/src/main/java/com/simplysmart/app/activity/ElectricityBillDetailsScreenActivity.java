package com.simplysmart.app.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.adapter.DueChargesListAdapter;
import com.simplysmart.app.util.ListHelper;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.bill.Bill;
import com.simplysmart.lib.model.bill.BillDetailResponse;
import com.simplysmart.lib.model.bill.DueCharge;
import com.simplysmart.lib.request.CreateRequest;

import java.util.ArrayList;


public class ElectricityBillDetailsScreenActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextView no_data_found;

    private LinearLayout mHeaderBar;
    private android.support.v7.widget.Toolbar mToolbar;
    private RelativeLayout mContent_layout;
    private LinearLayout mRow1;
    private EditText mBillingUnit;
    private EditText mStatus;
    private LinearLayout mRow2;
    private EditText mName;
    private EditText mBillFor;
    private TextView mSep1;
    private LinearLayout mRow3;
    private EditText mBillNumber;
    private EditText mBillDate;
    private LinearLayout mRow4;
    private EditText mBillPeriod;
    private EditText mDueDate;
    private TextView mSep2;
    private ListView mBillChargesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill_details_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Bill Details");

        initializeWidget();

        if (NetworkUtilities.isInternet(this)) {
            String billId;
            if (getIntent().getStringExtra("billId") != null) {
                billId = getIntent().getStringExtra("billId");
                getElectricityBillDataRequest(billId);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.bw_color_yellow;
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


    private void initializeWidget() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        no_data_found = (TextView) findViewById(R.id.no_data_found);

        mHeaderBar = (LinearLayout) findViewById(R.id.headerBar);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mContent_layout = (RelativeLayout) findViewById(R.id.content_layout);
        mRow1 = (LinearLayout) findViewById(R.id.row1);
        mBillingUnit = (EditText) findViewById(R.id.billingUnit);
        mStatus = (EditText) findViewById(R.id.status);
        mRow2 = (LinearLayout) findViewById(R.id.row2);
        mName = (EditText) findViewById(R.id.name);
        mBillFor = (EditText) findViewById(R.id.billFor);
        mSep1 = (TextView) findViewById(R.id.sep1);
        mRow3 = (LinearLayout) findViewById(R.id.row3);
        mBillNumber = (EditText) findViewById(R.id.billNumber);
        mBillDate = (EditText) findViewById(R.id.billDate);
        mRow4 = (LinearLayout) findViewById(R.id.row4);
        mBillPeriod = (EditText) findViewById(R.id.billPeriod);
        mDueDate = (EditText) findViewById(R.id.dueDate);
        mSep2 = (TextView) findViewById(R.id.sep2);
        mBillChargesList = (ListView) findViewById(R.id.billChargesList);

    }

    private void getElectricityBillDataRequest(String billId) {
        showActivitySpinner();
        CreateRequest.getInstance().getBillDetails(billId, new ApiCallback<BillDetailResponse>() {
            @Override
            public void onSuccess(BillDetailResponse response) {

                dismissActivitySpinner();
                setBillData(response.getData().getBill());
            }

            @Override
            public void onFailure(String error) {
                dismissActivitySpinner();
            }
        });
    }

    private void setBillData(Bill bill) {

        mBillingUnit.setText(bill.getUnit_info());
        mStatus.setText(bill.getAasm_state());
        mName.setText(bill.getName());
        mBillFor.setText(bill.getUtility_type());

        mBillNumber.setText(bill.getBill_number());
        mBillDate.setText(bill.getBill_date());
        mBillPeriod.setText(bill.getStart_date() + " to " + bill.getEnd_date());
        mDueDate.setText(bill.getDue_date_formatted());

        ArrayList<DueCharge> dueCharges = new ArrayList<>();

        DueCharge totalUnit = new DueCharge();
        totalUnit.setName("Total Units");

        if (bill.getUnits_consumed() != null && !bill.getUnits_consumed().equalsIgnoreCase("")) {
            totalUnit.setDue_amount(String.format("%.2f", Double.parseDouble(bill.getUnits_consumed())));
        } else {
            totalUnit.setDue_amount("");
        }

        DueCharge energyCharges = new DueCharge();
        energyCharges.setName("Energy Charges");
        energyCharges.setDue_amount(bill.getEnergy_charge());

        DueCharge taxAmount = new DueCharge();
        taxAmount.setName("Tax Amount");
        taxAmount.setDue_amount(bill.getTax_amount());

        dueCharges.add(totalUnit);
        dueCharges.add(energyCharges);
        dueCharges.add(taxAmount);

        if (bill.getDue_charges() != null && bill.getDue_charges().size() > 0) {
            dueCharges.addAll(bill.getDue_charges());
        }

        DueCharge netAmount = new DueCharge();
        netAmount.setName("Net amount");
        netAmount.setDue_amount(bill.getNet_amount());

        dueCharges.add(netAmount);

        DueChargesListAdapter adapter =
                new DueChargesListAdapter(ElectricityBillDetailsScreenActivity.this, dueCharges);
        mBillChargesList.setAdapter(adapter);
        ListHelper.getListViewSize(mBillChargesList);
    }
}
