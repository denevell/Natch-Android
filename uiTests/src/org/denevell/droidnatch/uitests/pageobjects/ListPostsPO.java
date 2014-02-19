package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.natch.android.R;

public class ListPostsPO {

	public void postHasContent(int i, String string) {
        onView(withContentDescription("list_posts_row"+i))
        	.check(matches(withText(string)));
	}

	public ListPostsPO postHasAuthor(int i, String string) {
        onView(withContentDescription("list_posts_row_author"+i))
        	.check(matches(withText(string)));
        return this;
	}

	public ListPostsPO addPost(String string) {
        onView(withId(R.id.list_posts_addpost_edittext))
                .perform(typeText(string), pressImeActionButton());
        return this;
	}

}
