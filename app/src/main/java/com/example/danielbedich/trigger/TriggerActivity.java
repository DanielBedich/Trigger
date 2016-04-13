package com.example.danielbedich.trigger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TriggerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Button mButtonNew;
    private ArrayList<String> triggers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        //how to dynamically add items to the list
        //need to learn how to save activity details for when the app closes and then use those activity names to refill list

        triggers = getSharedPreferencesLogList(TriggerActivity.this);

        saveSharedPreferencesLogList(TriggerActivity.this, triggers);
        if(!triggers.isEmpty()) {
            ListView listView = (ListView) findViewById(R.id.triggerlist);
            ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, triggers);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(this);
        }

Log.d("TAGF", ""+triggers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mButtonNew = (Button) findViewById(R.id.new_button);
        mButtonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAction = new Intent(v.getContext(), CreateActivity.class);
                startActivity(newAction);
            }

        });
    }

    public static void saveSharedPreferencesLogList(Context context, ArrayList<String> triggers){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor  mEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(triggers);
        mEditor.putString("Triggers", json);
        mEditor.commit();
    }

    public static ArrayList<String> getSharedPreferencesLogList(Context context) {
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

    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        //write your code here that will start a new intent, something like:
        Intent detailAction = new Intent(v.getContext(), DetailsActivity.class);
        detailAction.putExtra("trigger", position);
        startActivity(detailAction);
    }

}
