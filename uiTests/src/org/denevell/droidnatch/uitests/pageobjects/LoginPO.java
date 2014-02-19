package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

public class LoginPO {

	public void loginWithDefaultCredential() {
        onView(withText("Login")).perform(click());
        onView(withId(R.id.login_username_edittext))
        	.perform(clearText(),
        			typeText("aaron"), 
        			ViewActions.pressImeActionButton());
        onView(withId(R.id.login_password_edittext))
        	.perform(clearText(),
        			typeText("aaron"), 
        			ViewActions.pressImeActionButton());
        closeSoftKeyboard();
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        onView(withText("Login")).perform(click());
	}

}
