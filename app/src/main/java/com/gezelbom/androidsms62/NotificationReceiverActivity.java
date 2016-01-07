package com.gezelbom.androidsms62;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.TextView;

/**
 * Created by Alex
 *
 * Activity that is invoked and receives the Intent which the notification sends. Simply shows the
 * SMS
 */
public class NotificationReceiverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);

        TextView textViewSMS = (TextView) findViewById(R.id.textView_body);
        TextView textViewNumber = (TextView) findViewById(R.id.textView_number);

        for (SmsMessage sms : Telephony.Sms.Intents.getMessagesFromIntent(getIntent())) {
            if (sms != null) {
                textViewSMS.setText(sms.getDisplayMessageBody());
                textViewNumber.setText(sms.getDisplayOriginatingAddress());
            }else {
                break;
            }
        }
    }
}
