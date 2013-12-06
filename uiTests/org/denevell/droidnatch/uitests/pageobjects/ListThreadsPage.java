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
        uiDevice.waitForIdle();
    }
    
    private UiObject getThreadsList() {
       return new UiObject(new UiSelector().description("listthreads_listview"));
    }

    private UiObject getThreadsRow() {
       return new UiObject(new UiSelector().description("list_threads_row"));
    }

    public UiObject getLoadingView() {
       System.out.println("Looking for loading view");
       return new UiObject(new UiSelector().description("list_threads_loading_view"));
    }
    
    public UiObject waitForThreadsToLoad() throws UiObjectNotFoundException, InterruptedException {
        System.out.println("Looking for threads");
        UiObject threadRow = getThreadsRow();
        threadRow.waitForExists(10000);
        threadsList = getThreadsList();
        System.out.println("Found threads: " + threadsList.getChildCount());
        return threadsList;
    }

}
