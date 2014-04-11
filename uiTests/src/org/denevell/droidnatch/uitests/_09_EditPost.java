package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddPostPO;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.EditPostPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

public class _09_EditPost extends NatchAndroidInstrumentationWithLogin {

    public _09_EditPost() throws Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        getActivity();
    }

	public void test() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        new AddPostPO().addPost("New post in thread");

        new ListPostsPO()
        	.bringUpEditDeleteOptions(1)
        	.pressEditPostOption();

        new EditPostPO().edit("Edited");

        new ListPostsPO().postHasContent(1, "Edited");
    }

	public void testCannotEditOthersPost() throws Exception {
        new AddThreadPO().addThread("New thread to edit", "New thread to edit");
        
        new AddPostPO().addPost("New post");
        
        pressBack();

        new LoginPO().logout();
		String username = "new"+new Date().getTime();
		new RegisterPO().register(getInstrumentation(), username, username); // Logs us in too
		
		new ListThreadsPO().pressItem(0);

        new ListPostsPO().bringUpEditDeleteOptions(1);

        onView(withId(com.newfivefour.android.manchester.R.id.posts_context_menu_edit_thread)).check(doesNotExist());
    }

	public void testErrorOnBlanks() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        new AddPostPO().addPost("New post in thread");

        new ListPostsPO()
        	.bringUpEditDeleteOptions(1)
        	.pressEditPostOption();

        new EditPostPO()
        	.edit(" ")
        	.showBlankError();
    }

}
