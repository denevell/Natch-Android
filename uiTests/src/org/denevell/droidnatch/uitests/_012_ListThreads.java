package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;

public class _012_ListThreads extends NatchAndroidInstrumentationWithLogin {

    public _012_ListThreads() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test() throws Exception {
        new ListThreadsPO().checkNoThreads();
        new AddThreadPO().addThread("Listing threads", "Listing threads");
        pressBack();
        new ListThreadsPO().checkHasNumberOfThreads(1);
    }

    public void testSeeConnectionErrorListView() throws Exception {
        new ListThreadsPO().checkNoThreads();
        new AddThreadPO().addThread("Listing threads", "Listing threads");
        String oldPath = Urls.getBasePath();
        Urls.setBasePath("http://www.dsflkjsdflkjsdflkjsdlfkjsd.int/");
        pressBack();
        onView(withId(R.id.list_view_service_error_textview)).check(matches(isDisplayed()));
        Urls.setBasePath(oldPath);
        new ListThreadsPO()
        	.pressItemThenBack(0)
        	.checkHasNumberOfThreads(1);
    }

}
