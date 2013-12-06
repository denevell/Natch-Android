package org.denevell.droidnatch.uitests;

import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPage;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;


public class _12_ListThreads extends UiAutomatorTestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void test_1_ListThreads() throws Exception {
        // Arrange
        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());
        getUiDevice().waitForIdle();

        // Act 
        UiObject threads = listThreadsPage.waitForThreadsToLoad();
        
        // Assert
        int children = threads.getChildCount();
        assertTrue("List thraeds view should have > 0 children", children>0);
    }

    public void test_2_LoadingViewOnListThreads() throws Exception {
        // Arrange
        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());
        getUiDevice().waitForIdle();
        UiObject loadingView = listThreadsPage.getLoadingView();
        loadingView.waitForExists(10000);

        // Act 
        System.out.println("Checking for loading view");
        assertTrue("Loading view should show", loadingView.exists());
        UiObject threads = listThreadsPage.waitForThreadsToLoad();
        
        // Assert
        int children = threads.getChildCount();
        assertTrue("List thraeds view should have > 0 children", children>0);
        assertFalse("Loading view should be hidden", loadingView.exists());
    }

}
