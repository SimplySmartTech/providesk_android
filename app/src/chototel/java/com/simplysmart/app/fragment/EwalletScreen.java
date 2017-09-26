package com.simplysmart.app.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.adapter.TransactionListAdapter;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.app.dialog.DatePickerFragment;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.walllet.TransactionResponse;
import com.simplysmart.lib.request.ChototelCreateRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shekhar on 4/8/15.
 */
public class EwalletScreen extends BaseFragment {

    private Activity _activity;
    private EditText enterAmount;
    private RelativeLayout topUpLayout, transactionLayout;
    private TextView switchTransactionView;
    private FrameLayout switchTopUpView;
    private TextView valAmount500, valAmount1000, valAmount2000;
    private TextView startDate, endDate;
    private Calendar c;
    private boolean isStartDate;
    private ProgressBar progressBar;
    private TextView no_data_found;

    private TextView iconWalletBalance, txtWalletBalance, valWalletBalance;

    private ListView listTransaction;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ewallet_screen, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeWidgets();

        Bundle bundle = getArguments();
        if (bundle != null) {
            // Set top-up view
            enterAmount.setText(String.valueOf(bundle.getInt("budgeted_amount")));
            topUpLayout.setVisibility(View.VISIBLE);
            transactionLayout.setVisibility(View.GONE);
            switchTransactionView.setVisibility(View.VISIBLE);
            switchTopUpView.setVisibility(View.GONE);
        } else {
            //set transaction list
            enterAmount.setText("");
            topUpLayout.setVisibility(View.GONE);
            transactionLayout.setVisibility(View.VISIBLE);
            switchTransactionView.setVisibility(View.GONE);
            switchTopUpView.setVisibility(View.VISIBLE);
        }

