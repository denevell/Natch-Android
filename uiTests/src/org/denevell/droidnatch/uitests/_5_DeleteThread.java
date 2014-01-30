package org.denevell.droidnatch.uitests;

import android.test.ActivityInstrumentationTestCase2;

import org.denevell.droidnatch.MainPageActivity;

public class _5_DeleteThread extends ActivityInstrumentationTestCase2<MainPageActivity> {

    public _5_DeleteThread(Class<MainPageActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void test_5_DeleteThread() throws Exception {
//        // Arrange
//        AddThreadPage addThreadPage = new AddThreadPage(getUiDevice());
//        ListThreadsPage listThreadsPage = new ListThreadsPage(getUiDevice());
//        @SuppressWarnings("deprecation")
//        String subject = new Date().toGMTString();
//
//        // Act
//        addThreadPage.addThread(subject);
//        listThreadsPage.waitForThreadsToLoad();
//        UiObject firstRow = listThreadsPage.getThreadsRow(0);
//        assertEquals("Correct thread input", subject, firstRow.getText());
//        listThreadsPage.longPressDeleteThreadRow(0);
//        listThreadsPage.waitForThreadsToLoad();
//
//        // Assert
//        firstRow = listThreadsPage.getThreadsRow(0);
//        assertNotSame("Thread input now deleted", subject, firstRow.getText());
    }

}
