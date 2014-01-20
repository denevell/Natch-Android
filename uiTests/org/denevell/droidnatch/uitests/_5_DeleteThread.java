package org.denevell.droidnatch.uitests;

import java.util.Date;

import org.denevell.droidnatch.uitests.pageobjects.AddThreadPage;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPage;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;


public class _5_DeleteThread extends UiAutomatorTestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void test_5_DeleteThread() throws Exception {
        // Arrange
        AddThreadPage addThreadPage = new AddThreadPage(getUiDevice());
        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());
        @SuppressWarnings("deprecation")
        String subject = new Date().toGMTString();

        // Act 
        addThreadPage.addThread(subject);
        listThreadsPage.waitForThreadsToLoad();
        UiObject firstRow = listThreadsPage.getThreadsRow(0);
        assertEquals("Correct thread input", subject, firstRow.getText());
        listThreadsPage.longPressDeleteThreadRow(0);
        listThreadsPage.waitForThreadsToLoad();
        
        // Assert
        firstRow = listThreadsPage.getThreadsRow(0);
        assertNotSame("Thread input now deleted", subject, firstRow.getText());
    }

}
