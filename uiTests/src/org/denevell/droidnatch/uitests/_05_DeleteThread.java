package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;

public class _05_DeleteThread extends NatchAndroidInstrumentationWithLogin {

    public _05_DeleteThread() throws  Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

	public void testCanDelete() throws Exception {
        new AddThreadPO().addThreadAndPressBack("New thread to delete", "New thread to delete");

        new ListThreadsPO().threadHasContent(0, "New thread to delete");

        new ListThreadsPO().bringUpEditDeleteOptions(0);

        onView(withText("Delete thread")).perform(click());

        new ListThreadsPO().checkNoThreads();
    }

	public void testCannotDeleteOthersPost() throws Exception {
        new AddThreadPO().addThreadAndPressBack("New thread to delete", "New thread to delete");

        new ListThreadsPO().threadHasContent(0, "New thread to delete");

		new LoginPO().logout(getInstrumentation(), "aaron");
        long timeString = new Date().getTime();
		String username = "new"+timeString;
		new RegisterPO().register(getInstrumentation(), username, username); // Logs us in too

        new ListThreadsPO().bringUpEditDeleteOptions(0);

        onView(withText("Delete thread")).check(doesNotExist());
    }

}
