package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.uitests.pageobjects.AddPostPO;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;

public class _06_AddPostToThread extends NatchAndroidInstrumentationWithLogin {

    public _06_AddPostToThread() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        TestUtils.deleteDb();
        getActivity();
    }

	public void test() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        new AddPostPO()
        	.addPost("New post in thread");

        new ListPostsPO()
        	.postHasContent(1, "New post in thread");
    }

	public void testScrollToPostOnAdd() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        new AddPostPO()
        	.addPost("First post in thread")
        	.addPost("New post in thread")
        	.addPost("New post in thread")
        	.addPost("New post in thread")
        	.addPost("New post in thread")
        	.addPost("New post in thread")
        	.addPost("Hidden, I hope");

        pressBack();
        new ListThreadsPO().pressItem(0);
        
        onView(withText("First post in thread")).check(matches(isDisplayed()));
        onView(withText("Hidden, I hope")).check(ViewAssertions.doesNotExist());

        new AddPostPO().addPost("But should see this.");
        onView(withText("But should see this.")).check(matches(isDisplayed()));
    }

	public void testRefreshButton() throws Exception {
		String threadId = TestUtils.addPostViaRest(getInstrumentation().getTargetContext());
		
        new ListThreadsPO()
        	.pressRefresh()
        	.pressItem(0);

        new ListPostsPO().checkHasNumberOfPosts(1);

		TestUtils.addPostViaRest(getInstrumentation().getTargetContext(), threadId);

        onView(withId(R.id.posts_option_menu_refresh)).perform(click());

        new ListPostsPO().checkHasNumberOfPosts(2);
    }
	
    public void testSeeError() throws Exception {
        new AddThreadPO()
        	.addThread("Hiya!", "Hiii");

        new AddPostPO()
        	.addPost(" ")
        	.showBlankError();
    }

    public void testSeeConnectionErrorListView() throws Exception {
        new AddThreadPO().addThreadAndPressBack("New thread", "New thread");

        String oldPath = ShamefulStatics.getBasePath();
        ShamefulStatics.setBasePath("http://www.dsflkjsdflkjsdflkjsdlfkjsd.int/");
        
        new ListThreadsPO().pressItem(0);
        onView(withId(R.id.list_view_service_error_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.posts_option_menu_refresh)).perform(click()); // To test we don't crash
        ShamefulStatics.setBasePath(oldPath);
        pressBack();
        new ListThreadsPO().pressItem(0);
        new ListPostsPO().postHasContent(0, "New thread");
    }
    
    public void testAddSeeErrorWhenNotLoggedIn() throws Exception {
        new AddThreadPO()
        	.addThreadAndPressBack("Hiya!", "Hiii");
        
        new LoginPO().logout(getInstrumentation(), "aaron");
        
        new ListThreadsPO().pressItem(0);

        new AddPostPO()
        	.addPost("Should have logged in")
        	.showLoginError();
    }    
    
    public void testLinksClickable() throws Exception {
    	// Can't test atm - no way to click on link.
    }    
    
    public void testBackIconWorks() throws Exception {
        new AddThreadPO().addThread("Hiya!", "Hiii");
        
        new ListPostsPO()
        	.checkHasNumberOfPosts(1)
        	.pressBackIcon();
        
        new ListThreadsPO().checkHasNumberOfThreads(1);
    }    

	public void shouldSeeDateOnPost() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");

        new ListPostsPO()
        	.seeDate(new Date().getTime());
    }
}
