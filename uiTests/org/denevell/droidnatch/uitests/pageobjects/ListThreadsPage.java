package org.denevell.droidnatch.uitests.pageobjects;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class ListThreadsPage {
    
    @SuppressWarnings("unused")
    private final UiDevice uiDevice;

    public ListThreadsPage(UiDevice uiDevice) {
        this.uiDevice = uiDevice;
    }
    
    private UiObject getThreadsList() {
       return new UiObject(new UiSelector().description("listthreads_listview"));
    }

    public UiObject getLoadingView() {
       System.out.println("Looking for loading view");
       return new UiObject(new UiSelector().description("listthreads_loading_view"));
    }
    
    public UiObject waitForThreadsToLoad() throws UiObjectNotFoundException, InterruptedException {
        System.out.println("Looking for threads");
        UiObject threadsList = getThreadsList();
        threadsList.waitForExists(10000);
        Thread.sleep(2000);
        System.out.println("Found threads: " + threadsList.getChildCount());
        return threadsList;
    }

}
