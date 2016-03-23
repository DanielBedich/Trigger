package com.example.danielbedich.trigger;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.TimePickerDialog;

public class CreateActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER = 1;
    private static final int LOCATION_PICKER = 1;
    private static final String DEBUG_TAG = null;
    private Button mButtonSave;
    private Button mButtonCancel;
    private EditText mContactText;
    private EditText mMessageText;
    private String contactID;
    private Uri uriContact;
    private NotificationManager mNotificationManager;
    private Spinner mSpinnerTrigger;
    private Spinner mSpinnerAction;
    private TimePicker mTimePicker;
    private EditText mGPSLocationText;
    private String[] triggerArray;
    private String[] actionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mGPSLocationText = (EditText) findViewById(R.id.gps_location);
        mGPSLocationText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent gpsAction = new Intent(v.getContext(), MapsActivity.class);
                startActivity(gpsAction);
            }
        });

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mMessageText = (EditText) findViewById(R.id.message_text);
        mSpinnerAction = (Spinner) findViewById(R.id.action_spinner);

        actionArray = getResources().getStringArray(R.array.actionArray);
        ArrayAdapter<String> adapterSpinnerAction  = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, actionArray);

        mSpinnerAction.setAdapter(adapterSpinnerAction);
        mSpinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerAction.getSelectedItem().toString().equals("Call")) {
                    mMessageText.setEnabled(false);
                    mMessageText.setInputType(InputType.TYPE_NULL);
                    mMessageText.setText(null);
                    mMessageText.setVisibility(View.GONE);
                    //mMessageText.setFocusable(false);
                } else {
                    mMessageText.setEnabled(true);
                    mMessageText.setInputType(InputType.TYPE_CLASS_TEXT);
                    mMessageText.setVisibility(View.VISIBLE);
                    //mMessageText.setFocusable(true);
                }

                if (mSpinnerAction.getSelectedItem().toString().equals("Reminder")) {
                    mContactText.setEnabled(false);
                    mContactText.setInputType(InputType.TYPE_NULL);
                    mContactText.setText(null);
                    mContactText.setVisibility(View.GONE);
                    //mMessageText.setFocusable(false);

                } else {
                    mContactText.setEnabled(true);
                    mContactText.setInputType(InputType.TYPE_CLASS_TEXT);
                    mContactText.setVisibility(View.VISIBLE);
                    //mMessageText.setFocusable(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mSpinnerTrigger = (Spinner) findViewById(R.id.trigger_spinner);

        triggerArray = getResources().getStringArray(R.array.triggerArray);
        ArrayAdapter<String> adapterSpinnerTrigger  = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, triggerArray);

        mSpinnerTrigger.setAdapter(adapterSpinnerTrigger);
        mSpinnerTrigger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mSpinnerTrigger.getSelectedItem().toString().equals("Time")){
                    mTimePicker.setEnabled(true);
                    mTimePicker.setVisibility(View.VISIBLE);
                    mGPSLocationText.setVisibility(View.GONE);
                } else {
                    mTimePicker.setEnabled(false);
                    mTimePicker.setVisibility(View.GONE);
                    mGPSLocationText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mButtonSave = (Button) findViewById(R.id.save_button);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saveAction = new Intent(v.getContext(), TriggerActivity.class);
                startActivity(saveAction);
            }
        });

        mButtonCancel = (Button) findViewById(R.id.cancel_button);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelAction = new Intent(v.getContext(), TriggerActivity.class);
                startActivity(cancelAction);


                NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(v.getContext());
                notifBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
                notifBuilder.setContentTitle("Trigger");
                notifBuilder.setTicker("New Trigger!");
                notifBuilder.setContentText(mMessageText.getText().toString());

                long[] vibrateTime = {2000};
                notifBuilder.setVibrate(vibrateTime);
                Intent resultIntent = new Intent(CreateActivity.this, TriggerActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(CreateActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notifBuilder.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(1, notifBuilder.build());
/**
                //Call the specified number
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + mContactText.getText().toString()));
                if(PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(call);
                }

                //How to text someone
                SmsManager smsMan = SmsManager.getDefault();
                String num = mContactText.getText().toString();
                String mes= mMessageText.getText().toString();
                smsMan.sendTextMessage(num,null,mes,null, null);
 */
            }
        });

        mContactText = (EditText) findViewById(R.id.contact_name);
        mContactText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, CONTACT_PICKER);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CONTACT_PICKER && resultCode == RESULT_OK){

            uriContact = data.getData();
            getContactName();
            getContactNumber();
        }
    }

    private void getContactNumber(){
        String contactNumber = null;

        Cursor cursorID = getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID},null, null,null);

        if(cursorID.moveToFirst()){
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ? AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE+" = "
                        + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID},null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        mContactText.setText(contactNumber);
        cursorPhone.close();
    }

    private void getContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();

    }


}