package com.simplysmart.providesk.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.dialog.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shekhar on 23/03/17.
 */
public class AmenitiesScreen extends BaseFragment {


    private View rootView;

    private TextView startTime, endTime;
    private TextView startDate, endDate;

    private Calendar c;

    boolean isStartDate, isEndDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_amenities_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        c = Calendar.getInstance();
        initializeWidgets();

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartDate = true;
                DialogFragment newFragment = new DatePickerFragment(mDateListener,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH), isStartDate);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEndDate = true;
                DialogFragment newFragment = new DatePickerFragment(mDateListener,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH), isEndDate);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openTimeSelectDialog(startTime);
            }
        });


        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimeSelectDialog(endTime);
            }
        });


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
        startDate = (TextView) rootView.findViewById(R.id.startDate);
        endDate = (TextView) rootView.findViewById(R.id.endDate);
        startTime = (TextView) rootView.findViewById(R.id.startTime);
        endTime = (TextView) rootView.findViewById(R.id.endTime);
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bw_color_blue);
    }

    @Override
    public String getHeaderTitle() {
        return "Amenities";
    }

    public void updateDate(int year, int month, int day) {
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        if (isStartDate) {
            isStartDate = false;
            startDate.setText(sdf.format(c.getTime()));

        } else{
            isEndDate = false;
            endDate.setText(sdf.format(c.getTime()));

        }
    }

    private DatePickerDialog.OnDateSetListener mDateListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    updateDate(year, month, day);
                }
            };


    private void openTimeSelectDialog(final TextView setDateOn) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int hour, minute;
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        boolean isPM = (hourOfDay >= 12);
                        setDateOn.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

}

