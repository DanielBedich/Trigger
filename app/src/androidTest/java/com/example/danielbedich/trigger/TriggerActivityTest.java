package com.example.danielbedich.trigger;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by DanielBedich on 4/14/16.
 */
public class TriggerActivityTest extends ActivityInstrumentationTestCase2<TriggerActivity> {
    private TriggerActivity mTriggerActivity;
    private View mList;
    private View mNewBtn;
    private View mChangePWBtn;
    private Instrumentation mInstrumentation;

    private static int TIMEOUT_IN_MS = 10000;

    public TriggerActivityTest(){
        super(TriggerActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        setActivityInitialTouchMode(false);
        mTriggerActivity = getActivity();
        mList = mTriggerActivity.findViewById(R.id.triggerlist);
        mNewBtn = mTriggerActivity.findViewById(R.id.new_button);
        mChangePWBtn = mTriggerActivity.findViewById(R.id.changeBtn);
        mInstrumentation = getInstrumentation();
    }

    public void testPreconditions(){
        assertNotNull(mTriggerActivity);
        assertNotNull(mChangePWBtn);
        assertNotNull(mNewBtn);
        assertNotNull(mList);

    }

    public void testNewButtonLoadsCreateActivity() {
        // Set up Activity Monitor
        Instrumentation.ActivityMonitor createActivityMonitor =
                mInstrumentation.addMonitor(CreateActivity.class.getName(),
                        null, false);

        // Click New Button
        TouchUtils.clickView(this, mNewBtn);

        // Wait for the Activity to Load
        CreateActivity receiverActivity = (CreateActivity)
                createActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);

        // Check the Activity has exists
        assertNotNull("CreateActivity is null", receiverActivity);

        // Check the Activity has loaded
        assertEquals("Monitor for SearchActivity has not been called",
                1, createActivityMonitor.getHits());

        // Remove the Activity Monitor
        getInstrumentation().removeMonitor(createActivityMonitor);
    }

    public void testChangePasswordButtonLoadsChangePasswordActivity() {
        // Set up Activity Monitor
        Instrumentation.ActivityMonitor changePasswordActivityMonitor =
                mInstrumentation.addMonitor(ChangePasswordActivity.class.getName(),
                        null, false);

        // Click ActionBar Your Holiday Icon
        TouchUtils.clickView(this, mChangePWBtn);

        // Wait for the Activity to Load
        ChangePasswordActivity receiverActivity = (ChangePasswordActivity)
                changePasswordActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);

        // Check the Activity has exists
        assertNotNull("ChangePasswordActivity is null", receiverActivity);

        // Check the Activity has loaded
        assertEquals("Monitor for YourHolidayActivity has not been called",
                1, changePasswordActivityMonitor.getHits());

        // Remove the Activity Monitor
        getInstrumentation().removeMonitor(changePasswordActivityMonitor);
    }

    protected void tearDown() throws Exception{
        mTriggerActivity.finish();
    }
}
