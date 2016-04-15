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
public class TriggerActivityTest extends ActivityInstrumentationTestCase2<TriggerActivity>  {
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

    public void testNewButtonStartsCreateActivity() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CreateActivity.class.getName(), null, false);

        // open current activity.
        TriggerActivity myActivity = getActivity();
        final Button newBtn = (Button) myActivity.findViewById(R.id.new_button);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                newBtn.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        CreateActivity createActivity = (CreateActivity)getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(createActivity);
        createActivity.finish();
    }


    public void testChangePasswordButtonLoadsChangePasswordActivity() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ChangePasswordActivity.class.getName(), null, false);

        // open current activity.
        TriggerActivity myActivity = getActivity();
        final Button chgBtn = (Button) myActivity.findViewById(R.id.changeBtn);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                chgBtn.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        ChangePasswordActivity changePasswordActivity = (ChangePasswordActivity)getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(changePasswordActivity);
        changePasswordActivity.finish();
    }

    public void testListItemStartsDetailsActivity(){
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(DetailsActivity.class.getName(), null, false);

        // open current activity.
        TriggerActivity myActivity = getActivity();
        final View view = (View) myActivity.findViewById(R.id.triggerlist);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.

            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        DetailsActivity detailsActivity = (DetailsActivity)getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(detailsActivity);
        detailsActivity.finish();
    }

    protected void tearDown() throws Exception{
        mTriggerActivity.finish();
    }
}
