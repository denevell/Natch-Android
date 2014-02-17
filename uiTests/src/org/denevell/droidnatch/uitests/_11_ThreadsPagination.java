package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationTestCase2;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

public class _11_ThreadsPagination extends NatchAndroidInstrumentationTestCase2 {

    public _11_ThreadsPagination() throws Exception {
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

    public void test_showThreadPagination() throws Exception {
        new AddThreadPO().addThreadAndPressBack("One", "One");

        onView(withId(R.id.list_threads_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(1)));

        new AddThreadPO().addThreadAndPressBack("Two", "One");
        new AddThreadPO().addThreadAndPressBack("Three", "One");
        new AddThreadPO().addThreadAndPressBack("Four", "One");
        new AddThreadPO().addThreadAndPressBack("Five", "One");
        new AddThreadPO().addThreadAndPressBack("Six", "One");

        onView(withId(R.id.list_threads_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(5)));

        onView(ViewMatchers.withText("More"))
        	.perform(ViewActions.click());

        onView(withId(R.id.list_threads_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(6)));
    }



}
