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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {

    private static int triggerFlag; //1 for time, 2 for gps
    private static int actionFlag; //1 for reminder, 2, for sms, 3 for call
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
    private ArrayList<String> triggers = new ArrayList<>();
    private String[] triggerArray;
    private String[] actionArray;
    private EditText mNameText;
    private SharedPreferences mPrefs;
    private LocationManager mLocationManager;
    private LatLng currentCoord;
    private LatLng destination;
    private int position;
    private int NOTIF_ID = 0;
    private boolean gpsFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(CreateActivity.this);
        position = this.getIntent().getIntExtra("trigger2", -1);
        triggerArrayList = getSharedPreferencesLogList(CreateActivity.this);
        for(Trigger object: triggerArrayList){
            Log.d("tago",""+object.getActionName());
        }
        //Initialize fields of view
        mSpinnerTrigger = (Spinner) findViewById(R.id.trigger_spinner);
        mSpinnerAction = (Spinner) findViewById(R.id.action_spinner);
        mContactText = (EditText) findViewById(R.id.contact_name);
        mMessageText = (EditText) findViewById(R.id.message_text);
        mNameText = (EditText) findViewById(R.id.action_name);
        mGPSLocationText = (EditText) findViewById(R.id.gps_location);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);

        if(position>=0) {
            mNameText.setText(triggerArrayList.get(position).getActionName());
            mMessageText.setText(triggerArrayList.get(position).getMessage());
            mContactText.setText(triggerArrayList.get(position).getContactNumber());
            mGPSLocationText.setText(triggerArrayList.get(position).getGPS());
        }

        //Trigger roles and setting visible elements
        triggerArray = getResources().getStringArray(R.array.triggerArray);
        ArrayAdapter<String> adapterSpinnerTrigger = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, triggerArray);
        mSpinnerTrigger.setAdapter(adapterSpinnerTrigger);
        if(position>=0)
            mSpinnerTrigger.setSelection(adapterSpinnerTrigger.getPosition(triggerArrayList.get(position).getTriggerType()));
        mSpinnerTrigger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerTrigger.getSelectedItem().toString().equals("Time")) {
                    mTimePicker.setEnabled(true);
                    mTimePicker.setVisibility(View.VISIBLE);
                    mGPSLocationText.setVisibility(View.GONE);
                    triggerFlag = 1;
                } else if(mSpinnerTrigger.getSelectedItem().toString().equals("GPS Arrival")){
                    triggerFlag = 2;
                    mTimePicker.setEnabled(false);
                    mTimePicker.setVisibility(View.GONE);
                    mGPSLocationText.setVisibility(View.VISIBLE);
                }else{
                    triggerFlag = 3;
                    mTimePicker.setEnabled(false);
                    mTimePicker.setVisibility(View.GONE);
                    mGPSLocationText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Action roles and setting visible elements
        actionArray = getResources().getStringArray(R.array.actionArray);
        ArrayAdapter<String> adapterSpinnerAction = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, actionArray);

        mSpinnerAction.setAdapter(adapterSpinnerAction);
        if(position>=0)
            mSpinnerAction.setSelection(adapterSpinnerAction.getPosition(triggerArrayList.get(position).getActionType()));
        mSpinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerAction.getSelectedItem().toString().equals("Call")) {
                    mMessageText.setEnabled(false);
                    mMessageText.setInputType(InputType.TYPE_NULL);
                    mMessageText.setText(null);
                    mMessageText.setText(null);
                    mMessageText.setVisibility(View.GONE);
                    actionFlag = 3;
                } else {
                    mMessageText.setEnabled(true);
                    mMessageText.setInputType(InputType.TYPE_CLASS_TEXT);
                    mMessageText.setVisibility(View.VISIBLE);
                }

                if (mSpinnerAction.getSelectedItem().toString().equals("Reminder")) {
                    mContactText.setEnabled(false);
                    mContactText.setInputType(InputType.TYPE_NULL);
                    mContactText.setText(null);
                    mContactText.setVisibility(View.GONE);
                    actionFlag = 1;

                } else {
                    mContactText.setEnabled(true);
                    mContactText.setInputType(InputType.TYPE_CLASS_TEXT);
                    mContactText.setVisibility(View.VISIBLE);
                }
                if (mSpinnerAction.getSelectedItem().toString().equals("SMS")) {
                    actionFlag = 2;
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
                if(position==-1){
                    currentTrigger = new Trigger(triggerSpinner.getSelectedItem().toString(),
                            actionSpinner.getSelectedItem().toString(),
                            contactNumber.getText().toString(), message.getText().toString(),
                            actionName.getText().toString(), timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute(), mGPSLocationText.getText().toString());
                    triggerArrayList.add(currentTrigger);
                } else{
                    triggerArrayList.get(position).setTriggerType(triggerSpinner.getSelectedItem().toString());
                    triggerArrayList.get(position).setActionType(actionSpinner.getSelectedItem().toString());
                    triggerArrayList.get(position).setContact(contactNumber.getText().toString());
                    triggerArrayList.get(position).setMessage(message.getText().toString());
                    triggerArrayList.get(position).setActionName(actionName.getText().toString());
                    triggerArrayList.get(position).setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    triggerArrayList.get(position).setGPS(mGPSLocationText.getText().toString());
                    triggers = getSharedStringPreferencesLogList(CreateActivity.this);
                    triggers.set(position, actionName.getText().toString());
                    saveSharedStringPreferencesLogList(CreateActivity.this, triggers);
                }

                //setup to get current location
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 15, (float) 20, mLocationListener);
                }

                if (triggerFlag == 1) {
                    //Timer code
                    NOTIF_ID++;
                    Calendar c = Calendar.getInstance();
                    int hourDiff = (mTimePicker.getCurrentHour() - c.get(Calendar.HOUR_OF_DAY)) * 60;
                    //Toast.makeText(CreateActivity.this, "hourDiff " + hourDiff, Toast.LENGTH_LONG).show();
                    if (hourDiff == 0) {
                        hourDiff = 1;
                    }
                    int minDiff = (mTimePicker.getCurrentMinute() - c.get(Calendar.MINUTE)) * 60;
                    if (minDiff == 0) {
                        minDiff = 1;
                    }
                    //Toast.makeText(CreateActivity.this, "minDiff " + minDiff, Toast.LENGTH_LONG).show();

                    Date timeStamp = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
                    Long time = new GregorianCalendar().getTimeInMillis() + hourDiff * minDiff * 1000;

                    Intent intentAlarm = new Intent(CreateActivity.this, TriggerExecution.class);
                    Bundle b = new Bundle();
                    switch (actionFlag) {
                        case 1: //reminder
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Mes", mMessageText.getText().toString());
                            b.putInt("id", NOTIF_ID);
                            break;
                        case 2: //sms
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Num", mContactText.getText().toString());
                            b.putString("Mes", mMessageText.getText().toString());
                            b.putInt("id", NOTIF_ID);
                            break;
                        case 3:
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Num", mContactText.getText().toString());
                            b.putInt("id", NOTIF_ID);
                            break;
                        default:
                            break;

                    }
                    intentAlarm.putExtras(b);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(CreateActivity.this, NOTIF_ID, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                    Toast.makeText(CreateActivity.this, "Alarm Scheduled for " + timeStamp.toString(), Toast.LENGTH_LONG).show();
                }
                if (triggerFlag == 2) {
                    //GPS Code
                }

                //used this to make sure my trigger class was extracting all the information in the class
                saveSharedPreferencesLogList(CreateActivity.this, triggerArrayList);
                Intent saveAction = new Intent(v.getContext(), TriggerActivity.class);
                if(position ==-1) {
                    saveAction.putExtra("actionName", currentTrigger.getActionName());
                }
                startActivity(saveAction);
            }
        });

        mButtonCancel = (Button) findViewById(R.id.cancel_button);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelAction = new Intent(v.getContext(), TriggerActivity.class);
                startActivity(cancelAction);
            }
        });


        mContactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, CONTACT_PICKER);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICKER && resultCode == RESULT_OK) {

            uriContact = data.getData();
            getContactName();
            getContactNumber();
        }
    }

    private void getContactNumber() {
        String contactNumber = null;

        Cursor cursorID = getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = "
                        + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID}, null);

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

    private final LocationListener mLocationListener = new LocationListener() {
        double currentLat;
        double currentLong;

        @Override
        public void onLocationChanged(Location location) {
            currentLat = location.getLatitude();
            currentLong = location.getLongitude();
            currentCoord = new LatLng(currentLat, currentLong);

            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addresses = null;
            double latitude;
            double longitude;
            float distance;
            Intent gpsAlarm = new Intent(CreateActivity.this, TriggerExecution.class);
            try {
                addresses = geocoder.getFromLocationName(mGPSLocationText.getText().toString(), 1);
            } catch (IOException e) {

            }
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
                destination = new LatLng(latitude, longitude);
            }
            if (destination != null && currentCoord != null) {
                Location current = new Location("");
                current.setLatitude(currentCoord.latitude);
                current.setLongitude(currentCoord.longitude);
                Location dest = new Location("");
                dest.setLatitude(destination.latitude);
                dest.setLongitude(destination.longitude);
                distance = current.distanceTo(dest);
                //Toast.makeText(CreateActivity.this, distance+"", Toast.LENGTH_SHORT).show();
                if (triggerFlag == 2 && distance < 50) {
                    gpsFlag = false;
                    Bundle b = new Bundle();
                    switch (actionFlag) {
                        case 1: //reminder
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Mes", mMessageText.getText().toString());
                            break;
                        case 2: //sms
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Num", mContactText.getText().toString());
                            b.putString("Mes", mMessageText.getText().toString());
                            break;
                        case 3: //call
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Num", mContactText.getText().toString());
                            break;
                        default:
                            break;

                    }
                    gpsAlarm.putExtras(b);
                    sendBroadcast(gpsAlarm);
                }else if(triggerFlag == 3 && distance > 100){
                    gpsFlag = false;
                    Bundle b = new Bundle();
                    switch (actionFlag) {
                        case 1: //reminder
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Mes", mMessageText.getText().toString());
                            break;
                        case 2: //sms
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Num", mContactText.getText().toString());
                            b.putString("Mes", mMessageText.getText().toString());
                            break;
                        case 3:
                            b.putInt("actionFlag", actionFlag);
                            b.putString("Num", mContactText.getText().toString());
                            break;
                        default:
                            break;

                    }
                    gpsAlarm.putExtras(b);
                    sendBroadcast(gpsAlarm);
                }
                //Toast.makeText(CreateActivity.this, distance + "", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void gpsTrigger(){

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
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
        SharedPreferences.Editor mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(triggerArrayList);
        mEditor.putString("TriggerList", json);
        mEditor.commit();
    }

    public static ArrayList<Trigger> getSharedPreferencesLogList(Context context) {
        ArrayList<Trigger> triggerArrayList = new ArrayList<>();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = mPrefs.getString("TriggerList", "");
        Type type = new TypeToken<ArrayList<Trigger>>() {
        }.getType();
        if (gson.fromJson(json, type) != null) {
            triggerArrayList = gson.fromJson(json, type);
        }
        return triggerArrayList;
    }

    public static void saveSharedStringPreferencesLogList(Context context, ArrayList<String> triggers){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor  mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(triggers);
        mEditor.putString("Triggers", json);
        mEditor.commit();
    }

    public static ArrayList<String> getSharedStringPreferencesLogList(Context context) {
        ArrayList<String> triggers = new ArrayList<>();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor  mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = mPrefs.getString("Triggers", "");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        //catch null lists
        if(gson.fromJson(json, type)!=null) {
            triggers = gson.fromJson(json, type);
        }
        return triggers;
    }
}
