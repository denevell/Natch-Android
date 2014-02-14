package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationTestCase2;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;
import org.denevell.natch.android.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

public class _09_EditPost extends NatchAndroidInstrumentationTestCase2 {

    @SuppressWarnings("deprecation")
    public _09_EditPost() throws Exception {
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

    public void test_1_EditPost() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText("New post in thread"), pressImeActionButton());

        onData(allOf(is(instanceOf(PostResource.class))))
                .atPosition(1)
                .perform(longClick());

        onView(withText("Edit post"))
                .perform(click());

        onView(withId(R.id.edit_post_edittext)).perform(clearText(), typeText("Edited"), pressImeActionButton());

        closeSoftKeyboard(); Thread.sleep(100);

        onView(withText("Edit")).perform(click());

        onData(allOf(is(instanceOf(PostResource.class))))
                .atPosition(1)
                .check(matches(is(withText("Edited"))));
    }

}
