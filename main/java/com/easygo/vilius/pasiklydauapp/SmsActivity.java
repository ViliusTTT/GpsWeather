package com.easygo.vilius.pasiklydauapp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Klase apdorojanti sms issiuntima
 */
public class SmsActivity extends AppCompatActivity {

    private EditText phoneNumber;   //Telefono numeris
    private TextView smsMessage;    //Zinutes turinys
    String adress;                  //Siunciamas adresas
    private Button sendBtn;         //Siuntimo mygtukas
    private TextView smsStatus;     //Siuntimo status
    private TextView deliveryStatus;//Ateinancios zinutes statusas
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;//Leidimo prasymo uzklausos kodas
    public static final String SMS_SENT_ACTION = "Sms_sent";//Pranesimas issiuntus zinute
    public static final String SMS_DELIVERED_ACTION = "SMS_Delivered";//Pranesimas gavus zintue

    /**
     * onCreate metodas apdoroja sms siuntimo layouta bei reikiamus laukus
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sms);
        handlePermissionsAndSMS();

    }

    /**
     * Siuncia zinute
     */
        public void sendSms(){
            phoneNumber = (EditText) findViewById(R.id.phone_number);
            smsMessage = (TextView) findViewById(R.id.sms_message);
            sendBtn = (Button) findViewById(R.id.send_btn);
            smsStatus = (TextView) findViewById(R.id.message_status);
            deliveryStatus = (TextView) findViewById(R.id.delivery_msg);
            adress = getIntent().getStringExtra("adresas");
            smsMessage.setText(adress);
         sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = phoneNumber.getText().toString();
                String smsBody = adress;
                smsMessage.setText(smsBody);
                //Check if the phoneNumber is empty
                if (phoneNum.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter a Phone Number", Toast.LENGTH_LONG).show();
                } else {
                    sendSMS(phoneNum, smsBody);
                }
            }
        });

        /*
        * Sent Receiver
        * */
            /**
             * Apdoroja zinutes issiuntimo statusa
             */
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = null;
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message = "Message Sent Successfully !";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message = "Error.";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message = "Error: No service.";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        message = "Error: Null PDU.";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        message = "Error: Radio off.";
                        break;
                }

                smsStatus.setText(message);
            }
        }, new IntentFilter(SMS_SENT_ACTION));

        /*
        * Delivery Receiver
        * */
            /**
             *  Apdoroja gaunamos zinutes statusa
             */

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                phoneNumber.setText("");
                smsMessage.setText(adress);

                deliveryStatus.setText("SMS Delivered");
            }
        }, new IntentFilter(SMS_DELIVERED_ACTION));
    }

    /**
     * Gauna reikiamus leidimus ir siuncia zinute
     */
    private void   handlePermissionsAndSMS() {
        Log.v("smthng", "handlePermissionsAndGetLocation");
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        sendSms();//if already has permission
    }

    /**
     * Apdoroja leidimu gavima
     * @param requestCode - uzklausos kodas
     * @param permissions - leidimai
     * @param grantResults - rezultatai
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Accepted
                    sendSms();
                } else {
                    // Denied
                    Toast.makeText(SmsActivity.this, "LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Siuncia sms zinute
     * @param phoneNumber - telefono numeris
     * @param smsMessage - sms zinutes turinys
     */
    public void sendSMS(String phoneNumber, String smsMessage) {
        if (!(checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)) {
            Log.v("Kursinis", "Has permission");
        SmsManager sms = SmsManager.getDefault();
        List<String> messages = sms.divideMessage(smsMessage);
        for (String message : messages) {

            /*
            * sendTextMessage (String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent)
            *
            * Sent Intent: fired when the message is sent and indicates if it's successfully sent or not
            *
            * Delivery Intent: fired when the message is sent and delivered
            * */

            sms.sendTextMessage(phoneNumber, null, message, PendingIntent.getBroadcast(
                    this, 0, new Intent(SMS_SENT_ACTION), 0), PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED_ACTION), 0));
        }
        } else {
            Log.v("Project", "Does not have permission");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
