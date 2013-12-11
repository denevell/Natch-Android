package org.denevell.droidnatch.uitests.pageobjects;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class ListThreadsPage {
    
    @SuppressWarnings("unused")
    private final UiDevice uiDevice;
    private UiObject threadsList;

    public ListThreadsPage(UiDevice uiDevice) {
        this.uiDevice = uiDevice;
    }
    
    private UiObject getThreadsList() {
       return new UiObject(new UiSelector().description("listthreads_listview"));
    }

    public UiObject getThreadsRow(int pos) {
       return new UiObject(new UiSelector().description("list_threads_row"+String.valueOf(pos)));
    }

    public UiObject longPressThreadRow(int pos) throws UiObjectNotFoundException {
        UiObject row = getThreadsRow(pos);
        System.out.println("Long pressing on list item");
        uiDevice.swipe(row.getBounds().centerX(), row.getBounds().centerY(), 
                row.getBounds().centerX(), row.getBounds().centerY(), 400);
        System.out.println("Searching for delete item");
        UiSelector deleteSelector = new UiSelector().text("Delete");
        UiObject deleteObject = new UiObject(deleteSelector);
        System.out.println("Clicking on delete item");
        deleteObject.click();
        System.out.println("Waiting for loading view");
        getLoadingView().waitForExists(1000);
        return null;
    }

    public UiObject getLoadingView() {
       System.out.println("Looking for loading view");
       return new UiObject(new UiSelector().description("list_threads_loading_view"));
    }
    
    public UiObject waitForThreadsToLoad() throws UiObjectNotFoundException, InterruptedException {
        System.out.println("Looking for threads");
        UiObject threadRow = getThreadsRow(0);
        threadRow.waitForExists(10000);
        threadsList = getThreadsList();
        System.out.println("Found threads: " + threadsList.getChildCount());
        getLoadingView().waitUntilGone(2000);
        return threadsList;
    }

}
