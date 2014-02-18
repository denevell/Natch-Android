package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentation;
import org.denevell.natch.android.R;

public class _00_Login extends NatchAndroidInstrumentation {

    public _00_Login() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testSuccess() throws Exception {
        onView(withText("Login")).perform(click());
        onView(withId(R.id.login_username_edittext)).perform(typeText("aaron"), pressImeActionButton());
        onView(withId(R.id.login_password_edittext)).perform(typeText("aaron"), pressImeActionButton());
        closeSoftKeyboard();
        Thread.sleep(100);
        onView(withText("Login")).perform(click());

        onView(withId(R.id.login_username_edittext)).check(doesNotExist()); // We thus know we've logged in okay
    }

    public void testFail() throws Exception {
        onView(withText("Login")).perform(click());
        onView(withId(R.id.login_username_edittext)).perform(typeText("bad"), pressImeActionButton());
        onView(withId(R.id.login_password_edittext)).perform(typeText("bad"), pressImeActionButton());
        closeSoftKeyboard();
        Thread.sleep(100);
        onView(withText("Login")).perform(click());

        onView(withId(R.id.login_username_edittext)).check(matches(isDisplayed())); // Therefore fail
        onView(withId(R.id.login_username_edittext)).check(matches(CustomMatchers.showsErrorString())); // Therefore fail
    }

}
