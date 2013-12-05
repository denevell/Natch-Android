package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPage;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;


public class _1_ListThreads extends UiAutomatorTestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void testListThreads() throws Exception {
        // Assert
        new ListThreadsPage(getUiDevice()).waitForThreadsToLoad();
    }

}
