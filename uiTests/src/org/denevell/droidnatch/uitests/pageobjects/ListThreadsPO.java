package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;

public class ListThreadsPO {

	public ListThreadsPO checkNoThreads() {
    	onView(withText("Login")); // So we wait...
        onView(withId(R.id.list_threads_listview)).check(ViewAssertions.matches(CustomMatchers.listViewHasElements(0)));
        return this;
	}

	public void checkHasNumberOfThreads(int numThreads) {
        onView(withId(R.id.list_threads_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(numThreads)));
	}

	public ListThreadsPO threadHasAuthor(int i, String string) {
        onView(withContentDescription("list_threads_row_author"+i))
        	.check(matches(withText(string)));
        return this;
	}

	public void threadHasContent(int i, String string) {
        onView(withContentDescription("list_threads_row"+i))
        	.check(matches(withText(string)));
	}

}
