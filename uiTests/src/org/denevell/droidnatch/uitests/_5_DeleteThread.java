package org.denevell.droidnatch.uitests;

import android.test.ActivityInstrumentationTestCase2;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.denevell.droidnatch.uitests.CustomMatchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

public class _5_DeleteThread extends ActivityInstrumentationTestCase2<MainPageActivity> {

    @SuppressWarnings("deprecation")
    public _5_DeleteThread() {
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

    public void test_5_DeleteThread() throws Exception {
        onView(withId(R.id.editText1))
                .perform(typeText("New thread to delete"), pressImeActionButton());

        onData(allOf(is(instanceOf(ThreadResource.class))))
                .atPosition(0)
                .check(matches(is(withText("New thread to delete"))));

        onData(allOf(is(instanceOf(ThreadResource.class))))
                .atPosition(0)
                .perform(longClick());

        onView(withText("Delete thread"))
                .perform(click());

        onView(withId(R.id.list_threads_listview))
                .check(matches(listViewHasElements(0)));
    }



}
