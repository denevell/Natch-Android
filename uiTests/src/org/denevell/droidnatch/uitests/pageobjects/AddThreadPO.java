package org.denevell.droidnatch.uitests.pageobjects;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import org.denevell.natch.android.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/**
 * Created by user on 11/02/14.
 */
public class AddThreadPO {

    public void addThread(String subject, String content) throws Exception {
        closeSoftKeyboard();
        onView(withText("Add thread")).perform(click());
        onView(withId(R.id.add_thread_subject_edittext)).perform(typeText(subject), ViewActions.pressImeActionButton());
        onView(withId(R.id.add_thread_content_edittext)).perform(typeText(content), ViewActions.pressImeActionButton());
        closeSoftKeyboard();
        Thread.sleep(1);
        //onView(withId(R.id.add_thread_button)).perform(click());
        onView(withText("Add")).perform(click());
    }

}
