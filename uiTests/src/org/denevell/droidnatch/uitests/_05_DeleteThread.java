package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;

import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.newfivefour.android.manchester.R;

public class _05_DeleteThread extends NatchAndroidInstrumentationWithLogin {

    public _05_DeleteThread() throws  Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

	public void testCanDelete() throws Exception {
        new AddThreadPO().addThreadAndPressBack("New thread to delete", "New thread to delete");

        new ListThreadsPO().threadHasContent(0, "New thread to delete");

        new ListThreadsPO()
        	.bringUpEditDeleteOptions(0)
        	.bringUpDeleteOptions()
        	.pressDeleteThread();

        new ListThreadsPO().checkNoThreads();
    }

	public void testShowLoginMessageWhenNotLoggedIn() throws Exception {
        new AddThreadPO().addThreadAndPressBack("New thread to delete", "New thread to delete");

        new LoginPO().logoutDefaultUser(getInstrumentation());

        new ListThreadsPO()
        	.bringUpEditDeleteOptions(0)
        	.pressLoginLink();

        new LoginPO().loginOnDialogueBoxWithDefaultCredential(getInstrumentation());

        new ListThreadsPO()
        	.bringUpEditDeleteOptions(0)
        	.shouldntSeeLoginLink();
    }

	public void testCannotDeleteOthersPost() throws Exception {
        new AddThreadPO().addThreadAndPressBack("New thread to delete", "New thread to delete");

        new ListThreadsPO().threadHasContent(0, "New thread to delete");

		new LoginPO().logout();
        long timeString = new Date().getTime();
		String username = "new"+timeString;
		new RegisterPO().register(getInstrumentation(), username, username); // Logs us in too

        new ListThreadsPO().bringUpEditDeleteOptions(0);

        onView(withText(R.string.delete_thread_not_yours_to_edit)).check(matches(ViewMatchers.isDisplayed()));
    }

}
