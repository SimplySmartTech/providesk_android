package com.simplysmart.app.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Calendar c;
    private int year, month, day;
    private boolean isStartDate;

    public DatePickerFragment() {
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public DatePickerFragment(DatePickerDialog.OnDateSetListener callback, int year, int month, int day, boolean isStartDate) {
        this.mDateSetListener = callback;
        this.year = year;
        this.month = month;
        this.day = day;
        this.isStartDate = isStartDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);//R.style.AppDatePickerTheme
//        pickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return pickerDialog;
    }
}