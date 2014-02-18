package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;
import org.denevell.natch.android.R;

public class _10_EditThread extends NatchAndroidInstrumentationWithLogin {

    public _10_EditThread() throws Exception {
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
	public void test_1_EditThread() throws Exception {
        new AddThreadPO().addThread("New title thread", "New thread");

        onView(withId(R.id.list_posts_addpost_edittext)).check(matches(CustomMatchers.viewHasActivityTitle("New title thread")));

        onData(allOf(is(instanceOf(PostResource.class)))) .atPosition(0) .perform(longClick());
        onView(withText("Edit thread")).perform(click());

        onView(withId(R.id.edit_thread_subject_edittext)).perform(clearText(), typeText("Edited title"), pressImeActionButton());
        onView(withId(R.id.edit_thread_content_edittext)).perform(clearText(), typeText("Edited post"), pressImeActionButton());
        closeSoftKeyboard(); Thread.sleep(100);
        onView(withText("Edit")).perform(click());

        onData(allOf(is(instanceOf(PostResource.class)))).atPosition(0).check(matches(is(withText("Edited post"))));

        onView(withId(R.id.list_posts_addpost_edittext)).check(matches(CustomMatchers.viewHasActivityTitle("Edited title")));

        pressBack();

        onView(withContentDescription("list_threads_row0")).check(matches(withText("Edited title")));
    }

}
