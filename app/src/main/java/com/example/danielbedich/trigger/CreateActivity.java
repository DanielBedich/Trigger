package com.example.danielbedich.trigger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class CreateActivity extends AppCompatActivity {

    private Button mButtonSave;
    private Button mButtonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

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
            }
        });
    }



}
