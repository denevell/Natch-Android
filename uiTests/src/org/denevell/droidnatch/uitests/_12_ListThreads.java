package org.denevell.droidnatch.uitests;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationTestCase2;
import org.denevell.natch.android.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.denevell.droidnatch.uitests.CustomMatchers.listViewHasElements;

public class _12_ListThreads extends NatchAndroidInstrumentationTestCase2 {

    @SuppressWarnings("deprecation")
    public _12_ListThreads() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }
    
    public void test_1_ListThreads() throws Exception {
        onView(withId(R.id.list_threads_listview))
                .check(matches(listViewHasElements(0)));

        onView(withId(R.id.editText1))
                .perform(ViewActions.typeText("Listing threads"), ViewActions.pressImeActionButton());

        Espresso.pressBack();

        onView(withId(R.id.list_threads_listview))
                .check(matches(isDisplayed()));

        onView(withId(R.id.list_threads_listview))
                .check(matches(listViewHasElements()));
    }

    // I deleted test two, since it relies on the services having not loaded, when Espresso ensures
    // all services and tasks have finished before looking at the views.

}
