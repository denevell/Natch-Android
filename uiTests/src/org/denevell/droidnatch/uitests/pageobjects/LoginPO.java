package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.natch.android.R;

import android.app.Instrumentation;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

public class LoginPO {

	public LoginPO loginWithDefaultCredential(Instrumentation instr) {
		return loginWithCredential(instr, "aaron", "aaron");
	}

	public LoginPO loginWithCredential(Instrumentation instr, String username, String password) {
		openActionBarOverflowOrOptionsMenu(instr.getTargetContext());
        onView(withText("Login")).perform(click());
        onView(withId(R.id.login_username_edittext))
        	.perform(clearText(),
        			typeText(username), 
        			ViewActions.pressImeActionButton());
        onView(withId(R.id.login_password_edittext))
        	.perform(clearText(),
        			typeText(password), 
        			ViewActions.pressImeActionButton());
        closeSoftKeyboard();
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        onView(withText("Login")).perform(click());
        return this;
	}

	public void shouldseeUsername(Instrumentation instr, String username) {
		openActionBarOverflowOrOptionsMenu(instr.getTargetContext());
    	onView(ViewMatchers.withText(username)).check(matches(isDisplayed()));
	}

}
