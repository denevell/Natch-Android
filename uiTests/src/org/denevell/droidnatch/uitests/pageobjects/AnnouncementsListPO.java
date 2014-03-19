package org.denevell.droidnatch.uitests.pageobjects;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import org.denevell.droidnatch.uitests.CustomMatchers;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.newfivefour.android.manchester.R;

public class AnnouncementsListPO {

	public AnnouncementsListPO postHasContent(int i, String string) {
        onView(withContentDescription("announcement_title_row"+i))
        	.check(matches(withText(string)));
        return this;
	}

	public AnnouncementsListPO checkHasNumberOfPosts(int numPosts) {
        onView(withId(R.id.announcements_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(numPosts)));
        return this;
	}

	public AnnouncementsListPO pressAnnouncementsTab() {
        onView(withText("Announcements"))
        	.perform(click());
		return this;
	}

	public AnnouncementsListPO seeAnnouncementTitle(int i, String string) {
        onView(withContentDescription("announcement_title_row"+i)).check(matches(withText(string)));
        return this;
	}

	public AnnouncementsListPO pressItem(int i) {
        onView(withContentDescription("announcement_title_row"+i)).perform(click());
        try { Thread.sleep(500); } catch (InterruptedException e) { }
        return this;
	}

	public AnnouncementsListPO checkRowIsMarkedUnread(int row) {
        onView(withContentDescription("announcement_title_row"+row)).check(matches(CustomMatchers.bold()));
		return this;
	}

	public AnnouncementsListPO checkRowIsNotMarkedUnread(int row) {
        onView(withContentDescription("announcement_title_row"+row)).check(matches(not(CustomMatchers.bold())));
		return this;
	}

}
