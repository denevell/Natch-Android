package org.denevell.droidnatch.uitests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.NatchAndroidInstrumentationWithLogin;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

public class _16_ChangePassword extends NatchAndroidInstrumentationWithLogin {

    private LoginPO loginPo;

	public _16_ChangePassword() throws Exception {
        super("com.newfivefour.android.manchester", MainPageActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyIdlingResource volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);
        getActivity();
        loginPo = new LoginPO();
    }

    public void testChangePassword() throws Exception {
		loginPo
			.changePassword("newpass", "newpass")
			.changePasswordSuccess()
			.logoutFromDialogue()
			.loginWithCredential(getInstrumentation(), "aaron", "newpass")
			.shouldseeUsername(getInstrumentation(), "aaron"); 
    }

    public void testDontChangePassword() throws Exception {
		loginPo
			.changePassword("newpass", "different")
			.changePasswordNotSame();
    }

    public void testBlanksDoNothing() throws Exception {
		loginPo
			.changePassword("", "")
			.changePasswordNoSuccess()
			.changePasswordNoError();
		pressBack();
		loginPo
			.changePassword(" ", " ")
			.changePasswordNoSuccess()
			.changePasswordNoError();
		// Not crashed, yay
    }

}
