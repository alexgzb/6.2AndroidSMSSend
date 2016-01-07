package com.gezelbom.androidsms62;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Alex
 *
 * SMS Broadcaster that gets notified when an SMS is received and then creates a Toast message with the sms
 * and creates a notification with a pending intent that opens the SMS message in an Activity
 *
 */
public class SmsBroadcaster extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent resultIntent = new Intent(context, NotificationReceiverActivity.class);
        resultIntent.putExtras(intent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * Using static method from Telephony.SMS.Intents to get messages as SMSMesage objects
         * looping through them and for each sms creates a toast message with the sms and then a
         * Notification is created which uses LED flashing and Vibration
         */
        for (SmsMessage sms : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            if (sms != null) {
                String senderNum = sms.getDisplayOriginatingAddress();
                String message = sms.getDisplayMessageBody();

                Toast.makeText(context,"Sender: "+ senderNum + ", message: " + message, Toast.LENGTH_LONG).show();
                ((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(10000);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("New SMS Received")
                                .setContentIntent(resultPendingIntent)
                                .setLights(Color.CYAN, 100, 100)
                                .setContentText(message);

                int notificationId = 00001;
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationId, builder.build());

            }else {
                break;
            }
        }
    }
}
