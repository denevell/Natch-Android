package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPage;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;


public class _1_ListThreads extends UiAutomatorTestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void testListThreads() throws Exception {
        // Act 
        UiObject threads = new ListThreadsPage(getUiDevice()).waitForThreadsToLoad();
        
        // Assert
        int children = threads.getChildCount();
        assertTrue("List thraeds view should have > 0 children", children>0);
    }

}
