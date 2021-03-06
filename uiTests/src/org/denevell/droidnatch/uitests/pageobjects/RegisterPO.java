package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.uitests.CustomMatchers;

import com.newfivefour.android.manchester.R;

import android.app.Instrumentation;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

public class RegisterPO {

	public RegisterPO register(Instrumentation instr, String username, String password) {
		return register(instr, username, password, null);
	}

	public RegisterPO register(Instrumentation instr, String username, String password, String email) {
		openActionBarOverflowOrOptionsMenu(instr.getTargetContext());
        try { Thread.sleep(300); } catch (InterruptedException e1) { e1.printStackTrace(); }
        onView(withText("Register")).perform(click());
        try { Thread.sleep(300); } catch (InterruptedException e1) { e1.printStackTrace(); }
		registerFromDialogueBox(instr, username, password, email);
		return this;
	}

	public RegisterPO registerFromDialogueBox(Instrumentation instr, String username, String password, String email) {
        onView(withId(R.id.register_username_edittext))
        	.perform(clearText(),
        			typeText(username), 
        			ViewActions.pressImeActionButton());
        try { Thread.sleep(300); } catch (InterruptedException e1) { e1.printStackTrace(); }
        onView(withId(R.id.register_password_edittext))
        	.perform(clearText(),
        			typeText(password), 
        			pressImeActionButton(),
        			ViewActions.closeSoftKeyboard());
        try { Thread.sleep(300); } catch (InterruptedException e1) { e1.printStackTrace(); }
        if(email!=null) {
                onView(withId(R.id.register_optional_recovery_edittext))
                        .perform(clearText(),
                                        typeText(email), 
                                        pressImeActionButton(),
                                        ViewActions.closeSoftKeyboard());
                try { Thread.sleep(300); } catch (InterruptedException e1) { e1.printStackTrace(); }
        }
        onView(withText("Register")).perform(click());
        return this;
	}

	public RegisterPO showRegisterError() {
        onView(withId(R.id.register_username_edittext))
        	.check(matches(CustomMatchers.showsErrorString())); // Therefore fail
        return this;
	}

	public RegisterPO showRegisterDuplication() {
        onView(withId(R.id.register_username_edittext))
        	.check(matches(CustomMatchers.showsErrorString("Username already exists"))); // Therefore fail
        return this;
	}

	public RegisterPO registerSuccess() {
        onView(withId(R.id.register_username_edittext))
        	.check(doesNotExist()); // We thus know we've logged in okay
        return this;
	}


}
