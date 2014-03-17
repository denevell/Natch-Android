package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;

public class _012_ListThreads extends NatchAndroidInstrumentationWithLogin {

    public _012_ListThreads() throws Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test() throws Exception {
        new ListThreadsPO().checkNoThreads();
        new AddThreadPO().addThreadAndPressBack("Listing threads", "Listing threads");
        new ListThreadsPO().checkHasNumberOfThreads(1);
    }

    public void testSeeConnectionErrorListView() throws Exception {
        new ListThreadsPO().checkNoThreads();
        new AddThreadPO().addThread("Listing threads", "Listing threads");
        String oldPath = ShamefulStatics.getBasePath();
        ShamefulStatics.setBasePath("http://www.dsflkjsdflkjsdflkjsdlfkjsd.int/");
        pressBack();
        new ListThreadsPO()
        	.seeServiceError()
        	.pressRefresh(); // To ensure we don't crash...
        ShamefulStatics.setBasePath(oldPath);
        // So to make the views, and therefore url, refresh
        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());
        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());
        new AddThreadPO().addThreadAndPressBack("New", "New");
        new ListThreadsPO().checkHasNumberOfThreads(2);
    }

    public void testCanSeeEmpty() throws Exception {
        new ListThreadsPO().seeEmptyView();
        new AddThreadPO().addThreadAndPressBack("Listing threads", "Listing threads");
        new ListThreadsPO().dontSeeEmptyView();
    }

	public void testCanSeeDate() throws Exception {
    	long time = new Date().getTime();
        new AddThreadPO().addThreadAndPressBack("Listing threads", "Listing threads");
        new ListThreadsPO().seeDateAndPostsNum(time, 1);
    }

    public void testRefreshButton() throws Exception {
        new AddThreadPO().addThreadAndPressBack("Listing threads", "Listing threads");
        TestUtils.addPostViaRest(getInstrumentation().getTargetContext());
        new ListThreadsPO()
        	.pressRefresh()
        	.checkHasNumberOfThreads(2);
    }

    public void testCanLongClickOnFirstRowDoesntCrashThings() throws Exception {
        new AddThreadPO().addThreadAndPressBack("Listing threads", "Listing threads");
        onView(withContentDescription("list_threads_row0")).perform(longClick());
        new ListThreadsPO()
        	.checkHasNumberOfThreads(1);
    }

}
