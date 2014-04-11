package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.pageobjects.RegisterPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentation;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

public class _15_RequestPasswordReset extends NatchAndroidInstrumentation {

    private LoginPO loginPo;

	public _15_RequestPasswordReset() throws Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        TestUtils.deleteDb();
        getActivity();
        loginPo = new LoginPO();
    }

    public void testCanRequestPassword() throws Exception {
    	// Arrange
    	new RegisterPO().register(getInstrumentation(), "b", "b", "aa@b.com");
		loginPo.logout();
    	
    	// Act
		loginPo
			.requestPasswordReset("aa@b.com")
			.passwordResetSuccessful();
    }

    public void testCantRequestPasswordWithBadEmail() throws Exception {
    	// Arrange
    	new RegisterPO().register(getInstrumentation(), "b", "b", "aa@b.com");
		loginPo.logout();
    	
    	// Act
		loginPo
			.requestPasswordReset("aa@b1.com")
			.passwordResetUnSuccessful();
    }

    public void testCantRequestPasswordWithBlankEmail() throws Exception {
    	// Arrange
    	new RegisterPO().register(getInstrumentation(), "b", "b", "aa@b.com");
		loginPo.logout();
    	
    	// Act
		loginPo
			.requestPasswordReset("")
			.passwordResetNoSuccessful()
			.passwordResetNoError();
		pressBack();
		pressBack();
		loginPo
			.requestPasswordReset("")
			.passwordResetNoSuccessful()
			.passwordResetNoError();
    }

}
