package com.example.danielbedich.trigger;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {


    private static final String TAG = "CreateActivity";
    private static final String TRIGGER_INDEX = "Time";
    private static final String ACTION_INDEX = "Reminder";
    private static final String CONTACT_INDEX = null;
    private static final String MESSAGE_INDEX = null;
    private static final String NAME_INDEX = null;
    private static final String LOCATION_INDEX = null;
    private static final int CONTACT_PICKER = 1;
    private static final int LOCATION_PICKER = 2;
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
    private EditText contactNumber;
    private EditText message;
    private EditText actionName;
    private Spinner triggerSpinner;
    private Spinner actionSpinner;
    private TimePicker timePicker;
    private Trigger currentTrigger;
    private EditText mGPSLocationText;
    private ArrayList<Trigger> triggerArrayList = new ArrayList<>();
    private String[] triggerArray;
    private String[] actionArray;
    private EditText mNameText;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mNameText = (EditText) findViewById(R.id.action_name);

        mGPSLocationText = (EditText) findViewById(R.id.gps_location);

        mGPSLocationText.setText(getIntent().getStringExtra("mytext"));
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
                triggerSpinner = (Spinner) findViewById(R.id.trigger_spinner);
                actionSpinner = (Spinner) findViewById(R.id.action_spinner);
                contactNumber = (EditText) findViewById(R.id.contact_name);
                message = (EditText) findViewById(R.id.message_text);
                actionName = (EditText) findViewById(R.id.action_name);
                timePicker = (TimePicker) findViewById(R.id.timePicker);
                currentTrigger = new Trigger(triggerSpinner.getSelectedItem().toString(),
                        actionSpinner.getSelectedItem().toString(),
                        contactNumber.getText().toString(), message.getText().toString(),
                        actionName.getText().toString(), timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                triggerArrayList = getSharedPreferencesLogList(CreateActivity.this);
                triggerArrayList.add(currentTrigger);
                //used this to make sure my trigger class was extracting all the information in the class
                saveSharedPreferencesLogList(CreateActivity.this, triggerArrayList);
                Log.d("actionName", "onClick: " + triggerArrayList.get(0).getActionName());
                Intent saveAction = new Intent(v.getContext(), TriggerActivity.class);
                saveAction.putExtra("actionName",currentTrigger.getActionName());
                startActivity(saveAction);
            }
        });

        mButtonCancel = (Button) findViewById(R.id.cancel_button);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent cancelAction = new Intent(v.getContext(), TriggerActivity.class);
                //startActivity(cancelAction);

                /*
                Geocoder geocoder = new Geocoder(getApplicationContext());
                List<Address> addresses = null;
                try{
                    addresses = geocoder.getFromLocationName(mGPSLocationText.getText().toString(),1);
                } catch (IOException e){

                }
                if(addresses.size()>0){
                    double latitude = addresses.get(0).getLatitude();
                    double longitude = addresses.get(0).getLongitude();
                    mMessageText.setText(latitude+"");
                    mContactText.setText(longitude+"");
                }
                Toast.makeText(CreateActivity.this, "hourDiff "+hourDiff, Toast.LENGTH_LONG).show();

                int minDiff = (mTimePicker.getCurrentMinute()-c.get(Calendar.MINUTE))*60;
                if(minDiff==0){
                    minDiff=1;
                }
                Toast.makeText(CreateActivity.this, "minDiff "+minDiff, Toast.LENGTH_LONG).show();


                Long time2 =  new GregorianCalendar().getTimeInMillis()+hourDiff*minDiff*1000;

                Intent intentAlarm = new Intent(CreateActivity.this, TriggerExecution.class);
                Bundle b = new Bundle();
                b.putString("Num", mContactText.getText().toString());
                b.putString("Mes", mMessageText.getText().toString());
                intentAlarm.putExtras(b);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, time2, PendingIntent.getBroadcast(CreateActivity.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                Toast.makeText(CreateActivity.this, "Alarm Scheduled for "+timeStamp.toString(), Toast.LENGTH_LONG).show();

                //create an AlarmManager for scenario of picking time
                /*Intent timeIntent = new Intent(CreateActivity.this, TriggerExecution.class);
                PendingIntent pendTimeIntent = PendingIntent.getService(CreateActivity.this, 1, timeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmMan = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.add(Calendar.SECOND, 10);
                /**
                 cal.add(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                 cal.add(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                 cal.add(Calendar.SECOND, 0);
                 cal.add(Calendar.MILLISECOND, 0);

                alarmMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendTimeIntent);*/

                /*
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
                smsMan.sendTextMessage(num,null,mes,null, null); */
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
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putString(TRIGGER_INDEX, mSpinnerTrigger.toString());
        savedInstanceState.putString(ACTION_INDEX, mSpinnerAction.toString());
        savedInstanceState.putString(NAME_INDEX, mNameText.toString());
        savedInstanceState.putString(MESSAGE_INDEX, mMessageText.toString());
        savedInstanceState.putString(CONTACT_INDEX, mContactText.toString());
        savedInstanceState.putString(LOCATION_INDEX, mGPSLocationText.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void saveSharedPreferencesLogList(Context context, ArrayList<Trigger> triggerArrayList) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor  mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(triggerArrayList);
        mEditor.putString("TriggerList", json);
        mEditor.commit();
    }

    public static ArrayList<Trigger> getSharedPreferencesLogList(Context context) {
        ArrayList<Trigger> triggerArrayList = new ArrayList<>();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor  mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = mPrefs.getString("TriggerList", "");
        Type type = new TypeToken<ArrayList<Trigger>>(){}.getType();
        triggerArrayList = gson.fromJson(json, type);
        return triggerArrayList;
    }

}