package com.gezelbom.androidsms62;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;


/**
 * Simple APP to send and receive SMS.
 * The send SMS functionality has copy/paste functionality as well as possibility to get number from contacts.
 * Possibility to speak the text from the body TextToSpeech has been implemented
 * Displays a notification with flashing led lights and vibration when a SMS has been received
 */
public class MainActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    EditText smsBodyEditText;
    Button pickButton;
    Button speakButton;
    Button clearButton;
    Button copyButton;
    Button pasteButton;
    TextToSpeech textToSpeech;
    ClipboardManager myClipboard;
    public final int PICK_CONTACT = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ClipboardManager from the system
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //SpeakButton setup
        speakButton = (Button) findViewById(R.id.buttonSpeak);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // Use the textToSpeech object to speak the text from the body
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(smsBodyEditText.getText().toString(), TextToSpeech.QUEUE_ADD, null, "SMS-SPEAK");
                } else {
                    textToSpeech.speak(smsBodyEditText.getText().toString(), TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

        //ClearButton setup
        clearButton = (Button) findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsBodyEditText.setText("");
            }
        });

        //CopyButton setup
        copyButton = (Button) findViewById(R.id.buttonCopy);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clipData = ClipData.newPlainText("Text", smsBodyEditText.getText());
                myClipboard.setPrimaryClip(clipData);
            }
        });


        //PasteButton setup
        pasteButton = (Button) findViewById(R.id.buttonPaste);
        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsBodyEditText.setText(smsBodyEditText.getText().toString() + myClipboard.getPrimaryClip().getItemAt(0).getText());
            }
        });

        //PickButton Setup starts a contact picker dialog
        pickButton = (Button) findViewById(R.id.buttonPick);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });

        //EditText widgets for the number and the smsBody
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
                Snackbar.make(view, "To confirm".toUpperCase(), Snackbar.LENGTH_LONG).setAction("Click here", dialogBoxClickListener).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Creating a new instance of textToSpeech Class and setting language to English
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        textToSpeech.shutdown();
    }

    /**
     * Method to react on Activity Result If the result is from ContactPicker, get the number from the
     * chosen contact
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {

            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNumberEditText.setText(cursor.getString(column));
                cursor.close();
            }
        }
    }

    /**
     * OnClick Listener that creates a Alert Dialog, if the user Clicks on Yes, the sms is sent, otherwise not
     */
    View.OnClickListener dialogBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder
                    .setTitle("Are you sure?")
                    .setMessage("Sending SMS will probably cost, Do you want to continue?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Send an SMS using SmsManager
                            SmsManager.getDefault().sendTextMessage(phoneNumberEditText.getText().toString(), null, smsBodyEditText.getText().toString(), null, null);
                            phoneNumberEditText.setText("");
                            smsBodyEditText.setText("");
                            Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Close the dialog
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    };
}

