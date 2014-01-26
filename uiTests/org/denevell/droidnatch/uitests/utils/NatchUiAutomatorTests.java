package org.denevell.droidnatch.uitests.utils;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;

public class NatchUiAutomatorTests extends UiAutomatorTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Urls.setBasePath("http://192.168.43.103:8080/Natch-REST-ForAutomatedTests/rest/");
    }
	
}
