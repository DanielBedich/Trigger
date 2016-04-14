package com.example.danielbedich.trigger;

import android.app.Application;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private CreateActivity createActivity;
    private Instrumentation mInstrumentation;
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
    }

}
