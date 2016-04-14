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

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText mOldPass;
    private EditText mNewPass;
    private EditText mConPass;
    private Button mEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        final String password = PreferenceManager.getDefaultSharedPreferences(ChangePasswordActivity.this).getString("Password", "fail");

        mOldPass = (EditText) findViewById(R.id.oldpassword);
        mNewPass = (EditText) findViewById(R.id.newpassword);
        mConPass = (EditText) findViewById(R.id.confirmpassword);
        mEnter = (Button) findViewById(R.id.button);

        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOldPass.getText().toString().equals(password) && mNewPass.getText().toString().equals(mConPass.getText().toString())) {
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ChangePasswordActivity.this);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("Password", mNewPass.getText().toString());
                    mEditor.commit();
                    Toast.makeText(ChangePasswordActivity.this, "Password changed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChangePasswordActivity.this, TriggerActivity.class));
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Change failed", Toast.LENGTH_SHORT).show();
                    mOldPass.setText("");
                    mNewPass.setText("");
                    mConPass.setText("");
                }
            }
        });
    }
}
