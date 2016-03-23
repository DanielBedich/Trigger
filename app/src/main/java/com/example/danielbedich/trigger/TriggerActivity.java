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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TriggerActivity extends AppCompatActivity {

    private Button mButtonNew;
    private ArrayList<Trigger> triggerArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        //how to dynamically add items to the list
        //need to learn how to save activity details for when the app closes and then use those activity names to refill list
        ArrayList<String> triggers = new ArrayList<>();
        if(this.getIntent().getStringExtra("actionName") != null) {
            triggers.add(this.getIntent().getStringExtra("actionName"));
        }
        ListView listView = (ListView)findViewById(R.id.triggerlist);
        StringArrayAdapter listAdapter = new StringArrayAdapter(triggers, this);
        listView.setAdapter(listAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mButtonNew = (Button) findViewById(R.id.new_button);
        mButtonNew.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TriggerActivity.this);
                builder.setPositiveButton("New", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Intent newAction = new Intent(TriggerActivity.this, CreateActivity.class);
                        startActivity(newAction);
                    }
                })
                .setNegativeButton("Save", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Intent saveAction = new Intent(TriggerActivity.this, SavedActivity.class);
                        startActivity(saveAction);
                    }
                });
                builder.show();
            }

        });
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
