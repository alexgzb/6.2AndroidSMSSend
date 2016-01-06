package com.gezelbom.androidsms62;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;


/**
 * Simple APP to send SMS
 */
public class MainActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    EditText smsBodyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditText widgets for the number and the smsbody
        phoneNumberEditText = (EditText) findViewById(R.id.editText_number);
        phoneNumberEditText.setText("");
        smsBodyEditText = (EditText) findViewById(R.id.editText_body);
        smsBodyEditText.setText("");

        //Floating Action button for sending SMS
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            /**
             * Anonymous inner OnClickListener For the fab
             * Has an Action which is also an onclicklistener which uses the SmsManager to send an sms
             */
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "To confirm".toUpperCase(), Snackbar.LENGTH_LONG).setAction("Click here", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SmsManager.getDefault().sendTextMessage(phoneNumberEditText.getText().toString(), null, smsBodyEditText.getText().toString(), null, null);
                        phoneNumberEditText.setText("");
                        smsBodyEditText.setText("");
                    }
                }).show();
            }
        });
    }

}
