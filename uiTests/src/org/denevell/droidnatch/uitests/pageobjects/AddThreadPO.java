package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;
import org.hamcrest.CoreMatchers;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

/**
 * Created by user on 11/02/14.
 */
public class AddThreadPO {

    @SuppressWarnings("unchecked")
	public AddThreadPO addThread(String subject, String content) throws Exception {
        closeSoftKeyboard();
        onView(CoreMatchers.allOf(withId(R.id.add_thread_subject_edittext), isDisplayed()))
        	.perform(typeText(subject), 
        			ViewActions.pressImeActionButton(),
        			ViewActions.closeSoftKeyboard());
        Thread.sleep(300);
        onView(CoreMatchers.allOf(withId(R.id.add_thread_content_edittext), isDisplayed()))
        	.perform(typeText(content), 
        			ViewActions.pressImeActionButton(),
        			ViewActions.closeSoftKeyboard());
        Thread.sleep(300);
        //onView(withId(R.id.add_thread_button)).perform(click());
        onView(CoreMatchers.allOf(withText("Add"), isDisplayed())).perform(click());
        return this;
    }
    
    public AddThreadPO addThreadAndPressBack(String subject, String content) throws Exception {
    	addThread(subject, content);
    	Espresso.pressBack();
    	return this;
	}

	@SuppressWarnings("unchecked")
	public AddThreadPO showError() {
        onView(CoreMatchers.allOf(withId(R.id.add_thread_subject_edittext), isDisplayed()))
        	.check(matches(CustomMatchers.showsErrorString("blank"))); // Therefore fail
        return this;
	}

	public AddThreadPO showLoginError() {
        onView(withId(R.id.please_login_context_menu)).check(matches(isDisplayed())); // Therefore fail
        return this;
	}

	public AddThreadPO pressLoginAfterTryingToAdd() {
        onView(withId(R.id.please_login_context_menu)).perform(click());
        return this;
	}

	public AddThreadPO pressRegisterAfterTryingToAdd() {
        onView(withId(R.id.please_register_context_menu)).perform(click());
        return this;
	}

}
