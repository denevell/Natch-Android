package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddPostPO;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

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
	
    public void testSeeError() throws Exception {
        new AddThreadPO()
        	.addThread("Hiya!", "Hiii");

        new AddPostPO()
        	.addPost(" ")
        	.showBlankError();
    }

}
