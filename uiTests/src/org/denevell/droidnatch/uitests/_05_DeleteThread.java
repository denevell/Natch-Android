package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.denevell.droidnatch.uitests.CustomMatchers.listViewHasElements;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationTestCase2;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;
import org.denevell.natch.android.R;

public class _05_DeleteThread extends NatchAndroidInstrumentationTestCase2 {

    public _05_DeleteThread() throws  Exception {
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

    @SuppressWarnings("unchecked")
	public void test_5_DeleteThread() throws Exception {
        new AddThreadPO().addThread("New thread to delete", "New thread to delete");

        pressBack();

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
