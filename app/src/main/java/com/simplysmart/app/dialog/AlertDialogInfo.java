package com.simplysmart.app.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.lib.model.sensor.SensorItem;

public class AlertDialogInfo extends DialogFragment implements View.OnClickListener {

    private static final String KEY_MESSAGE = "message";

    public static AlertDialogInfo newInstance(SensorItem sensorList) {
        AlertDialogInfo f = new AlertDialogInfo();

        Bundle args = new Bundle();
        args.putParcelable(KEY_MESSAGE, sensorList);
        f.setArguments(args);

        return f;
    }

    public AlertDialogInfo() {
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Typeface textTypeface = Typeface.createFromAsset(getActivity().getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_standard, null);

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = (TextView) dialogView.findViewById(R.id.dialogMessage);
        Button dialogNegativeButton = (Button) dialogView.findViewById(R.id.dialogButtonNegative);
        Button dialogPositiveButton = (Button) dialogView.findViewById(R.id.dialogButtonPositive);

        dialogTitle.setText("Last Reading Details");

        if (getArguments().getParcelable(KEY_MESSAGE) != null) {
            SensorItem sensorItem = getArguments().getParcelable(KEY_MESSAGE);

            if (sensorItem != null) {
                dialogMessage.setText("\n" + "Reading date : " + sensorItem.getLast_reading_at() + "\n"
                        + "Reading value : " + sensorItem.getLast_reading() + " " + sensorItem.getUnit());
            }
        }

        dialogPositiveButton.setText("Close");
        dialogPositiveButton.setTextColor(getActivity().getResources().getColor(R.color.bw_color_black));
        dialogNegativeButton.setText("");
        dialogNegativeButton.setVisibility(View.GONE);

        dialogTitle.setTypeface(textTypeface);
        dialogMessage.setTypeface(textTypeface);
        dialogNegativeButton.setTypeface(textTypeface);
        dialogPositiveButton.setTypeface(textTypeface);

        dialogNegativeButton.setOnClickListener(this);
        dialogPositiveButton.setOnClickListener(this);

        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dialogButtonNegative) {
            dismiss();
        }
        if (v.getId() == R.id.dialogButtonPositive) {
            dismiss();
        }
    }

}