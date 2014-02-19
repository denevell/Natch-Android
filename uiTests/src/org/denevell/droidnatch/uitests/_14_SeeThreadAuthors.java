package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;

public class _14_SeeThreadAuthors extends NatchAndroidInstrumentationWithLogin {

    public _14_SeeThreadAuthors() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test() throws Exception {
        new AddThreadPO()
        	.addThreadAndPressBack("Listing threads", "Listing threads");
        new ListThreadsPO()
        	.threadHasAuthor(0, "aaron");
    }

}
