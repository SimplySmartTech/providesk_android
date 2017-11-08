package com.simplysmart.providesk.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSmsReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private SharedPreferences UserInfo;

    public void onReceive(Context context, Intent intent) {

        UserInfo = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        if (UserInfo.getString("is_mobile_validated", "0").equalsIgnoreCase("0")) {
            // send details for validation to api
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        // String phoneNumber =
                        // currentMessage.getDisplayOriginatingAddress();

                        // String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        // Log.i("SmsReceiver", "senderNum: " + senderNum +
                        // "; message: " + message);

                        // int duration = Toast.LENGTH_LONG;
                        // Toast toast = Toast.makeText(context, "senderNum: " +
                        // senderNum + ", message: " + message,
                        // duration);
                        // toast.show();

                        Intent smsIntent = new Intent("smsreceiver");
                        smsIntent.putExtra("SMS_MESSAGE", message);

                        LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }

        }

    }

}