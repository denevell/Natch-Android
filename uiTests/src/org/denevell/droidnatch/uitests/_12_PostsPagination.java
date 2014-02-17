package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
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

public class _12_PostsPagination extends NatchAndroidInstrumentationTestCase2 {

    public _12_PostsPagination() throws Exception {
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

	public void test_1_AddPostToThread() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText("One"), pressImeActionButton());
        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText("Two"), pressImeActionButton());
        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText("Three"), pressImeActionButton());
        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText("Four"), pressImeActionButton());
        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText("Five"), pressImeActionButton());

        onView(withId(R.id.list_posts_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(5)));

        onView(ViewMatchers.withText("More"))
        	.perform(ViewActions.click());

        Thread.sleep(1000);

        onView(withId(R.id.list_posts_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(6)));
    }

}
