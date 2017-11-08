package com.simplysmart.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.config.GlobalData;
import com.simplysmart.app.custom.MySeekBar;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.planner.Month;
import com.simplysmart.lib.model.planner.PlannerData;
import com.simplysmart.lib.model.planner.PlannerResponse;
import com.simplysmart.lib.request.CreateRequest;

/**
 * Created by shekhar on 13/8/15.
 * PlannerScreen
 */
public class PlannerScreen extends BaseFragment {

    private Activity _activity;
    private TextView btnOneM, btnThreeM, btnSixM, btnTwelveM;
    private TextView txtOneM, txtThreeM, txtSixM, txtTwelveM;
    private TextView budgetedExpenses, walletBalance;
    private FrameLayout btnFrameTopUp;
    private TextView tick;
    private CheckBox checkbox_maintenance;

    private int valSelectedElectrical = 0, valAverageElectrical = 0, valMinimumElectrical = 0, valMaximumElectrical = 0;
    private int valSelectedWater = 0, valAverageWater = 0, valMinimumWater = 0, valMaximumWater = 0;

    private boolean isChanged;
    private int monthSelected = 1;
    private boolean isRecommended;

    private View rootView;
    private ProgressBar progressBar;
    private RelativeLayout content_layout;
    private TextView no_data_found;
    private Typeface textTypeface;

    private PlannerData plannerData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
        textTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_planner_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        initializeWidget();

