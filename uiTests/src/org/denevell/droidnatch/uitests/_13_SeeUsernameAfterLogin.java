package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentation;

public class _13_SeeUsernameAfterLogin extends NatchAndroidInstrumentation {

    public _13_SeeUsernameAfterLogin() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
    	Urls.setUsername("");
        super.setUp();
    }
    
    public void test() throws Exception {
    	new LoginPO()
    	.loginWithDefaultCredential(getInstrumentation())
    	.shouldseeUsername(getInstrumentation(), "aaron");
    }

}
