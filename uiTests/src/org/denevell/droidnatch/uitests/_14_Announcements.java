package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AnnouncementsListPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

public class _14_Announcements extends NatchAndroidInstrumentationWithLogin {

    public _14_Announcements() throws Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        TestUtils.deleteDb();
        getActivity();
    }

    public void testSeeAnnouncement() throws Exception {
    	TestUtils.addAnnouncement(getActivity(), "New Announce", "Announce content");
    	TestUtils.addAnnouncement(getActivity(), "Newer Announce", "Newer Announce content");
    	new ListThreadsPO()
    		.pressRefresh()
    		.checkHasNumberOfThreads(0);
    	new AnnouncementsListPO()
    		.pressAnnouncementsTab()
    		.checkHasNumberOfPosts(2)
    		.seeAnnouncementTitle(1, "New Announce")
    		.seeAnnouncementTitle(0, "Newer Announce");
    	new ListThreadsPO()
    		.pressTab()
    		.pressRefresh()
    		.checkHasNumberOfThreads(0);;
    }

}
