package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

public class _034_AddThread extends NatchAndroidInstrumentationWithLogin {

    public _034_AddThread() throws Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        getActivity();
    }

    public void testAdd() throws Exception {
        new AddThreadPO().addThread("Hiya!", "Content");
        new ListPostsPO().postHasContent(0, "Content");
        pressBack();
        onView(withContentDescription("list_threads_row0")).check(matches(withText("Hiya!")));
        new ListThreadsPO().threadHasAuthor(0, "aaron");
    }

    public void testSeeError() throws Exception {
        new AddThreadPO()
        	.addThread("Hiya!", "")
        	.showError();
    }

    public void testSeeErrorWhenNotLoggedInAndCanThenAdd() throws Exception {
    	new LoginPO().logout();
        new AddThreadPO()
        	.addThread("a", "b")
        	.showLoginError()
        	.pressLoginAfterTryingToAdd();
    	new LoginPO().loginOnDialogueBoxWithDefaultCredential(getInstrumentation());
        new AddThreadPO().addThreadAndPressBack("Hiya!", "Content");
        new ListThreadsPO().checkHasNumberOfThreads(1);
    }

    public void testSeeErrorWhenNotLoggedInAndCanThenRegisterAndAdd() throws Exception {
    	new LoginPO().logout();
        new AddThreadPO()
        	.addThread("a", "b")
        	.showLoginError()
        	.pressRegisterAfterTryingToAdd();
    	String user = "aaron" + new Date().getTime();
		new RegisterPO().registerFromDialogueBox(getInstrumentation(), user, user, null);
        new AddThreadPO().addThreadAndPressBack("Hiya!", "Content");
        new ListThreadsPO().checkHasNumberOfThreads(1);
    }

}
