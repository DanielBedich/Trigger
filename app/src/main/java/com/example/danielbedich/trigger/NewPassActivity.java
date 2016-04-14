package com.example.danielbedich.trigger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NewPassActivity extends AppCompatActivity {

    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);

        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(NewPassActivity.this);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("Password", mPassword.getText().toString());
                    mEditor.commit();
                    Toast.makeText(NewPassActivity.this, "Password accepted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewPassActivity.this, TriggerActivity.class));
                } else {
                    Toast.makeText(NewPassActivity.this, "Password denied", Toast.LENGTH_SHORT).show();
                    mPassword.setText("");
                    mConfirmPassword.setText("");
                }
            }
        });

    }
}
