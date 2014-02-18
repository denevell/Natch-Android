package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.natch.android.R;

public class _012_ListThreads extends NatchAndroidInstrumentationWithLogin {

    public _012_ListThreads() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test_1_ListThreads() throws Exception {
        new ListThreadsPO().checkNoThreads();
        new AddThreadPO().addThread("Listing threads", "Listing threads");
        pressBack();
        onView(withId(R.id.list_threads_listview)).check(matches(isDisplayed()));
        new ListThreadsPO().checkHasNumberOfThreads(1);
    }

    // I deleted test two, since it relies on the services having not loaded, when Espresso ensures
    // all services and tasks have finished before looking at the views.

}
