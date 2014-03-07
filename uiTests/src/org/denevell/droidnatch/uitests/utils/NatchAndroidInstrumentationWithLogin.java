package org.denevell.droidnatch.uitests.utils;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;

public class NatchAndroidInstrumentationWithLogin extends NatchAndroidInstrumentation {

	public NatchAndroidInstrumentationWithLogin(String pkg, Class<MainPageActivity> activityClass) throws Exception {
        super(pkg, activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        new LoginPO().loginWithDefaultCredential(getInstrumentation());
    }
}
