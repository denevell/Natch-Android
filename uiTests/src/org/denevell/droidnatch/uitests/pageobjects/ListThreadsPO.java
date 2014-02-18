package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;

public class ListThreadsPO {

	public void checkNoThreads() {
    	onView(withText("Login")); // So we wait...
        onView(withId(R.id.list_threads_listview)).check(ViewAssertions.matches(CustomMatchers.listViewHasElements(0)));
	}

	public void checkHasNumberOfThreads(int numThreads) {
        onView(withId(R.id.list_threads_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(numThreads)));
	}

}
