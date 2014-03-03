package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.EditThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
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
	public void test() throws Exception {
        new AddThreadPO().addThread("New title thread", "New thread");

        onView(withId(R.id.list_posts_addpost_edittext)).check(matches(CustomMatchers.viewHasActivityTitle("New title thread")));

        onData(allOf(is(instanceOf(PostResource.class)))) .atPosition(0) .perform(longClick());

        onView(withText("Edit thread")).perform(click());
        
        new EditThreadPO().edit("Edited title", "Edited post");

        new ListPostsPO().postHasContent(0, "Edited post");
        onView(withId(R.id.list_posts_addpost_edittext)).check(matches(CustomMatchers.viewHasActivityTitle("Edited title")));

        // So we can be sure it's the same after rotation
        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());

        new ListPostsPO().postHasContent(0, "Edited post");
        onView(withId(R.id.list_posts_addpost_edittext)).check(matches(CustomMatchers.viewHasActivityTitle("Edited title")));

        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());

        pressBack();

        onView(withContentDescription("list_threads_row0")).check(matches(withText("Edited title")));
    }

	public void testCannotEditOthersThread() throws Exception {
        new AddThreadPO().addThread("New thread to edit", "New thread to edit");
        
        new ListPostsPO().addPost("New post");
        
        pressBack();

		String username = "new"+new Date().getTime();
		new LoginPO().logout(getInstrumentation(), "aaron");
		new RegisterPO().register(getInstrumentation(), username, username); // Logs us in too
		
		new ListThreadsPO().pressItem(0);

        new ListPostsPO().bringUpEditDeleteOptions(0);

        onView(withText("Edit thread")).check(doesNotExist());
    }

	@SuppressWarnings("unchecked")
	public void testShowErrorOnBlanks() throws Exception {
        new AddThreadPO().addThread("New title thread", "New thread");

        onView(withId(R.id.list_posts_addpost_edittext)).check(matches(CustomMatchers.viewHasActivityTitle("New title thread")));

        onData(allOf(is(instanceOf(PostResource.class)))) .atPosition(0) .perform(longClick());

        onView(withText("Edit thread")).perform(click());
        
        new EditThreadPO()
        	.edit("Edited title", " ")
        	.showBlankError();
    }	

}
