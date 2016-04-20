package com.example.danielbedich.trigger;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danielbedich.trigger.CreateActivity;
import com.example.danielbedich.trigger.DetailsActivity;
import com.example.danielbedich.trigger.R;
import com.example.danielbedich.trigger.Trigger;
import com.example.danielbedich.trigger.TriggerActivity;

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
        mEditBtn = mDetailsActivity.findViewById(R.id.editbutton);
        mDeletePWBtn = mDetailsActivity.findViewById(R.id.deletebutton);
        mBackPWBtn = mDetailsActivity.findViewById(R.id.backbutton);
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
        final Button backBtn = (Button) myActivity.findViewById(R.id.backbutton);
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

            ArrayList<Trigger> triggerArrayList = DetailsActivity.getSharedTriggerPreferencesLogList(mDetailsActivity);
        if(triggerArrayList.isEmpty()) {
            triggerArrayList.add(new Trigger("Time", "SMS", "5672247591", "message", "name", 1, 2, "", 999999));
            DetailsActivity.saveSharedTriggerPreferencesLogList(mDetailsActivity, triggerArrayList);
        }
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CreateActivity.class.getName(), null, false);

        // open current activity.
        DetailsActivity myActivity = getActivity();
        final Button editBtn = (Button) myActivity.findViewById(R.id.editbutton);
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
        if(triggerArrayList.get(0).getActionName().equals("name")) {
            triggerArrayList.remove(0);
            DetailsActivity.saveSharedTriggerPreferencesLogList(mDetailsActivity, triggerArrayList);
        }
    }

    public void testDeleteStartsTriggerActivity() {

        ArrayList<Trigger> triggerArrayList = DetailsActivity.getSharedTriggerPreferencesLogList(mDetailsActivity);
        triggerArrayList.add(0, new Trigger("Time", "SMS", "5672247591", "message","name",1,2,"",999999));
            DetailsActivity.saveSharedTriggerPreferencesLogList(mDetailsActivity, triggerArrayList);
            int size = triggerArrayList.size()-1;

        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(TriggerActivity.class.getName(), null, false);

        // open current activity.
        DetailsActivity myActivity = getActivity();
        final Button deleteBtn = (Button) myActivity.findViewById(R.id.deletebutton);
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
        assertNotNull(triggerActivity);
        triggerActivity.finish();

        triggerArrayList = DetailsActivity.getSharedTriggerPreferencesLogList(mDetailsActivity);
        assertEquals(size, triggerArrayList.size());
    }

    protected void tearDown() throws Exception{
        mDetailsActivity.finish();
    }
}
