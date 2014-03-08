package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddPostPO;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.natch.android.R;

import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;

public class _12_PostsPagination extends NatchAndroidInstrumentationWithLogin {

    public _12_PostsPagination() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        PaginationMapper instance = PaginationMapper.getInstance();
		instance.postsPaginationObject().defaultRange=1;
        instance.postsPaginationObject().paginationMaximum=1;
        instance.postsPaginationObject().range=1;
        super.setUp();
    }

	public void test() throws Exception {
        new AddThreadPO().addThread("New thread", "New thread");
        
        onView(withId(R.id.list_posts_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(1)));

        new AddPostPO().addPost("One");
        
        // Should now trigger the pagination to show more

        onView(withId(R.id.list_posts_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(2)));

        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());

        onView(withId(R.id.list_posts_listview))
        	.check(ViewAssertions.matches(CustomMatchers.listViewHasElements(2)));

        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());
    }

}
