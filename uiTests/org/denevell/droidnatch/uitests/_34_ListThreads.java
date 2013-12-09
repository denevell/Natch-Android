package org.denevell.droidnatch.uitests;

import java.util.Date;

import org.denevell.droidnatch.uitests.pageobjects.AddThreadPage;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPage;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;


public class _34_ListThreads extends UiAutomatorTestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void test_34_AddThread() throws Exception {
        // Arrange
        AddThreadPage page = new AddThreadPage(getUiDevice());
        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());
        @SuppressWarnings("deprecation")
        String subject = new Date().toGMTString();

        // Act 
        page.setThreadText(subject);
        listThreadsPage.waitForThreadsToLoad();
        
        // Assert
        UiObject firstRow = listThreadsPage.getThreadsRow(0);
        assertEquals("Correct thread input", subject, firstRow.getText());
    }

}
