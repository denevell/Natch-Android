package org.denevell.droidnatch.uitests;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.natch.android.R;

import java.util.Date;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

public class _34_AddThread extends ActivityInstrumentationTestCase2<MainPageActivity> {

    @SuppressWarnings("deprecation")
    public _34_AddThread() {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        TestUtils.deleteDb();
        getActivity();
    }

    public void test_34_AddThread() throws Exception {
        String date = new Date().toString();
        onView(withId(R.id.editText1)).perform(ViewActions.typeText("Hiya!"+date), ViewActions.pressImeActionButton());
        onView(withContentDescription("list_threads_row0")).check(matches(withText("Hiya!"+date)));
    }



}
