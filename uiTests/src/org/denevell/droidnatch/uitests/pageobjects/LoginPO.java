package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import com.newfivefour.android.manchester.R;

import android.app.Instrumentation;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

public class LoginPO {

	public LoginPO loginWithDefaultCredential(Instrumentation instr) {
        onView(withText("Login")).perform(click());
		return loginOnDialogueBoxWithCredential(instr, "aaron", "aaron");
	}

	public LoginPO loginWithCredential(Instrumentation instr, String user, String pass) {
        onView(withText("Login")).perform(click());
		return loginOnDialogueBoxWithCredential(instr, user, pass);
	}

	public LoginPO loginOnDialogueBoxWithDefaultCredential(Instrumentation instr) {
		return loginOnDialogueBoxWithCredential(instr, "aaron", "aaron");
	}

	public LoginPO loginOnDialogueBoxWithCredential(Instrumentation instr, String username, String password) {
        onView(withId(R.id.login_username_edittext))
        	.perform(clearText(),
        			typeText(username), 
        			ViewActions.pressImeActionButton());
        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        onView(withId(R.id.login_password_edittext))
        	.perform(clearText(),
        			typeText(password), 
        			ViewActions.pressImeActionButton());
        closeSoftKeyboard();
        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        onView(withText("Login")).perform(click());
        return this;
	}

	public LoginPO shouldseeUsername(Instrumentation instr, String username) {
    	onView(ViewMatchers.withText(username)).check(matches(isDisplayed()));
    	return this;
	}
	
	public LoginPO logout(Instrumentation instrumentation) {
        onView(withId(R.id.threads_option_menu_login)).perform(click());
        onView(withId(R.id.logout_button)).perform(click());
		return this;
	}

	public LoginPO shouldSeeRegisterOption(Instrumentation instr) {
		openActionBarOverflowOrOptionsMenu(instr.getTargetContext());
    	onView(ViewMatchers.withText("Register")).check(matches(ViewMatchers.isDisplayed()));
		return this;
	}

	public LoginPO shouldntSeeRegisterOption(Instrumentation instr) {
		openActionBarOverflowOrOptionsMenu(instr.getTargetContext());
    	onView(ViewMatchers.withText("Register")).check(ViewAssertions.doesNotExist());
    	pressBack();
		return this;
	}

	public void logoutDefaultUser(Instrumentation instrumentation) {
		logout(instrumentation);
	}

}
