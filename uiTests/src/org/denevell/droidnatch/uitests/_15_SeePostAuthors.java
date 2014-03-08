package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.AddPostPO;
import org.denevell.droidnatch.uitests.pageobjects.AddThreadPO;
import org.denevell.droidnatch.uitests.pageobjects.ListPostsPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;

public class _15_SeePostAuthors extends NatchAndroidInstrumentationWithLogin {

    public _15_SeePostAuthors() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test() throws Exception {
        new AddThreadPO()
        	.addThread("Listing threads", "Listing threads");
        new ListPostsPO().postHasAuthor(0, "aaron");
        new AddPostPO().addPost("a`nother");
        new ListPostsPO().postHasAuthor(0, "aaron");
    }

}
