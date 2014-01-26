package org.denevell.droidnatch.uitests;

import java.util.Date;

import org.denevell.droidnatch.uitests.pageobjects.AddThreadPage;
import org.denevell.droidnatch.uitests.pageobjects.ListThreadsPage;
import org.denevell.droidnatch.uitests.pageobjects.SingleThreadPage;
import org.denevell.droidnatch.uitests.utils.AppUtils;
import org.denevell.droidnatch.uitests.utils.UiConstants;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import org.denevell.droidnatch.uitests.utils.NatchUiAutomatorTests;


public class _6_AddPostToThread extends NatchUiAutomatorTests {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void test_1_GotoNewThreadPage() throws Exception {
        // Arrange
        AddThreadPage page = new AddThreadPage(getUiDevice());
        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());
        SingleThreadPage singleThreadPage = new SingleThreadPage(getUiDevice());
        @SuppressWarnings("deprecation")
        String subject = new Date().toGMTString();
        page.addThread(subject);
        listThreadsPage.waitForThreadsToLoad();
        UiObject firstRow = listThreadsPage.getThreadsRow(0);
        
        // Act
        firstRow.click();
        singleThreadPage.waitForPostsToLoad();
        singleThreadPage.addPost(subject);
        singleThreadPage.waitForPostsToLoad();
        
        // Assert
        UiObject firstPostRow = singleThreadPage.getPostRow(1);
        assertEquals("Correct post input", subject, firstPostRow.getText());
        
        // Cleanup
        singleThreadPage.longPressDeleteThreadRow(0);
    }


}
