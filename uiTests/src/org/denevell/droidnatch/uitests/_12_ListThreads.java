package org.denevell.droidnatch.uitests;

import android.test.ActivityInstrumentationTestCase2;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.natch.android.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.denevell.droidnatch.uitests.CustomMatchers.*;

public class _12_ListThreads extends ActivityInstrumentationTestCase2<MainPageActivity> {

    @SuppressWarnings("deprecation")
    public _12_ListThreads() {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        getActivity();
    }
    
    public void test_1_ListThreads() throws Exception {
        onView(withId(R.id.list_threads_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.list_threads_listview)).check(matches(listViewHasElements()));
    }

    // I deleted test two, since it relies on the services having not loaded, when Espresso ensures
    // all services and tasks have finished before looking at the views.

}
