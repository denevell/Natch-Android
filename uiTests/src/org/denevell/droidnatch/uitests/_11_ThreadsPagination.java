package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.PaginationMapper.ListThreadsPaginationObject;
import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.TestUtils;

public class _11_ThreadsPagination extends NatchAndroidInstrumentationWithLogin {

    public _11_ThreadsPagination() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
    	ListThreadsPaginationObject instance = PaginationMapper.getInstance().threadsPaginationObject();
		instance.defaultRange=1;
        instance.paginationMaximum=1;
        instance.range=1;
        super.setUp();
    }

    public void test() throws Exception {
        new AddThreadPO().addThreadAndPressBack("One", "One");

        new ListThreadsPO().checkHasNumberOfThreads(1);

        new AddThreadPO().addThreadAndPressBack("Two", "One");

        new ListThreadsPO().checkHasNumberOfThreads(2);
        
        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());

        new ListThreadsPO().checkHasNumberOfThreads(2);

        TestUtils.toggleOrientationChange(getActivity(), getInstrumentation());
    }



}
