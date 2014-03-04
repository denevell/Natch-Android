package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

public class _16_Logout extends NatchAndroidInstrumentationWithLogin {

    public _16_Logout() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        Urls.setUsername(null);
        super.setUp();
    }
    
    public void test() throws Exception {
    	new LoginPO().loginWithDefaultCredential(getInstrumentation());
    	Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Login")).check(doesNotExist());
        new LoginPO().logout(getInstrumentation(), "aaron");
    	Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Login")).check(matches(ViewMatchers.isDisplayed()));
    }

}
