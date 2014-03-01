package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

/**
 * Created by user on 11/02/14.
 */
public class AddThreadPO {

    public AddThreadPO addThread(String subject, String content) throws Exception {
        closeSoftKeyboard();
        onView(withText("Add thread")).perform(click());
        onView(withId(R.id.add_thread_subject_edittext))
        	.perform(typeText(subject), 
        			ViewActions.pressImeActionButton(),
        			ViewActions.closeSoftKeyboard());
        Thread.sleep(300);
        onView(withId(R.id.add_thread_content_edittext))
        	.perform(typeText(content), 
        			ViewActions.pressImeActionButton(),
        			ViewActions.closeSoftKeyboard());
        Thread.sleep(300);
        //onView(withId(R.id.add_thread_button)).perform(click());
        onView(withText("Add")).perform(click());
        return this;
    }
    
    public AddThreadPO addThreadAndPressBack(String subject, String content) throws Exception {
    	addThread(subject, content);
    	Espresso.pressBack();
    	return this;
	}

	public AddThreadPO showError() {
        onView(withId(R.id.add_thread_subject_edittext))
        	.check(matches(CustomMatchers.showsErrorString("blank"))); // Therefore fail
        return this;
	}

}
