package org.denevell.droidnatch.uitests;

import java.util.Date;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentation;

public class _000_Register extends NatchAndroidInstrumentation {

    public _000_Register() throws Exception {
        super("org.denevell.natch.android", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSuccess() throws Exception {
    	String username = "aaron"+new Date().getTime();
		new RegisterPO()
    		.register(getInstrumentation(), username, username)
    		.registerSuccess();
		// Often fails since there's a gap between register and login
		// where Espresso thinks Volley has stopped, but it hasn't. 
		// Run it through the debugger and see if it still fails.
		new LoginPO()
			.shouldseeUsername(getInstrumentation(), username)
			.shouldntSeeRegisterOption(getInstrumentation())
			.logout(getInstrumentation())
			.shouldSeeRegisterOption(getInstrumentation());
    }
    
    public void testFail() throws Exception {
    	new RegisterPO()
    		.register(getInstrumentation(), "aaron", "aaron") // Should be in the db from the base class
    		.showRegisterError()
    		.showRegisterDuplication();
    }


}
