package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withParent;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.denevell.droidnatch.uitests.CustomMatchers;
import org.hamcrest.CoreMatchers;

import android.app.Instrumentation;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.newfivefour.android.manchester.R;

public class ListThreadsPO {

	public ListThreadsPO checkNoThreads() {
    	onView(withText("Login")); // So we wait...
        onView(withId(R.id.threads_listview)).check(ViewAssertions.matches(CustomMatchers.listViewHasElements(0)));
        return this;
	}

	public ListThreadsPO checkHasNumberOfThreads(int numThreads) {
        onView(withId(R.id.threads_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(numThreads)));
        return this;
	}

	public ListThreadsPO threadHasAuthor(int i, String string) {
        onView(withContentDescription("list_threads_row_author"+i))
        	.check(matches(withText(string)));
        return this;
	}

	public ListThreadsPO threadHasContent(int i, String string) {
        onView(withContentDescription("list_threads_row"+i)).check(matches(withText(string)));
        return this;
	}

	public ListThreadsPO bringUpEditDeleteOptions(int row) {
        onView(withContentDescription("list_threads_row"+row)).perform(longClick());
		return this;
	}

	public ListThreadsPO pressItem(int row) {
        onView(withContentDescription("list_threads_row"+row)).perform(click());
        try { Thread.sleep(500); } catch (InterruptedException e) { }
		return this;
	}

	public ListThreadsPO pressItemThenBack(int row) {
        onView(withContentDescription("list_threads_row"+row)).perform(click(), pressBack());
		return this;
	}

	@SuppressWarnings("unchecked")
	public void seeEmptyView() {
        onView(allOf(withId(android.R.id.empty), withParent(withId(R.id.threads_list_relativelayout))))
        		.check(ViewAssertions.matches(isDisplayed()));
	}

	@SuppressWarnings("unchecked")
	public void dontSeeEmptyView() {
        onView(allOf(withId(android.R.id.empty), withParent(withId(R.id.threads_list_relativelayout))))
        		.check(matches(CoreMatchers.not(isDisplayed())));
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

	public ListThreadsPO seeDateAndPostsNum(long time, int pageNum) {
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

	@SuppressWarnings("unchecked")
	public ListThreadsPO seeServiceError() {
        onView(allOf(withId(R.id.list_view_service_error), withParent(withId(R.id.threads_list_relativelayout))))
        	.check(matches(isDisplayed()));
        return this;
	}

	public ListThreadsPO pressTab() {
        onView(withText("Chat")).perform(click());
        return this;
	}

	public ListThreadsPO checkRowIsMarkedUnread(int row) {
        onView(withContentDescription("list_threads_row"+row)).check(matches(CustomMatchers.bold()));
		return this;
	}

	public ListThreadsPO checkRowIsNotMarkedUnread(int row) {
        onView(withContentDescription("list_threads_row"+row)).check(matches(not(CustomMatchers.bold())));
		return this;
	}

	public ListThreadsPO bringUpDeleteOptions() {
		Espresso.openContextualActionModeOverflowMenu();
		return this;
	}

	public ListThreadsPO pressDeleteThread() {
        onView(withText("Delete thread")).perform(click());
        return this;
	}
	

}
