package com.example.danielbedich.trigger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SavedActivity extends AppCompatActivity {

    private Button mButtonOK;
    private Button mButtonEdit;
    private Button mButtonDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        mButtonOK = (Button) findViewById(R.id.ok_button);
        mButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent okAction = new Intent(v.getContext(), TriggerActivity.class);
                startActivity(okAction);
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
