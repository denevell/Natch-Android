package org.denevell.droidnatch.uitests;

import android.test.ActivityInstrumentationTestCase2;

import org.denevell.droidnatch.MainPageActivity;

public class _6_AddPostToThread extends ActivityInstrumentationTestCase2<MainPageActivity> {

    public _6_AddPostToThread(Class<MainPageActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test_1_GotoNewThreadPage() throws Exception {
//        // Arrange
//        @SuppressWarnings("deprecation")
//        SingleThreadPage singleThreadPage = new SingleThreadPage(getUiDevice());
//        String subject = new Date().toGMTString();
//        addThreadAndPost(getUiDevice(), subject, subject);
//
//        // Assert
//        UiObject firstPostRow = singleThreadPage.getPostRow(1);
//        assertEquals("Correct post input", subject, firstPostRow.getText());
//
//        // Cleanup
//        singleThreadPage.longPressDeleteThreadRow(0);
    }

    public static void addThreadAndPost(String threadTitle, String postTitle) throws Exception {
//        AddThreadPage page = new AddThreadPage(uiDevice);
//        ListThreadsPage listThreadsPage = new ListThreadsPage(uiDevice);
//        SingleThreadPage singleThreadPage = new SingleThreadPage(uiDevice);
//        page.addThread(threadTitle);
//        listThreadsPage.waitForThreadsToLoad();
//        UiObject firstRow = listThreadsPage.getThreadsRow(0);
//
//        // Act
//        firstRow.click();
//        singleThreadPage.waitForPostsToLoad();
//        singleThreadPage.addPost(postTitle);
//        singleThreadPage.waitForPostsToLoad();
    }


}
