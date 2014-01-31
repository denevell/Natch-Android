package org.denevell.droidnatch.uitests;

import android.test.ActivityInstrumentationTestCase2;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

public class _6_AddPostToThread extends ActivityInstrumentationTestCase2<MainPageActivity> {

    @SuppressWarnings("deprecation")
    public _6_AddPostToThread() {
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
        onView(withId(R.id.editText1))
                .perform(typeText("New thread to open"), pressImeActionButton());

        onData(allOf(is(instanceOf(ThreadResource.class))))
                .atPosition(0)
                .check(matches(is(withText("New thread to open"))));

        onData(allOf(is(instanceOf(ThreadResource.class))))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.editText1))
                .perform(typeText("New post in thread"), pressImeActionButton());

        onData(allOf(is(instanceOf(PostResource.class))))
                .atPosition(1)
                .check(matches(is(withText("New post in thread"))));
    }

}
