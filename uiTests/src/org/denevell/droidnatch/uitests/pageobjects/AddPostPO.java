package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

public class AddPostPO {

	public AddPostPO addPost(String content) throws Exception {
        closeSoftKeyboard();
        onView(withId(R.id.post_add_edittext)).perform(typeText(content), ViewActions.closeSoftKeyboard());
        try { Thread.sleep(300); } catch(Exception e) {}
        onView(withId(R.id.post_add_button)).perform(click());
        return this;
    }
    
	public AddPostPO showBlankError() {
        onView(withId(R.id.post_add_edittext))
        	.check(matches(CustomMatchers.showsErrorString("blank"))); // Therefore fail
        return this;
	}
	
	public AddPostPO showLoginError() {
        onView(withId(R.id.please_login_context_menu)).check(matches(isDisplayed())); // Therefore fail
        return this;
	}

}
