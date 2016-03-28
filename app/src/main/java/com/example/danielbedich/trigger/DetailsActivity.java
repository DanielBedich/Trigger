package com.example.danielbedich.trigger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private Button mButtonBack;
    private Button mButtonEdit;
    private Button mButtonDelete;
    private TextView nameDetails;
    private TextView triggerDetails;
    private TextView actionDetails;
    private TextView contactDetails;
    private TextView messageDetails;
    private int position;
    private ArrayList<Trigger> triggerArrayList = new ArrayList<>();
    private ArrayList<String> triggers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        nameDetails =(TextView)findViewById(R.id.name);
        triggerDetails =(TextView)findViewById(R.id.trigger);
        actionDetails =(TextView)findViewById(R.id.action);
        contactDetails =(TextView)findViewById(R.id.contact);
        messageDetails =(TextView)findViewById(R.id.message);
        position = (int) this.getIntent().getIntExtra("trigger", 0);
        triggerArrayList = getSharedTriggerPreferencesLogList(DetailsActivity.this);
        triggers = getSharedStringPreferencesLogList(DetailsActivity.this);
        Log.d("tag2",""+position);
        nameDetails.setText("Name: " + triggerArrayList.get(position).getActionName());
        triggerDetails.setText("Trigger: " + triggerArrayList.get(position).getTriggerType());
        actionDetails.setText("Action: " + triggerArrayList.get(position).getActionType());
        contactDetails.setText("Contact: " + triggerArrayList.get(position).getContactNumber());
        messageDetails.setText("Message: " + triggerArrayList.get(position).getMessage());
        


        mButtonBack = (Button) findViewById(R.id.back_button);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backAction = new Intent(v.getContext(), TriggerActivity.class);
                startActivity(backAction);
            }
        });

        mButtonDelete = (Button) findViewById(R.id.delete_button);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent delAction = new Intent(v.getContext(), TriggerActivity.class);
                delete(position);
                startActivity(delAction);
            }
        });

        mButtonEdit = (Button) findViewById(R.id.edit_button);
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAction = new Intent(v.getContext(), CreateActivity.class);
                editAction.putExtra("trigger2", position);
                startActivity(editAction);
            }
        });

    }

    public void delete(int position){
        Context context = DetailsActivity.this;
        ArrayList<Trigger> triggerArrayList = getSharedTriggerPreferencesLogList(DetailsActivity.this);
        ArrayList<String> triggers = getSharedStringPreferencesLogList(DetailsActivity.this);
        int NOTIF_ID = triggerArrayList.get(position).getID();
        int actionFlag=0;


        if (triggerArrayList.get(position).getActionType().equals("Call")) {

            actionFlag = 3;
        }

        if (triggerArrayList.get(position).getActionType().equals("Reminder")) {

            actionFlag = 1;

        }

        if (triggerArrayList.get(position).getActionType().equals("SMS")) {
            actionFlag = 2;
        }

        Intent intentAlarm = new Intent(context, TriggerExecution.class);
        Bundle b = new Bundle();
        switch (actionFlag) {
            case 1: //reminder
                b.putInt("actionFlag", actionFlag);
                b.putString("Mes", triggerArrayList.get(position).getMessage());
                break;
            case 2: //sms
                b.putInt("actionFlag", actionFlag);
                b.putString("Num", triggerArrayList.get(position).getContactNumber());
                b.putString("Mes", triggerArrayList.get(position).getMessage());
                break;
            case 3:
                b.putInt("actionFlag", actionFlag);
                b.putString("Num", triggerArrayList.get(position).getContactNumber());
                break;
            default:
                break;

        }
        intentAlarm.putExtras(b);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context, NOTIF_ID, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        //used this to make sure my trigger class was extracting all the information in the class
        triggerArrayList.remove(position);
        triggers.remove(position);
        saveSharedTriggerPreferencesLogList(context, triggerArrayList);
        saveSharedStringPreferencesLogList(context, triggers);
    }



    public static void saveSharedTriggerPreferencesLogList(Context context, ArrayList<Trigger> triggerArrayList) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(triggerArrayList);
        mEditor.putString("TriggerList", json);
        mEditor.commit();
    }

    public static ArrayList<Trigger> getSharedTriggerPreferencesLogList(Context context) {
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
