package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;

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

	@SuppressWarnings("unchecked")
	public ListPostsPO bringUpEditDeleteOptions(int row) {
		onData(allOf(is(instanceOf(PostResource.class)))).atPosition(row).perform(longClick());
		return this;
	}

	public void checkHasNumberOfPosts(int numPosts) {
        onView(withId(R.id.list_posts_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(numPosts)));
	}

}
