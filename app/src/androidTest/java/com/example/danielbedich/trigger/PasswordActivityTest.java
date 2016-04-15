package com.example.danielbedich.trigger;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by DanielBedich on 4/15/16.
 */
public class PasswordActivityTest extends ActivityInstrumentationTestCase2<PasswordActivity> {
    private PasswordActivity mPasswordActivity;
    private View mSubmitBtn;
    private EditText mEditText;
    private Instrumentation mInstrumentation;
    String pass;
    SharedPreferences prefs;
    boolean b;
    private static int TIMEOUT_IN_MS = 10000;

    public PasswordActivityTest() {
        super(PasswordActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);
        mPasswordActivity = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(mPasswordActivity);
        mSubmitBtn = mPasswordActivity.findViewById(R.id.btnSubmit);
        mEditText = (EditText)mPasswordActivity.findViewById(R.id.txtPassword);
        mInstrumentation = getInstrumentation();


        prefs = PreferenceManager.getDefaultSharedPreferences(mPasswordActivity);
        SharedPreferences.Editor mEditor = prefs.edit();
        mEditor.putString("PasswordTest", "123");
        mEditor.commit();
        pass = prefs.getString("PasswordTest", "fail");
    }

    public void testPreconditions(){
        assertNotNull(mPasswordActivity);
        assertNotNull(mSubmitBtn);
        assertNotNull(mEditText);
    }


    @UiThreadTest
    public void testPasswordTextFocus() {
        assertNotNull(mEditText);
        assertTrue(mEditText.requestFocus());
        assertTrue(mEditText.hasFocus());
    }

    @UiThreadTest
    public void testCorrectPassword() {
        // register next activity that need to be monitored.
        mEditText.setText("123");
        b=mPasswordActivity.checkPassword(pass,mEditText.getText().toString());
        assertTrue(b);
    }

    @UiThreadTest
    public void testWrongPassword() {
        // register next activity that need to be monitored.
        mEditText.setText("000");
        b=mPasswordActivity.checkPassword(pass,mEditText.getText().toString());
        assertFalse(b);
    }
}
