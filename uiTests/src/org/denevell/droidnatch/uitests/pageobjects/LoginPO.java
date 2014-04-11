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
import static org.hamcrest.CoreMatchers.not;

import org.denevell.droidnatch.uitests.CustomMatchers;

import android.app.Instrumentation;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.newfivefour.android.manchester.R;

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
	
	public LoginPO logout() {
        onView(withId(R.id.threads_option_menu_login)).perform(click());
        logoutFromDialogue();
		return this;
	}

	public LoginPO logoutFromDialogue() {
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
		logout();
	}

	public LoginPO requestPasswordReset(String email) {
        onView(withText("Login")).perform(click());
        onView(withText("Forgotten password?")).perform(click());
        onView(withId(R.id.password_reset_recovery_email_editext)).perform(typeText(email));
        onView(withId(R.id.password_reset_button)).perform(click());
		return this;
	}

	public LoginPO passwordResetSuccessful() {
		onView(withId(R.id.password_reset_success_textview))
			.check(ViewAssertions.matches(withText(R.string.password_reset_request_successful)));
		return this;
	}

	public LoginPO passwordResetNoSuccessful() {
		onView(withId(R.id.password_reset_success_textview))
			.check(ViewAssertions.matches(not(withText(R.string.password_reset_request_successful))));
		return this;
	}

	public void passwordResetNoError() {
		onView(withId(R.id.password_reset_recovery_email_editext))
			.check(ViewAssertions.matches(not(CustomMatchers.showsErrorString())));
	}

	public void passwordResetUnSuccessful() {
		onView(withId(R.id.password_reset_recovery_email_editext))
			.check(ViewAssertions.matches(CustomMatchers.showsErrorString("Password reset request failed")));
	}

	public LoginPO changePassword(String s1, String s2) {
        onView(withId(R.id.threads_option_menu_login)).perform(click());
        onView(withId(R.id.change_password_change_password_edittext))
        	.perform(clearText(),
        			typeText(s1));
        onView(withId(R.id.change_password_confirm_edittext))
        	.perform(clearText(),
        			typeText(s2));
        onView(withId(R.id.change_password_button))
        	.perform(click());
		return this;
	}

	public LoginPO changePasswordSuccess() {
        onView(withId(R.id.change_password_success_textview)).check(matches(withText(R.string.change_password_success)));
		return this;
	}

	public LoginPO changePasswordNoSuccess() {
        onView(withId(R.id.change_password_success_textview)).check(matches(not(withText(R.string.change_password_success))));
		return this;
	}

	public void changePasswordNotSame() {
        onView(withId(R.id.change_password_change_password_edittext))
        	.check(matches(CustomMatchers.showsErrorString("Passwords not the same")));
	}

	public void changePasswordGeneralError() {
        onView(withId(R.id.change_password_change_password_edittext))
        	.check(matches(CustomMatchers.showsErrorString("Error")));
	}

	public LoginPO changePasswordNoError() {
        onView(withId(R.id.change_password_change_password_edittext))
        	.check(matches(not(CustomMatchers.showsErrorString())));
		return this;
	}

}
