package com.example.danielbedich.trigger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TriggerActivity extends AppCompatActivity {

    private Button mButtonNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
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

}
