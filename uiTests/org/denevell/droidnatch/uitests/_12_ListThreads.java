package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPage;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;
import org.denevell.droidnatch.uitests.utils.NatchUiAutomatorTests;


import com.android.uiautomator.core.UiObject;


public class _12_ListThreads extends NatchUiAutomatorTests {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void test_1_ListThreads() throws Exception {
        // Arrange
        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());

        // Act 
        UiObject threads = listThreadsPage.waitForThreadsToLoad();
        
        // Assert
        int children = threads.getChildCount();
        assertTrue("List thraeds view should have > 0 children", children>0);
    }

    public void test_2_LoadingViewOnListThreads() throws Exception {
        // Arrange
        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());
        UiObject loadingView = listThreadsPage.getLoadingView();

        // Act 
        UiObject threads = listThreadsPage.waitForThreadsToLoad();
        loadingView = listThreadsPage.getLoadingView();
        boolean exists = loadingView.waitForExists(10000);
        System.out.println("Checking for disappeared loading view");
        assertFalse("Loading view should be hidden", exists);
        
        // Assert
        int children = threads.getChildCount();
        assertTrue("List thraeds view should have > 0 children", children>0);
        assertFalse("Loading view should be hidden", loadingView.exists());
    }

}
