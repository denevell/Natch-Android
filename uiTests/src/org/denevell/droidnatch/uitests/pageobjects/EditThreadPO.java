package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.uitests.CustomMatchers;
import com.newfivefour.android.manchester.R;

import com.google.android.apps.common.testing.ui.espresso.Espresso;

public class EditThreadPO {

    public EditThreadPO edit(String subject, String content) throws Exception {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_thread_subject_edittext)).perform(
        		clearText(), 
        		typeText(subject),
        		closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.edit_thread_content_edittext)).perform(
        		clearText(), 
        		typeText(content),
        		closeSoftKeyboard());
        Thread.sleep(100);
        onView(withText("Edit")).perform(click());
        return this;
    }
    
	public EditThreadPO showBlankError() {
        onView(withId(R.id.edit_thread_subject_edittext))
        	.check(matches(CustomMatchers.showsErrorString("blank"))); // Therefore fail
        return this;
	}

}
