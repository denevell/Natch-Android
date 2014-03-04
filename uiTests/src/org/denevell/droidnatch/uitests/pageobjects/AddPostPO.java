package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;

public class AddPostPO {

    public AddPostPO addPost(String content) throws Exception {
        closeSoftKeyboard();
        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText(content), pressImeActionButton());
        return this;
    }
    
	public AddPostPO showBlankError() {
        onView(withId(R.id.list_posts_addpost_edittext))
        	.check(matches(CustomMatchers.showsErrorString("blank"))); // Therefore fail
        return this;
	}
	
	public AddPostPO showLoginError() {
        onView(withId(R.id.list_posts_addpost_edittext))
        	.check(matches(CustomMatchers.showsErrorString("login"))); // Therefore fail
        return this;
	}

}
