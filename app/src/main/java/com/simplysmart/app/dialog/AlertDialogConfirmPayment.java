package com.simplysmart.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simplysmart.app.R;

public class AlertDialogConfirmPayment extends DialogFragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_AMOUNT_LENGTH = "amountLength";
    private static final String KEY_UNIT_NAME_LENGTH = "unitNameLength";
    private static final String KEY_POSITIVE_BUTTON = "positiveButton";
    private static final String KEY_NEGATIVE_BUTTON = "negativeButton";

    public static AlertDialogConfirmPayment newInstance(String title, String message,
                                                        int amountLength, int unitNameLength,
                                                        String negativeButton, String positiveButton) {
        AlertDialogConfirmPayment f = new AlertDialogConfirmPayment();

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_MESSAGE, message);
        args.putInt(KEY_AMOUNT_LENGTH, amountLength);
        args.putInt(KEY_UNIT_NAME_LENGTH, unitNameLength);
        args.putString(KEY_NEGATIVE_BUTTON, negativeButton);
        args.putString(KEY_POSITIVE_BUTTON, positiveButton);
        f.setArguments(args);

        return f;
    }

    public AlertDialogConfirmPayment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_payment, null);

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = (TextView) dialogView.findViewById(R.id.dialogMessage);
        Button dialogNegativeButton = (Button) dialogView.findViewById(R.id.dialogButtonNegative);
        Button dialogPositiveButton = (Button) dialogView.findViewById(R.id.dialogButtonPositive);

        SpannableString spannableString = new SpannableString(getArguments().getString(KEY_MESSAGE));

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.bw_color_blue)),
                26, (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 1), 0);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.bw_color_blue)),
                (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9), (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9 + 12), 0);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.bw_color_blue)),
                (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9 + 12 + 5),
                (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9 + 12 + 5 + getArguments().getInt(KEY_UNIT_NAME_LENGTH) + 1), 0);

        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                26, (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 1), 0);

        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9),
                (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9 + 12), 0);

        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9 + 12 + 5),
                (26 + getArguments().getInt(KEY_AMOUNT_LENGTH) + 9 + 12 + 5 + getArguments().getInt(KEY_UNIT_NAME_LENGTH) + 1), 0);

        dialogTitle.setText(getArguments().getString(KEY_TITLE));
        dialogMessage.setText(spannableString);
        dialogNegativeButton.setText(getArguments().getString(KEY_NEGATIVE_BUTTON));
        dialogPositiveButton.setText(getArguments().getString(KEY_POSITIVE_BUTTON));

        dialogNegativeButton.setVisibility(View.GONE);
        dialogPositiveButton.setVisibility(View.GONE);

        builder.setView(dialogView);

        builder.setPositiveButton(R.string.ok_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                    }
                }
        )
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(0, Activity.RESULT_CANCELED, getActivity().getIntent());
                    }
                })
                .create();

        return builder.create();
    }
}