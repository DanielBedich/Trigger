package com.example.danielbedich.trigger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetailsActivity extends AppCompatActivity {

    private Button mButtonBack;
    private Button mButtonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mButtonBack = (Button) findViewById(R.id.back_button);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backAction = new Intent(v.getContext(), TriggerActivity.class);
                startActivity(backAction);
            }
        });

        mButtonEdit = (Button) findViewById(R.id.edit_button);
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAction = new Intent(v.getContext(), CreateActivity.class);
                startActivity(editAction);
            }
        });
    }

}
