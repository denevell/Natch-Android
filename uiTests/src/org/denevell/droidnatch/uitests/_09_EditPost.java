package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;
import org.denevell.natch.android.R;

public class _09_EditPost extends NatchAndroidInstrumentationWithLogin {

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

	public void test() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText("New post in thread"), pressImeActionButton());

        new ListPostsPO().bringUpEditDeleteOptions(1);

        onView(withText("Edit post"))
                .perform(click());

        onView(withId(R.id.edit_post_edittext))
        	.perform(clearText(), typeText("Edited"), pressImeActionButton());

        closeSoftKeyboard(); Thread.sleep(100);

        onView(withText("Edit")).perform(click());

        new ListPostsPO().postHasContent(1, "Edited");
    }

	public void testCannotEditOthersPost() throws Exception {
        new AddThreadPO().addThread("New thread to edit", "New thread to edit");
        
        new ListPostsPO().addPost("New post");
        
        pressBack();

		String username = "new"+new Date().getTime();
		new RegisterPO().register(getInstrumentation(), username, username); // Logs us in too
		
		new ListThreadsPO().pressItem(0);

        new ListPostsPO().bringUpEditDeleteOptions(1);

        onView(withText("Edit post")).check(doesNotExist());
    }

}
