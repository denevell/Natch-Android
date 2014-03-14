package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentation;

import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

public class _13_Logout extends NatchAndroidInstrumentation {

    public _13_Logout() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test() throws Exception {
    	new LoginPO().loginWithDefaultCredential(getInstrumentation());
        onView(withText("Login")).check(doesNotExist());
        new LoginPO().logout(getInstrumentation());
        onView(withText("Login")).check(matches(ViewMatchers.isDisplayed()));
    }

}
