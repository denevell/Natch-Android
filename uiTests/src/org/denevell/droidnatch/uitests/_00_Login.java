package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentation;
import org.denevell.natch.android.R;

public class _00_Login extends NatchAndroidInstrumentation {

    public _00_Login() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        Urls.setUsername(null);
        super.setUp();
    }
    
    public void testSuccess() throws Exception {
    	new LoginPO()
    		.loginWithDefaultCredential(getInstrumentation());
        onView(withId(R.id.login_username_edittext)).check(doesNotExist()); // We thus know we've logged in okay
    }

    public void testFail() throws Exception {
        Urls.setUsername(null);
    	new LoginPO()
    		.loginWithCredential(getInstrumentation(), "bad", "bad");

        onView(withId(R.id.login_username_edittext)).check(matches(isDisplayed())); // Therefore fail
        onView(withId(R.id.login_username_edittext)).check(matches(CustomMatchers.showsErrorString())); // Therefore fail
    }

    public void testBlanksFail() throws Exception {
        Urls.setUsername(null);
    	new LoginPO()
    		.loginWithCredential(getInstrumentation(), " ", " ");

        onView(withId(R.id.login_username_edittext)).check(matches(isDisplayed())); // Therefore fail
        onView(withId(R.id.login_username_edittext)).check(matches(CustomMatchers.showsErrorString())); // Therefore fail
    }

}
