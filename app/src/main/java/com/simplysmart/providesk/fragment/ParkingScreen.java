package com.simplysmart.providesk.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.config.AppConstant;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.lodhachess.response.LodhaChessCancelResponseEnvelope;
import com.simplysmart.lib.model.lodhachess.response.LodhaChessResponseEnvelope;
import com.simplysmart.lib.request.LodhaChessCreateRequest;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by shekhar on 23/03/17.
 */
public class ParkingScreen extends BaseFragment {

    private Typeface textTypeface;
    private TextView getCarRequestButton;
    private TextView cancelRequestButton;

    private RelativeLayout ll_parent;
    private View rootView;
    private TextView serverMessage;

    private EditText reg_a, reg_b, reg_c, reg_d;

    private boolean updateFlag = true;

    private HashMap<String, String> responseCodeMap = new HashMap<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        textTypeface = Typeface.createFromAsset(getActivity().getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_parking_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeWidgets();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeWidgets() {


        setMapData();

        ll_parent = (RelativeLayout) rootView.findViewById(R.id.ll_parent);

        reg_a = (EditText) rootView.findViewById(R.id.reg_a);
        reg_b = (EditText) rootView.findViewById(R.id.reg_b);
        reg_c = (EditText) rootView.findViewById(R.id.reg_c);
        reg_d = (EditText) rootView.findViewById(R.id.reg_d);

        getCarRequestButton = (TextView) rootView.findViewById(R.id.GetCarRequestButton);
        cancelRequestButton = (TextView) rootView.findViewById(R.id.CancelRequestButton);
        serverMessage = (TextView) rootView.findViewById(R.id.serverMessage);

        getCarRequestButton.setTypeface(textTypeface);
        cancelRequestButton.setTypeface(textTypeface);

        enableButton();

        getCarRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkUtilities.isInternet(getActivity())) {
                    reg_d.setCursorVisible(false);
                    serverMessage.setText("");
                    callGetCarRequest(reg_a.getText().toString().trim()
                            + reg_b.getText().toString().trim()
                            + reg_c.getText().toString().trim()
                            + reg_d.getText().toString().trim());
                } else {
//                    serverMessage.setText(getString(R.string.connect_to_internet));
                }
            }
        });

        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkUtilities.isInternet(getActivity())) {
                    reg_d.setCursorVisible(false);
                    serverMessage.setText("");
                    callCancelGetCarRequest(reg_a.getText().toString().trim()
                            + reg_b.getText().toString().trim()
                            + reg_c.getText().toString().trim()
                            + reg_d.getText().toString().trim());
                } else {
//                    serverMessage.setText(getString(R.string.connect_to_internet));
                }
            }
        });

        reg_a.setTypeface(textTypeface);
        reg_b.setTypeface(textTypeface);
        reg_c.setTypeface(textTypeface);
        reg_d.setTypeface(textTypeface);

        reg_a.clearFocus();
        reg_a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    reg_b.requestFocus();
                }
                enableButton();
            }
        });

        reg_b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    reg_c.requestFocus();
                } else if (s.length() == 0) {
                    reg_a.requestFocus();
                    reg_a.setSelection(reg_a.getText().length());
                }
                enableButton();
            }
        });

        reg_c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    reg_d.requestFocus();
                    reg_d.setCursorVisible(true);
                } else if (s.length() == 0) {
                    reg_b.requestFocus();
                    reg_b.setSelection(reg_b.getText().length());
                }
                enableButton();
            }
        });

        reg_d.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                reg_d.setCursorVisible(true);
                return false;
            }
        });
        reg_d.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                reg_d.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    CommonMethod.hideKeyboard(getActivity());
                } else if (s.length() == 0) {
                    reg_c.requestFocus();
                    reg_c.setSelection(reg_c.getText().length());
                }
                enableButton();
            }
        });
    }

    private void setMapData() {
        responseCodeMap.put("1", "Ok");
        responseCodeMap.put("2", "Car Not Parked");
        responseCodeMap.put("3", "Car Is Valid But It Is Storage Request");
        responseCodeMap.put("4", "Request Is Already Running For This Car No.");
        responseCodeMap.put("5", "Car No. Already In Queue");
        responseCodeMap.put("6", "Car Is Already Stored");
        responseCodeMap.put("7", "Failed To Insert In User Request Data");
        responseCodeMap.put("8", "System Error");
        responseCodeMap.put("19", "Car Not In Queue");
        responseCodeMap.put("10", "Can Not Cancel Request");
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bw_color_dark_green);
    }

    @Override
    public String getHeaderTitle() {
        return "Parking";
    }

    private void enableButton() {

        if (reg_a.getText().toString().trim().length() > 0
                && reg_b.getText().toString().trim().length() > 0
                && reg_c.getText().toString().trim().length() > 0
                && reg_d.getText().toString().trim().length() > 0) {

            getCarRequestButton.setEnabled(true);
            cancelRequestButton.setEnabled(true);
        } else {
            getCarRequestButton.setEnabled(false);
            cancelRequestButton.setEnabled(false);
        }
    }

    private void callGetCarRequest(String carNumber) {

        showActivitySpinner();
        LodhaChessCreateRequest.getInstance().getQueueNoAndStatus(carNumber, new ApiCallback<LodhaChessResponseEnvelope>() {
            @Override
            public void onSuccess(LodhaChessResponseEnvelope response) {

                dismissActivitySpinner();
                try {
                    String responseString = response.getBody().getGiveQueueNoAndStatusResponse().getGiveQueueNoAndStatusResult();
                    JSONObject responseObject = new JSONObject(responseString);

                    if (responseObject.getString("Result").equalsIgnoreCase("1") && !responseObject.getString("QueueNo").equalsIgnoreCase("0")) {
                        serverMessage.setText("Car is in " + "Queue No. " + responseObject.getString("QueueNo"));
                    } else {
                        serverMessage.setText(responseCodeMap.get(responseObject.getString("Result")));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                dismissActivitySpinner();
            }
        });
    }

    private void callCancelGetCarRequest(String carNumber) {

        showActivitySpinner();
        LodhaChessCreateRequest.getInstance().postCancelRequest(carNumber, new ApiCallback<LodhaChessCancelResponseEnvelope>() {
            @Override
            public void onSuccess(LodhaChessCancelResponseEnvelope response) {

                dismissActivitySpinner();
                try {
                    String responseString = response.getBody().getGiveCancelRequestResponse().getGiveCancelRequestResult();
                    if (responseString.equalsIgnoreCase("1")) {
                        serverMessage.setText(responseCodeMap.get(responseString) + " \nYour request is being processed.");
                    } else {
                        serverMessage.setText(responseCodeMap.get(responseString));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                dismissActivitySpinner();
            }
        });
    }


}
