package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

public class _034_AddThread extends NatchAndroidInstrumentationWithLogin {

    public _034_AddThread() throws Exception {
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

    public void testSeeErrorWhenNotLoggedIn() throws Exception {
    	new LoginPO().logout(getInstrumentation(), "aaron");
        new AddThreadPO().showLoginError();
    }




}
