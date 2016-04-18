package com.example.danielbedich.trigger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TriggerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Button mButtonNew;
    private Button mChangePassBtn;
    private ArrayList<Trigger> triggerArrayList = new ArrayList<>();
    private ArrayList<String> triggerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        //how to dynamically add items to the list
        //need to learn how to save activity details for when the app closes and then use those activity names to refill list

        triggerArrayList = getSharedPreferencesLogList(TriggerActivity.this);

        if(!triggerArrayList.isEmpty()) {

            for(Trigger triggerObject : triggerArrayList){
                triggerList.add(triggerObject.getActionName());
            }

            ListView listView = (ListView) findViewById(R.id.triggerlist);
            ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, triggerList);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(this);
        }

        Log.d("TAGF", ""+ triggerList);
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

        mChangePassBtn = (Button) findViewById(R.id.changeBtn);
        mChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TriggerActivity.this, ChangePasswordActivity.class));
            }
        });
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

    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        //write your code here that will start a new intent, something like:
        Intent detailAction = new Intent(v.getContext(), DetailsActivity.class);
        detailAction.putExtra("trigger", position);
        startActivity(detailAction);
    }

}
