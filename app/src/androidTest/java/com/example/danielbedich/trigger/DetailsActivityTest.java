package com.example.danielbedich.trigger;

import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telecom.Call;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Brock Sokevitz on 4/14/16.
 */
public class DetailsActivityTest extends ActivityInstrumentationTestCase2<DetailsActivity>  {
    private DetailsActivity mDetailsActivity;
    private View mName;
    private View mTrigger;
    private View mAction;
    private View mContact;
    private View mMessage;
    private View mEditBtn;
    private View mDeletePWBtn;
    private View mBackPWBtn;
    private Instrumentation mInstrumentation;

    private static int TIMEOUT_IN_MS = 10000;

    public DetailsActivityTest(){
        super(DetailsActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        setActivityInitialTouchMode(false);
        mDetailsActivity = getActivity();
        mName = mDetailsActivity.findViewById(R.id.name);
        mTrigger = mDetailsActivity.findViewById(R.id.trigger);
        mAction = mDetailsActivity.findViewById(R.id.action);
        mContact = mDetailsActivity.findViewById(R.id.contact);
        mMessage = mDetailsActivity.findViewById(R.id.message);
        mEditBtn = mDetailsActivity.findViewById(R.id.edit_button);
        mDeletePWBtn = mDetailsActivity.findViewById(R.id.delete_button);
        mBackPWBtn = mDetailsActivity.findViewById(R.id.back_button);
        mInstrumentation = getInstrumentation();
    }

    public void testPreconditions(){
        assertNotNull(mDetailsActivity);
        assertNotNull(mName);
        assertNotNull(mTrigger);
        assertNotNull(mAction);
        assertNotNull(mContact);
        assertNotNull(mMessage);
        assertNotNull(mEditBtn);
        assertNotNull(mDeletePWBtn);
        assertNotNull(mBackPWBtn);

    }

    public void testBackButtonStartsTriggerActivity() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(TriggerActivity.class.getName(), null, false);

        // open current activity.
        DetailsActivity myActivity = getActivity();
        final Button backBtn = (Button) myActivity.findViewById(R.id.back_button);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                backBtn.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        TriggerActivity triggerActivity = (TriggerActivity)getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(triggerActivity);
        triggerActivity.finish();
    }


    public void testEditButtonStartsCreateActivity() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CreateActivity.class.getName(), null, false);

        // open current activity.
        DetailsActivity myActivity = getActivity();
        final Button editBtn = (Button) myActivity.findViewById(R.id.edit_button);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                editBtn.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        CreateActivity createActivity = (CreateActivity)getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(createActivity);
        createActivity.finish();
    }

    public void testDeleteStartsTriggerActivity() {

        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(TriggerActivity.class.getName(), null, false);

        // open current activity.
        DetailsActivity myActivity = getActivity();
        ArrayList<Trigger> triggerArrayList = DetailsActivity.getSharedTriggerPreferencesLogList(myActivity);
        Trigger trigger = new Trigger("Time","SMS","5672247591","message","name",1,2,"",999999);
        int size = triggerArrayList.size();
        triggerArrayList.add(0,trigger);
        DetailsActivity.saveSharedTriggerPreferencesLogList(myActivity, triggerArrayList);
        final Button deleteBtn = (Button) myActivity.findViewById(R.id.edit_button);

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                deleteBtn.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        TriggerActivity triggerActivity = (TriggerActivity)getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        triggerArrayList = DetailsActivity.getSharedTriggerPreferencesLogList(myActivity);
        assertEquals(size, triggerArrayList.size());
        assertNotNull(triggerActivity);
        triggerActivity.finish();
    }

    protected void tearDown() throws Exception{
        mDetailsActivity.finish();
    }
}