        if (NetworkUtilities.isInternet(_activity)) {
            getPlannerDetails();
        } else {
            progressBar.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);
            no_data_found.setText(_activity.getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_planner, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem reset = menu.findItem(R.id.action_reset);
        reset.setVisible(isChanged);
        GlobalData.getInstance().setIsChanged(isChanged);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_reset:

                parsePlannerResponse();
                isChanged = false;
                _activity.invalidateOptionsMenu();
                tick.setBackgroundResource(0);
                return true;

            case R.id.action_save:
                if (NetworkUtilities.isInternet(_activity)) {
                    if (plannerData != null && plannerData.getPlanner() != null) {
                        updatePlannerDetails(plannerData.getPlanner().getUnit_id(), checkbox_maintenance.isChecked(), monthSelected, valSelectedWater, valSelectedElectrical);
                    } else {
                        showSnackBar(content_layout, "No changes found to save.");
                    }
                } else {
                    showSnackBar(content_layout, _activity.getString(R.string.error_no_internet_connection));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeWidget() {

        Typeface iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        content_layout = (RelativeLayout) rootView.findViewById(R.id.content_layout);
        no_data_found = (TextView) rootView.findViewById(R.id.no_data_found);

        TextView electricity_logo = (TextView) rootView.findViewById(R.id.electricity_logo);
        TextView water_logo = (TextView) rootView.findViewById(R.id.water_logo);
        TextView maintenance_logo = (TextView) rootView.findViewById(R.id.maintenance_logo);
        TextView txtBudgetedExpenses = (TextView) rootView.findViewById(R.id.txt_budget_expence);
        TextView txtWalletBalance = (TextView) rootView.findViewById(R.id.txt_wallet_bal);
        TextView txtMaintenance = (TextView) rootView.findViewById(R.id.max_value_maintenance);

        TextView buttonRecommended = (TextView) rootView.findViewById(R.id.btn_recommended);
        tick = (TextView) rootView.findViewById(R.id.tick);
        budgetedExpenses = (TextView) rootView.findViewById(R.id.val_budget_expense);
        walletBalance = (TextView) rootView.findViewById(R.id.val_wallet_balance);
        checkbox_maintenance = (CheckBox) rootView.findViewById(R.id.checkbox_maintenance);

        electricity_logo.setTypeface(iconTypeface);
        water_logo.setTypeface(iconTypeface);
        maintenance_logo.setTypeface(iconTypeface);

        btnFrameTopUp = (FrameLayout) rootView.findViewById(R.id.layout_topup_btn);
        TextView btnWalletTopUp = (TextView) rootView.findViewById(R.id.btn_wallet_topup);

        btnOneM = (TextView) rootView.findViewById(R.id.btn_for_one);
        btnThreeM = (TextView) rootView.findViewById(R.id.btn_for_three);
        btnSixM = (TextView) rootView.findViewById(R.id.btn_for_six);
        btnTwelveM = (TextView) rootView.findViewById(R.id.btn_for_twelve);

        txtOneM = (TextView) rootView.findViewById(R.id.txt_for_one);
        txtThreeM = (TextView) rootView.findViewById(R.id.txt_for_three);
        txtSixM = (TextView) rootView.findViewById(R.id.txt_for_six);
        txtTwelveM = (TextView) rootView.findViewById(R.id.txt_for_twelve);

        btnOneM.setOnClickListener(oneMSelectionClick);
        btnThreeM.setOnClickListener(threeMSelectionClick);
        btnSixM.setOnClickListener(sixMSelectionClick);
        btnTwelveM.setOnClickListener(twelveMSelectionClick);

        buttonRecommended.setOnClickListener(setRecommendedValues);
        btnWalletTopUp.setOnClickListener(walletTopUpClick);

        btnOneM.setTypeface(iconTypeface);
        btnThreeM.setTypeface(iconTypeface);
        btnSixM.setTypeface(iconTypeface);
        btnTwelveM.setTypeface(iconTypeface);

        txtOneM.setTypeface(textTypeface);
        txtThreeM.setTypeface(textTypeface);
        txtSixM.setTypeface(textTypeface);
        txtTwelveM.setTypeface(textTypeface);

        btnWalletTopUp.setTypeface(iconTypeface);
        buttonRecommended.setTypeface(iconTypeface);

        budgetedExpenses.setTypeface(textTypeface);
        walletBalance.setTypeface(textTypeface);
        txtBudgetedExpenses.setTypeface(textTypeface);
        txtWalletBalance.setTypeface(textTypeface);

        btnWalletTopUp.setText(_activity.getString(R.string.icon_wallet_topup));

        MySeekBar maintenance_bar = (MySeekBar) rootView.findViewById(R.id.maintenance_bar);
        maintenance_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        checkbox_maintenance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int total = Integer.parseInt(budgetedExpenses.getText().toString().trim());
                if (buttonView.isChecked()) {
                    total = total + 15000;
                } else {
                    total = total - 15000;
                }
                budgetedExpenses.setText(String.valueOf(total));
            }
        });
    }

    private void getPlannerDetails() {

        progressBar.setVisibility(View.VISIBLE);
        CreateRequest.getInstance().getPlannerDetails(GlobalData.getInstance().getUnits().get(0).getId(), new ApiCallback<PlannerResponse>() {
            @Override
            public void onSuccess(PlannerResponse response) {
                progressBar.setVisibility(View.GONE);
                content_layout.setVisibility(View.VISIBLE);
                plannerData = response.getData();
                if (plannerData == null) return;
                parsePlannerResponse();
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                no_data_found.setText(error);
            }
        });
    }

    private void parsePlannerResponse() {

        checkbox_maintenance.setChecked(plannerData.getPlanner().isAmc());
        walletBalance.setText(String.valueOf((int) plannerData.getPlanner().getEwallet_balance()));
        monthSelected = plannerData.getPlanner().getDuration();

        Month month = null;
        switch (monthSelected) {
            case 1:
                month = plannerData.getPlanner().getRanges().getOne();
                break;
            case 3:
                month = plannerData.getPlanner().getRanges().getThree();
                break;
            case 6:
                month = plannerData.getPlanner().getRanges().getSix();
                break;
            case 12:
                month = plannerData.getPlanner().getRanges().getTwelve();
                break;
            default:
                break;
        }
        setPlannerValues(month);
    }

    private void setPlannerValues(Month month) {

        if (month != null) {
            valMinimumElectrical = month.getMin().getElectricity();
            valMaximumElectrical = month.getMax().getElectricity();
            valAverageElectrical = month.getAvg().getElectricity();

            valMinimumWater = month.getMin().getWater();
            valMaximumWater = month.getMax().getWater();
            valAverageWater = month.getAvg().getWater();

            valSelectedElectrical = plannerData.getPlanner().getElectricity();
            valSelectedWater = plannerData.getPlanner().getWater();

            setUpBarElectricity(valMinimumElectrical, valMaximumElectrical, valSelectedElectrical, 100, valAverageElectrical);
            setUpBarWater(valMinimumWater, valMaximumWater, valSelectedWater, 100, valAverageWater);
        }

        setSelectedMonthView(monthSelected);

        if (Integer.parseInt(walletBalance.getText().toString()) <= Integer.parseInt(budgetedExpenses.getText().toString())) {
            btnFrameTopUp.setVisibility(View.VISIBLE);
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(_activity, R.anim.fade_in);
            btnFrameTopUp.startAnimation(myFadeInAnimation);
        } else {
            btnFrameTopUp.setVisibility(View.INVISIBLE);
        }
    }

    private void setSelectedMonthView(int selected) {

        refreshMonthView();

        switch (selected) {
            case 1:
                btnOneM.setText(_activity.getString(R.string.icon_calendar_selected));
                txtOneM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_white));
                break;
            case 3:
                btnThreeM.setText(_activity.getString(R.string.icon_calendar_selected));
                txtThreeM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_white));
                break;
            case 6:
                btnSixM.setText(_activity.getString(R.string.icon_calendar_selected));
                txtSixM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_white));
                break;
            case 12:
                btnTwelveM.setText(_activity.getString(R.string.icon_calendar_selected));
                txtTwelveM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_white));
                break;
            default:
                break;
        }
    }

    private void refreshMonthView() {
        btnOneM.setText(_activity.getString(R.string.icon_calendar_normal));
        btnThreeM.setText(_activity.getString(R.string.icon_calendar_normal));
        btnSixM.setText(_activity.getString(R.string.icon_calendar_normal));
        btnTwelveM.setText(_activity.getString(R.string.icon_calendar_normal));

        txtOneM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_green));
        txtThreeM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_green));
        txtSixM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_green));
        txtTwelveM.setTextColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_green));
    }

    private void updatePlannerDetails(String unitId, boolean amc, int duration, int water, int electricity) {
        CreateRequest.getInstance().updatePlannerDetails(unitId, amc, duration, water, electricity, new ApiCallback<PlannerResponse>() {
            @Override
            public void onSuccess(PlannerResponse response) {
                showSnackBar(content_layout, response.getMessage());
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bw_color_dark_green);
    }

    @Override
    public String getHeaderTitle() {
        return GlobalData.getInstance().getUpdatedUnitName();
    }

    //setup Slider for Electricity Expenses
    private void setUpBarElectricity(final int minVal, int maxVal, int selected, final int interval, int average) {

        final MySeekBar electricityBar = (MySeekBar) rootView.findViewById(R.id.electricity_bar);
        final ProgressBar electricityBarAverage = (ProgressBar) rootView.findViewById(R.id.electricity_bar_average);
        final TextView selectedValueElectricity = (TextView) rootView.findViewById(R.id.selected_value_electricity);
        TextView valueBarMinimum = (TextView) rootView.findViewById(R.id.min_value_electricity);
        TextView valueBarMaximum = (TextView) rootView.findViewById(R.id.max_value_electricity);
        TextView avgValueElectricity = (TextView) rootView.findViewById(R.id.average_value_electricity);

        valueBarMinimum.setText(_activity.getString(R.string.Rs) + minVal);
        valueBarMaximum.setText(_activity.getString(R.string.Rs) + maxVal);

        valMaximumElectrical = maxVal;

        electricityBar.setMax((maxVal - minVal) / interval);
        electricityBar.setProgress((selected - minVal) / 100);

        electricityBarAverage.setMax((maxVal - minVal) / interval);
        electricityBarAverage.setProgress((roundToNearestCent(average) - minVal) / 100);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Rect thumbRect = new Rect(171, 0, 218, 50);

        p.setMargins(thumbRect.centerX() - (selectedValueElectricity.getWidth() / 2), 0, 0, 0);
        selectedValueElectricity.setLayoutParams(p);
        selectedValueElectricity.setText(String.valueOf(_activity.getResources().getString(R.string.Rs) + selected));

        avgValueElectricity.setText(_activity.getString(R.string.Rs) + average);
        avgValueElectricity.setPadding(thumbRect.centerX() - (selectedValueElectricity.getWidth() / 2) / 2, 0, 0, 0);

        valAverageElectrical = Integer.parseInt(avgValueElectricity.getText().toString().substring(1));

        valSelectedElectrical = Integer.parseInt(selectedValueElectricity.getText().toString().substring(1));
        setBudgetedExpense(valSelectedElectrical, valSelectedWater);

        electricityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress <= 0 || progress >= electricityBar.getMax()) {
                    selectedValueElectricity.setVisibility(View.INVISIBLE);
                } else {
                    selectedValueElectricity.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    Rect thumbRect = electricityBar.getSeekBarThumb().getBounds();
                    p.setMargins(thumbRect.centerX(), 0, 0, 0);
                    selectedValueElectricity.setLayoutParams(p);
                    selectedValueElectricity.setText(_activity.getString(R.string.Rs) + String.valueOf((progress * interval) + minVal));
                    valSelectedElectrical = Integer.parseInt(selectedValueElectricity.getText().toString().substring(1));

                    setBudgetedExpense(valSelectedElectrical, valSelectedWater);

                    //show reset button when when change detected
                    isChanged = valSelectedWater != 400 || valSelectedElectrical != 500;
                    _activity.invalidateOptionsMenu();
                    if (isRecommended) {
                        tick.setBackgroundResource(R.drawable.tick_icon);
                    } else {
                        tick.setBackgroundResource(0);
                    }
                }
            }
        });
    }

    //setup Slider for Water Expenses
    private void setUpBarWater(final int minVal, int maxVal, int selected, final int interval, int average) {

        final MySeekBar WaterBar = (MySeekBar) rootView.findViewById(R.id.water_bar);
        final ProgressBar waterBarAverage = (ProgressBar) rootView.findViewById(R.id.water_bar_average);
        final TextView selectedValueWater = (TextView) rootView.findViewById(R.id.selected_value_water);

        TextView valueBarMinimum = (TextView) rootView.findViewById(R.id.min_value_water);
        TextView valueBarMaximum = (TextView) rootView.findViewById(R.id.max_value_water);
        TextView avgValueWater = (TextView) rootView.findViewById(R.id.average_value_water);

        valueBarMinimum.setText(_activity.getString(R.string.Rs) + minVal);
        valueBarMaximum.setText(_activity.getString(R.string.Rs) + maxVal);

        valMaximumWater = maxVal;

        WaterBar.setMax((maxVal - minVal) / interval);
        WaterBar.setProgress((selected - minVal) / 100);

        waterBarAverage.setMax((maxVal - minVal) / interval);
        waterBarAverage.setProgress((roundToNearestCent(average) - minVal) / 100);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Rect thumbRect = new Rect(171, 0, 218, 50);

        p.setMargins(thumbRect.centerX() - (selectedValueWater.getWidth() / 2), 0, 0, 0);
        selectedValueWater.setLayoutParams(p);
        selectedValueWater.setText(String.valueOf(_activity.getResources().getString(R.string.Rs) + selected));

        avgValueWater.setText(_activity.getString(R.string.Rs) + String.valueOf(average));
        avgValueWater.setPadding(thumbRect.centerX() - (selectedValueWater.getWidth() / 2) / 2, 0, 0, 0);

        valAverageWater = Integer.parseInt(avgValueWater.getText().toString().substring(1));

        valSelectedWater = Integer.parseInt(selectedValueWater.getText().toString().substring(1));
        setBudgetedExpense(valSelectedElectrical, valSelectedWater);

        WaterBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress <= 0 || progress >= WaterBar.getMax()) {
                    selectedValueWater.setVisibility(View.INVISIBLE);
                } else {
                    selectedValueWater.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    Rect thumbRect = WaterBar.getSeekBarThumb().getBounds();
                    p.setMargins(thumbRect.centerX() - (selectedValueWater.getWidth() / 2), 0, 0, 0);
                    selectedValueWater.setLayoutParams(p);
                    selectedValueWater.setText(_activity.getString(R.string.Rs) + String.valueOf((progress * interval) + minVal));
                    valSelectedWater = Integer.parseInt(selectedValueWater.getText().toString().substring(1));

                    setBudgetedExpense(valSelectedElectrical, valSelectedWater);

                    //show reset button when when change detected
                    isChanged = valSelectedWater != 400 || valSelectedElectrical != 500;
                    _activity.invalidateOptionsMenu();
                    if (isRecommended) {
                        tick.setBackgroundResource(R.drawable.tick_icon);
                        isRecommended = false;
                    } else {
                        tick.setBackgroundResource(0);
                    }
                }
            }
        });
    }

    //Calculate total budgeted expenses
    private void setBudgetedExpense(int valSelectedElectrical, int valSelectedWater) {

        setHasOptionsMenu(true);
        initializeWidget();
        int total;

        //Get budgeted expense
        if (checkbox_maintenance.isChecked()) {
            total = 15000 + valSelectedElectrical + valSelectedWater;
        } else {
            total = valSelectedElectrical + valSelectedWater;
        }
        budgetedExpenses.setText(String.valueOf(total));

        if (Integer.parseInt(walletBalance.getText().toString()) <= total) {
            btnFrameTopUp.setVisibility(View.VISIBLE);
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(_activity, R.anim.fade_in);
            btnFrameTopUp.startAnimation(myFadeInAnimation);
        } else {
            btnFrameTopUp.setVisibility(View.INVISIBLE);
        }
    }

    private final View.OnClickListener oneMSelectionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isRecommended = false;
            monthSelected = 1;

            Month month = plannerData.getPlanner().getRanges().getOne();
            setPlannerValues(month);
            setSelectedMonthView(1);
        }
    };

    private final View.OnClickListener threeMSelectionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isRecommended = false;
            monthSelected = 3;

            Month month = plannerData.getPlanner().getRanges().getThree();
            setPlannerValues(month);
            setSelectedMonthView(3);
        }
    };

    private final View.OnClickListener sixMSelectionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isRecommended = false;
            monthSelected = 6;

            Month month = plannerData.getPlanner().getRanges().getSix();
            setPlannerValues(month);
            setSelectedMonthView(6);
        }
    };

    private final View.OnClickListener twelveMSelectionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isRecommended = false;
            monthSelected = 12;

            Month month = plannerData.getPlanner().getRanges().getTwelve();
            setPlannerValues(month);
            setSelectedMonthView(12);
        }
    };

    private final View.OnClickListener walletTopUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int budgetedAmount = 0;
            if (Integer.parseInt(budgetedExpenses.getText().toString().trim()) > Integer.parseInt(walletBalance.getText().toString().trim())) {
                budgetedAmount = Integer.parseInt(budgetedExpenses.getText().toString().trim()) - Integer.parseInt(walletBalance.getText().toString().trim());
            }

            // Pass the budgeted value to wallet screen, to top-up wallet
            Bundle bundle = new Bundle();
            bundle.putInt("budgeted_amount", budgetedAmount);
            Fragment graphScreen = new EwalletScreen();
            graphScreen.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, graphScreen).addToBackStack(null).commit();
        }
    };

    private final View.OnClickListener setRecommendedValues = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            tick.setBackgroundResource(R.drawable.tick_icon);
            isRecommended = false;
            isRecommended = true;

            valSelectedElectrical = valAverageElectrical;
            valSelectedWater = valAverageWater;

            setUpBarElectricity(valMinimumElectrical, valMaximumElectrical, valAverageElectrical, 100, valAverageElectrical);
            setUpBarWater(valMinimumWater, valMaximumWater, valAverageWater, 100, valAverageWater);
        }
    };

}
