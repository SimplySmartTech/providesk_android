package com.simplysmart.app.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.simplysmart.app.R;
import com.simplysmart.app.adapter.TransactionListAdapter;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.app.dialog.AlertDialogConfirmPayment;
import com.simplysmart.app.dialog.DatePickerFragment;
import com.simplysmart.app.dialog.TimePickerFragment;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.login.Resident;
import com.simplysmart.lib.model.walllet.TransactionResponse;
import com.simplysmart.lib.model.walllet.WalletCredentialRequest;
import com.simplysmart.lib.model.walllet.WalletCredentialResponse;
import com.simplysmart.lib.request.CreateRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shekhar on 4/8/15.
 */
public class EwalletScreen extends BaseFragment {

    int mStackLevel = 0;
    public static final int CONFIRM_DIALOG = 1;

    private Typeface textTypeface;
    private EditText enterAmount;
    private RelativeLayout topUpLayout, transactionLayout;
    private TextView switchTransactionView;
    private FrameLayout switchTopUpView;
    private TextView valAmount500, valAmount1000, valAmount2000;
    private TextView startDate, endDate;
    private Calendar c;
    private boolean isStartDate;

    private ListView listTransaction;
    private ProgressBar progressBar;
    private TextView no_data_found;

    private TextView iconWalletBalance, txtWalletBalance, valWalletBalance;

    private RelativeLayout ll_parent;
    private View rootView;

    private String transactionId = "";

