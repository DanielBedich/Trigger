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

public class PasswordActivity extends AppCompatActivity {

    private EditText mPassText;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity

            startActivity(new Intent(PasswordActivity.this, NewPassActivity.class));
            Toast.makeText(PasswordActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        final String password = PreferenceManager.getDefaultSharedPreferences(PasswordActivity.this).getString("Password", "fail");

        mPassText = (EditText) findViewById(R.id.txtPassword);

        mSubmit = (Button) findViewById(R.id.btnSubmit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mPassText.getText().toString();
                if (checkPassword(text,password)) {
                    Toast.makeText(PasswordActivity.this, "Password correct", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PasswordActivity.this, TriggerActivity.class));
                } else {
                    Toast.makeText(PasswordActivity.this, "Password wrong", Toast.LENGTH_SHORT).show();
                    mPassText.setText("");
                }
            }
        });
    }

    public boolean checkPassword(String text, String password){
        boolean b;
        if(text.equals(password)){
            b=true;
        }
        else{
            b=false;
        }
        return b;
    }
}
