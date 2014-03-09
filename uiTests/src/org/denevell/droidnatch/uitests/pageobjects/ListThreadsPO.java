package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.uitests.CustomMatchers;
import org.denevell.natch.android.R;
import org.hamcrest.CoreMatchers;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;

public class ListThreadsPO {

	public ListThreadsPO checkNoThreads() {
    	onView(withText("Login")); // So we wait...
        onView(withId(R.id.threads_listview)).check(ViewAssertions.matches(CustomMatchers.listViewHasElements(0)));
        return this;
	}

	public void checkHasNumberOfThreads(int numThreads) {
        onView(withId(R.id.threads_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(numThreads)));
	}

	public ListThreadsPO threadHasAuthor(int i, String string) {
        onView(withContentDescription("list_threads_row_author"+i))
        	.check(matches(withText(string)));
        return this;
	}

	public ListThreadsPO threadHasContent(int i, String string) {
        onView(withContentDescription("list_threads_row"+i))
        	.check(matches(withText(string)));
        return this;
	}

	@SuppressWarnings("unchecked")
	public ListThreadsPO bringUpEditDeleteOptions(int row) {
		onData(allOf(is(instanceOf(ThreadResource.class)))).atPosition(row).perform(longClick());
		return this;
	}

	@SuppressWarnings("unchecked")
	public ListThreadsPO pressItem(int row) {
		onData(allOf(is(instanceOf(ThreadResource.class)))).atPosition(row).perform(click());
		return this;
	}

	@SuppressWarnings("unchecked")
	public ListThreadsPO pressItemThenBack(int row) {
		onData(allOf(is(instanceOf(ThreadResource.class)))).atPosition(row).perform(
				click(),
				pressBack());
		return this;
	}

	public void seeEmptyView() {
        onView(withId(android.R.id.empty)).check(ViewAssertions.matches(isDisplayed()));
	}

	public void dontSeeEmptyView() {
        onView(withId(android.R.id.empty)).check(matches(CoreMatchers.not(isDisplayed())));
	}

	public ListThreadsPO pressRefresh() {
        onView(withId(R.id.threads_option_menu_refresh)).perform(click());
		return this;
	}

	public ListThreadsPO pressLoginLink() {
        onView(withText("Please login")).perform(click());
        return this;
	}

	public ListThreadsPO shouldntSeeLoginLink() {
        onView(withText("Please login")).check(ViewAssertions.doesNotExist());
        return this;
	}

	public ListThreadsPO seeAuthorAndPostsNum(String string, long time, int pageNum) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int dom = c.get(Calendar.DAY_OF_MONTH);
        String month = new SimpleDateFormat("MM", Locale.UK).format(c.getTime());
        String year = new SimpleDateFormat("yy", Locale.UK).format(c.getTime());
        String dateString = dom + "/" + month + "/" + year;		
        onView(withContentDescription("list_threads_row_date0")).check(ViewAssertions.matches(
        		CustomMatchers.withRegexText(".*"+dateString+".*| Posts: "+pageNum+".*")));
		return this;
	}
	

}