    private boolean updateFlag = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        textTypeface = Typeface.createFromAsset(getActivity().getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ewallet_screen, container, false);
        return rootView;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(updatePayment, new IntentFilter("updatePayment"));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(updatePayment);
        super.onDestroy();
    }

    private void initializeWidgets() {

        c = Calendar.getInstance();

        ll_parent = (RelativeLayout) rootView.findViewById(R.id.ll_parent);

        txtWalletBalance = (TextView) rootView.findViewById(R.id.txt_wallet_bal);
        valWalletBalance = (TextView) rootView.findViewById(R.id.val_wallet_balance);
        iconWalletBalance = (TextView) _activity.findViewById(R.id.icon_wallet_balance);

        TextView txt_transaction_caption = (TextView) rootView.findViewById(R.id.txt_transaction_caption);
        TextView txtRechargeWallet = (TextView) rootView.findViewById(R.id.txt_recharge_your_wallet);

        LinearLayout ll_date_from = (LinearLayout) rootView.findViewById(R.id.ll_date_from);
        LinearLayout ll_date_to = (LinearLayout) rootView.findViewById(R.id.ll_date_to);

        TextView icon_date_from = (TextView) rootView.findViewById(R.id.icon_date_from);
        TextView icon_date_to = (TextView) rootView.findViewById(R.id.icon_date_to);

        valAmount500 = (TextView) rootView.findViewById(R.id.val_amount_500);
        valAmount1000 = (TextView) rootView.findViewById(R.id.val_amount_1000);
        valAmount2000 = (TextView) rootView.findViewById(R.id.val_amount_2000);

        TextView buttonAddMoney = (TextView) rootView.findViewById(R.id.btn_add_money);
        TextView btn_wallet_topUp = (TextView) rootView.findViewById(R.id.btn_wallet_topup);
        TextView txt_wallet_topUp = (TextView) rootView.findViewById(R.id.txt_wallet_topup);

        switchTransactionView = (TextView) rootView.findViewById(R.id.txt_transaction);
        switchTopUpView = (FrameLayout) rootView.findViewById(R.id.layout_topup_btn);

        listTransaction = (ListView) rootView.findViewById(R.id.transaction_list);
        progressBar = (ProgressBar) _activity.findViewById(R.id.progressBar2);
        no_data_found = (TextView) _activity.findViewById(R.id.no_data_found);

        enterAmount = (EditText) rootView.findViewById(R.id.enter_amount);
        topUpLayout = (RelativeLayout) rootView.findViewById(R.id.layout_topup);
        transactionLayout = (RelativeLayout) rootView.findViewById(R.id.layout_transaction);

        startDate = (TextView) rootView.findViewById(R.id.val_date_from);
        endDate = (TextView) rootView.findViewById(R.id.val_date_to);

        txtWalletBalance.setTypeface(textTypeface);
        valWalletBalance.setTypeface(textTypeface);
        txtRechargeWallet.setTypeface(textTypeface);
        txt_transaction_caption.setTypeface(textTypeface);
        switchTransactionView.setTypeface(textTypeface);

        valAmount500.setTypeface(textTypeface);
        valAmount1000.setTypeface(textTypeface);
        valAmount2000.setTypeface(textTypeface);
        buttonAddMoney.setTypeface(textTypeface);
        enterAmount.setTypeface(textTypeface);

        Typeface iconTypeface = Typeface.createFromAsset(getActivity().getAssets(), AppConstant.FONT_BOTSWORTH);
        btn_wallet_topUp.setTypeface(iconTypeface);
        icon_date_from.setTypeface(iconTypeface);
        icon_date_to.setTypeface(iconTypeface);

        txt_wallet_topUp.setTypeface(textTypeface);

        switchTransactionView.setText(getActivity().getString(R.string.Rs) + " Transaction");

        switchTransactionView.setOnClickListener(switchToTransactionViewClick);
        btn_wallet_topUp.setOnClickListener(switchToTopUpViewClick);
        ll_date_from.setOnClickListener(dateFromClick);
        ll_date_to.setOnClickListener(dateToClick);

        buttonAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topUpWallet();
            }
        });

        enterAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    topUpWallet();
                }
                return false;
            }
        });

        valAmount500.setText(getActivity().getString(R.string.Rs) + " 500");
        valAmount1000.setText(getActivity().getString(R.string.Rs) + " 1000");
        valAmount2000.setText(getActivity().getString(R.string.Rs) + " 2000");

        valAmount500.setOnClickListener(click500);
        valAmount1000.setOnClickListener(click1000);
        valAmount2000.setOnClickListener(click2000);

        if (NetworkUtilities.isInternet(_activity)) {
            getTransactionsRequest();
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }
    }

    private void topUpWallet() {

        //TODO: Kept it hardcoded for lodha parking demo
        SharedPreferences UserInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonUnitInfo = UserInfo.getString("unit_info", "");
        Resident residentData = gson.fromJson(jsonUnitInfo, Resident.class);
        if (residentData.getMobile().equalsIgnoreCase("9999999998")) {
            return;
        }

        if (NetworkUtilities.isInternet(_activity)) {
            CommonMethod.hideKeyboard(getActivity());
            if (!enterAmount.getText().toString().trim().equalsIgnoreCase("")) {

                if (Integer.parseInt(enterAmount.getText().toString()) > 1000000) {
                    showSnackBar(ll_parent, "Please enter amount less then 100000.00");
                    return;
                }
                showDialog(CONFIRM_DIALOG, "Please confirm payment of " + getActivity().getResources().getString(R.string.Rs) + "" + enterAmount.getText().toString().trim()
                        + " towards Electricity for " + GlobalData.getInstance().getUpdatedUnitName() + ".");
            } else {
                showSnackBar(ll_parent, "Please enter amount");
            }
        } else {
            showSnackBar(ll_parent, getActivity().getResources().getString(R.string.error_no_internet_connection));
        }
    }

    private void getTransactionsRequest() {

        progressBar.setVisibility(View.VISIBLE);
        no_data_found.setVisibility(View.GONE);

        CreateRequest.getInstance().getTransactions(GlobalData.getInstance().getResidentId(), new ApiCallback<TransactionResponse>() {
            @Override
            public void onSuccess(TransactionResponse response) {

                progressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    valWalletBalance.setText(response.getData().getBalance());
                    iconWalletBalance.setText(_activity.getResources().getString(R.string.Rs));
                }
                if (response.getData() != null && response.getData().getTransactions() != null && response.getData().getTransactions().size() > 0) {
                    TransactionListAdapter adapter = new TransactionListAdapter(getActivity(), response.getData().getTransactions());
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

    private void getTransactionCredentials() {

        final WalletCredentialRequest request = new WalletCredentialRequest();
        request.setAmount(enterAmount.getText().toString().trim());
        request.setGateway("payu");
        request.setUtility_type(AppConstant.ELECTRICITY_BOT);
        request.setUnit_id(GlobalData.getInstance().getDemoUnitId());
        request.setResident_id(GlobalData.getInstance().getResidentId());

        if (AppSessionData.getInstance().getSubdomain().equalsIgnoreCase("demo")) {
            request.setSandbox(true);
        } else {
            request.setSandbox(false);
        }

        showActivitySpinner();
        CreateRequest.getInstance().getTransactionCredentials(request, new ApiCallback<WalletCredentialResponse>() {
            @Override
            public void onSuccess(WalletCredentialResponse response) {
                dismissActivitySpinner();
                makePayment(response);
                enterAmount.setText("");
            }

            @Override
            public void onFailure(String error) {
                dismissActivitySpinner();
            }
        });
    }

    public void makePayment(WalletCredentialResponse response) {

        double amount = 0;

        if (response.getAmount() != null && !response.getAmount().equalsIgnoreCase("")) {
            amount = Double.parseDouble(response.getAmount());
        }

        transactionId = response.getTxnid();
        String phone = response.getPhone();
        String productName = response.getProductinfo();
        String firstName = response.getFirstname();
        String txnId = response.getTxnid();
        String email = response.getEmail();
        String sUrl = response.getSurl();
        String fUrl = response.getFurl();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = response.isSandbox();
        String key = response.getKey();
        String merchantId = response.getMerchant_id();

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

        builder.setAmount(amount)
                .setTnxId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setIsDebug(isDebug)
                .setKey(key)
                .setMerchantId(merchantId);

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
        paymentParam.setMerchantHash(response.getPayment_hash());

//        testing purpose
//        String salt = WalletConstant.merchant_salt;
//        String serverCalculatedHash = hashCal(key + "|" + txnId + "|" + amount + "|" + productName + "|"
//                + firstName + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "|" + salt);
//        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);

    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
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

            enterAmount.setText("500");
            enterAmount.setSelection(enterAmount.getText().length());
            valAmount500.setBackgroundResource(R.drawable.ractangular_border_purple);
            valAmount1000.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount2000.setBackgroundResource(R.drawable.rectangular_border_light_gray);
        }
    };

    final private View.OnClickListener click1000 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            enterAmount.setText("1000");
            enterAmount.setSelection(enterAmount.getText().length());
            valAmount500.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount1000.setBackgroundResource(R.drawable.ractangular_border_purple);
            valAmount2000.setBackgroundResource(R.drawable.rectangular_border_light_gray);
        }
    };

    final private View.OnClickListener click2000 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            enterAmount.setText("2000");
            enterAmount.setSelection(enterAmount.getText().length());
            valAmount500.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount1000.setBackgroundResource(R.drawable.rectangular_border_light_gray);
            valAmount2000.setBackgroundResource(R.drawable.ractangular_border_purple);
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
        return getActivity().getResources().getColor(R.color.bw_color_purple);
    }

    @Override
    public String getHeaderTitle() {
        return GlobalData.getInstance().getUpdatedUnitName();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
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

    void showDialog(int type, String contentString) {

        mStackLevel++;

        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        switch (type) {

            case CONFIRM_DIALOG:

                AlertDialogConfirmPayment alertDialogMandatory = AlertDialogConfirmPayment.newInstance("Payment Confirmation", contentString,
                        enterAmount.getText().toString().trim().length(), GlobalData.getInstance().getUpdatedUnitName().length(), "Cancel", "OK");
                alertDialogMandatory.setTargetFragment(this, CONFIRM_DIALOG);
                alertDialogMandatory.show(getFragmentManager().beginTransaction(), "alertDialogConfirmPayment");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CONFIRM_DIALOG:
                getTransactionCredentials();
                break;
        }
    }

    private BroadcastReceiver updatePayment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            String paymentId = intent.getStringExtra("paymentId");

            if (updateFlag) {
                DebugLog.d("PayU update info called");
                updateTransactionDetailsRequest(status, paymentId);
            }
        }
    };

    private void updateTransactionDetailsRequest(String status, String paymentId) {
        progressBar.setVisibility(View.VISIBLE);
        no_data_found.setVisibility(View.GONE);

        updateFlag = false;

        WalletCredentialRequest request = new WalletCredentialRequest();
        request.setStatus(status);

        CreateRequest.getInstance().updateTransaction(transactionId, request, new ApiCallback<TransactionResponse>() {
            @Override
            public void onSuccess(TransactionResponse response) {
                updateFlag = true;
                progressBar.setVisibility(View.GONE);

                transactionLayout.setVisibility(View.VISIBLE);
                topUpLayout.setVisibility(View.GONE);

                switchTopUpView.setVisibility(View.VISIBLE);
                switchTransactionView.setVisibility(View.GONE);

                if (response.getData() != null) {
                    if (response.getData() != null) {
                        valWalletBalance.setText(response.getData().getBalance());
                        iconWalletBalance.setText(_activity.getResources().getString(R.string.Rs));
                    }
                    if (response.getData() != null && response.getData().getTransactions() != null && response.getData().getTransactions().size() > 0) {
                        TransactionListAdapter adapter = new TransactionListAdapter(getActivity(), response.getData().getTransactions());
                        listTransaction.setAdapter(adapter);
                    } else {
                        no_data_found.setVisibility(View.VISIBLE);
                        no_data_found.setText("No transactions found.");
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                updateFlag = true;
                progressBar.setVisibility(View.GONE);
                no_data_found.setVisibility(View.VISIBLE);
                no_data_found.setText(error);
            }
        });
    }
}