        if (NetworkUtilities.isInternet(_activity)) {
            getTransactionsRequest();
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }
    }

    private void initializeWidgets() {

        c = Calendar.getInstance();

        progressBar = (ProgressBar) _activity.findViewById(R.id.progressBar2);
        no_data_found = (TextView) _activity.findViewById(R.id.no_data_found);

        txtWalletBalance = (TextView) _activity.findViewById(R.id.txt_wallet_bal);
        valWalletBalance = (TextView) _activity.findViewById(R.id.val_wallet_balance);
        iconWalletBalance = (TextView) _activity.findViewById(R.id.icon_wallet_balance);
        TextView txt_transaction_caption = (TextView) _activity.findViewById(R.id.txt_transaction_caption);
        TextView txtRechargeWallet = (TextView) _activity.findViewById(R.id.txt_recharge_your_wallet);

        LinearLayout ll_date_from = (LinearLayout) _activity.findViewById(R.id.ll_date_from);
        LinearLayout ll_date_to = (LinearLayout) _activity.findViewById(R.id.ll_date_to);

        TextView icon_date_from = (TextView) _activity.findViewById(R.id.icon_date_from);
        TextView icon_date_to = (TextView) _activity.findViewById(R.id.icon_date_to);

        valAmount500 = (TextView) _activity.findViewById(R.id.val_amount_500);
        valAmount1000 = (TextView) _activity.findViewById(R.id.val_amount_1000);
        valAmount2000 = (TextView) _activity.findViewById(R.id.val_amount_2000);
        TextView buttonAddMoney = (TextView) _activity.findViewById(R.id.btn_add_money);
        TextView btn_wallet_topUp = (TextView) _activity.findViewById(R.id.btn_wallet_topup);
        TextView txt_wallet_topUp = (TextView) _activity.findViewById(R.id.txt_wallet_topup);

        switchTransactionView = (TextView) _activity.findViewById(R.id.txt_transaction);
        switchTopUpView = (FrameLayout) _activity.findViewById(R.id.layout_topup_btn);
        listTransaction = (ListView) _activity.findViewById(R.id.transaction_list);
        enterAmount = (EditText) _activity.findViewById(R.id.enter_amount);
        topUpLayout = (RelativeLayout) _activity.findViewById(R.id.layout_topup);
        transactionLayout = (RelativeLayout) _activity.findViewById(R.id.layout_transaction);

        startDate = (TextView) _activity.findViewById(R.id.val_date_from);
        endDate = (TextView) _activity.findViewById(R.id.val_date_to);

        Typeface iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);
        btn_wallet_topUp.setTypeface(iconTypeface);
        icon_date_from.setTypeface(iconTypeface);
        icon_date_to.setTypeface(iconTypeface);

        switchTransactionView.setText(_activity.getString(R.string.Rs) + " Transaction");

        switchTransactionView.setOnClickListener(switchToTransactionViewClick);
        btn_wallet_topUp.setOnClickListener(switchToTopUpViewClick);
        ll_date_from.setOnClickListener(dateFromClick);
        ll_date_to.setOnClickListener(dateToClick);

        valAmount500.setText(_activity.getString(R.string.Rs) + " 500");
        valAmount1000.setText(_activity.getString(R.string.Rs) + " 1000");
        valAmount2000.setText(_activity.getString(R.string.Rs) + " 2000");

        valAmount500.setOnClickListener(click500);
        valAmount1000.setOnClickListener(click1000);
        valAmount2000.setOnClickListener(click2000);

    }

    final private View.OnClickListener switchToTransactionViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switchTransactionView.setVisibility(View.GONE);
            switchTopUpView.setVisibility(View.VISIBLE);
            topUpLayout.setVisibility(View.GONE);
            transactionLayout.setVisibility(View.VISIBLE);
        }
    };

    final private View.OnClickListener switchToTopUpViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switchTransactionView.setVisibility(View.VISIBLE);
            switchTopUpView.setVisibility(View.GONE);
            transactionLayout.setVisibility(View.GONE);
            topUpLayout.setVisibility(View.VISIBLE);
        }
    };


    final private View.OnClickListener click500 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            valAmount500.setBackgroundResource(R.drawable.rectangular_border_yellow);
            valAmount1000.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount2000.setBackgroundResource(R.drawable.rectangular_border_light_gray);

        }
    };

    final private View.OnClickListener click1000 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            valAmount500.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount1000.setBackgroundResource(R.drawable.rectangular_border_yellow);
            valAmount2000.setBackgroundResource(R.drawable.rectangular_border_light_gray);

        }
    };

    final private View.OnClickListener click2000 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            valAmount500.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount1000.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount2000.setBackgroundResource(R.drawable.rectangular_border_yellow);
        }
    };

    final private View.OnClickListener dateFromClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isStartDate = true;
            DialogFragment newFragment = new DatePickerFragment(mDateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), isStartDate);
            newFragment.show(getFragmentManager(), "datePicker");
        }
    };

    final private View.OnClickListener dateToClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isStartDate = false;
            DialogFragment newFragment = new DatePickerFragment(mDateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), isStartDate);
            newFragment.show(getFragmentManager(), "datePicker");
        }
    };

    @Override
    public int getHeaderColor() {
        return _activity.getResources().getColor(R.color.bg_color);
    }

    @Override
    public String getHeaderTitle() {
        return _activity.getResources().getString(R.string.title_ewallet);
    }

    private DatePickerDialog.OnDateSetListener mDateListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    updateDate(year, month, day);
                }
            };

    public void updateDate(int year, int month, int day) {

        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(c.getTime());
        if (isStartDate) {
            startDate.setText(formattedDate);
        } else {
            endDate.setText(formattedDate);
        }

    }

    private void getTransactionsRequest() {

        progressBar.setVisibility(View.VISIBLE);
        no_data_found.setVisibility(View.GONE);

        ChototelCreateRequest.getInstance().getTransactions(GlobalData.getInstance().getBookingId(), new ApiCallback<TransactionResponse>() {
            @Override
            public void onSuccess(TransactionResponse response) {

                progressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    valWalletBalance.setText(response.getData().getBalance());
                    iconWalletBalance.setText(_activity.getResources().getString(R.string.Rs));
                }
                if (response.getData() != null && response.getData().getTransactions() != null && response.getData().getTransactions().size() > 0) {
                    TransactionListAdapter adapter = new TransactionListAdapter(_activity, response.getData().getTransactions());
                    listTransaction.setAdapter(adapter);
                } else {
                    no_data_found.setVisibility(View.VISIBLE);
                    no_data_found.setText("No transactions found.");
                }
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.VISIBLE);
                no_data_found.setText(error);
            }
        });
    }
}
