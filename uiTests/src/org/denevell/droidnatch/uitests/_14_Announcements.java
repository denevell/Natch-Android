package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddPostPO;
import org.denevell.droidnatch.uitests.pageobjects.AnnouncementsListPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

import com.newfivefour.android.manchester.R;

public class _14_Announcements extends NatchAndroidInstrumentationWithLogin {

    public _14_Announcements() throws Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        getActivity();
    }

    public void testSeeAnnouncement() throws Exception {
    	TestUtils.addAnnouncement(getActivity(), "New Announce", "Announce content");
    	TestUtils.addAnnouncement(getActivity(), "Newer Announce", "Newer Announce content");
    	TestUtils.addPostViaRest(getActivity());
    	new ListThreadsPO()
    		.pressRefresh()
    		.checkHasNumberOfThreads(1);
    	new AnnouncementsListPO()
    		.pressAnnouncementsTab()
    		.checkHasNumberOfPosts(2)
    		.seeAnnouncementTitle(1, "New Announce")
    		.seeAnnouncementTitle(0, "Newer Announce");
    	new ListThreadsPO()
    		.pressTab()
    		.pressRefresh()
    		.checkHasNumberOfThreads(1);;
    }

    public void testCanClickOnAnnouncement() throws Exception {
    	TestUtils.addAnnouncement(getActivity(), "New Announce", "Announce content");
    	new ListThreadsPO()
    		.pressRefresh();
    	new AnnouncementsListPO()
    		.pressAnnouncementsTab()
    		.pressItem(0);
    	new ListPostsPO()
    		.checkHasNumberOfPosts(1)
    		.postHasContent(0, "Announce content");
    	new AddPostPO()
    		.addPost("New");
    	new ListPostsPO()
    		.checkHasNumberOfPosts(2)
    		.postHasContent(1, "New");
    	
        onView(withId(R.id.list_posts_listpostsview_holder))
        	.check(matches(CustomMatchers.viewHasActivityTitle("New Announce")));
    }

    public void testGoesBackToSameTab() throws Exception {
    	TestUtils.addAnnouncement(getActivity(), "New Announce", "Announce content");
    	new ListThreadsPO()
    		.pressRefresh();
    	new AnnouncementsListPO()
    		.pressAnnouncementsTab()
    		.pressItem(0);
    	pressBack();
    	new AnnouncementsListPO()
    		.seeAnnouncementTitle(0, "New Announce");
    	new ListThreadsPO()
    		.pressTab()
    		.checkHasNumberOfThreads(0);;
    	new AnnouncementsListPO()
    		.pressAnnouncementsTab()
    		.checkHasNumberOfPosts(1);
    }

    public void testSeeUnreadThreadAsBold() throws Exception {
    	String threadId = TestUtils.addAnnouncement(getActivity(), "A", "nnounce");
        new ListThreadsPO()
        	.pressRefresh();
        new AnnouncementsListPO()
        	.pressAnnouncementsTab()
        	.checkHasNumberOfPosts(1)
        	.checkRowIsMarkedUnread(0)
        	.pressItem(0);
        pressBack();
        new AnnouncementsListPO()
        	.checkRowIsNotMarkedUnread(0);
        TestUtils.addPostViaRest(getActivity(), threadId);
        new ListThreadsPO()
        	.pressRefresh();
        new AnnouncementsListPO()
        	.checkRowIsMarkedUnread(0)
        	.pressItem(0);
        pressBack();
        new AnnouncementsListPO()
        	.checkRowIsNotMarkedUnread(0);
    }

}
