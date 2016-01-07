package com.gezelbom.androidsms62;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Simple APP to send and receive SMS.
 * The send SMS functionality has copy/paste functionality as well as possibillity to get number
 * from contacts.
 * Displays a notification with flashing led lights and vibration when a SMS has been received
 */
public class MainActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    EditText smsBodyEditText;
    ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditText widgets for the number and the smsbody
        phoneNumberEditText = (EditText) findViewById(R.id.editText_number);
        phoneNumberEditText.setText("");
        smsBodyEditText = (EditText) findViewById(R.id.editText_body);
        smsBodyEditText.setText("");

        // Experimenting with Floating Action button for sending SMS
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            /**
             * Anonymous inner OnClickListener For the fab
             * Has an Action which is also an onClickListener which shows a Alert dialog warning first and then
             * uses the SmsManager to send an sms
             */
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "To confirm".toUpperCase(), Snackbar.LENGTH_LONG).setAction("Click here", dialogBoxclickListener).show();
            }
        });
    }

    /**
     * OnClick Listener that creates a Alert Dialog, if the user Clicks on Yes, the sms is sent, otherwise not
     */
    View.OnClickListener dialogBoxclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

            alertDialogBuilder
                    .setTitle("Are you sure?")
                    .setMessage("Sending SMS will probably cost, Do you want to continue?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SmsManager.getDefault().sendTextMessage(phoneNumberEditText.getText().toString(), null, smsBodyEditText.getText().toString(), null, null);
                            phoneNumberEditText.setText("");
                            smsBodyEditText.setText("");
                            Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    };
}

