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


public class _7_DeletePost extends NatchUiAutomatorTests {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppUtils.openAppNamed(getUiDevice(), UiConstants.appName);
    }
    
    public void test_1_DeletePost() throws Exception {
        // Arrange
        @SuppressWarnings("deprecation")
        SingleThreadPage singleThreadPage = new SingleThreadPage(getUiDevice());
        String subject = new Date().toGMTString();
        _6_AddPostToThread.addThreadAndPost(getUiDevice(), subject, subject);

        // Act
        singleThreadPage.waitForPostsToLoad();
        UiObject firstPostRow = singleThreadPage.getPostRow(1);
        assertTrue("First post exists", firstPostRow.exists());
        singleThreadPage.longPressDeletePostRow(1);
        singleThreadPage.waitForPostsToLoad();

        // Assert
        firstPostRow = singleThreadPage.getPostRow(1);
        assertTrue("First post doesn't exist", firstPostRow.waitUntilGone(1000));
        UiObject initialPostRow = singleThreadPage.getPostRow(0);
        assertTrue("Initial post row still there", initialPostRow.waitForExists(1000));

        // Cleanup
        singleThreadPage.longPressDeleteThreadRow(0);
    }

}
