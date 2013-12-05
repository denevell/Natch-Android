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
    
    public void waitForThreadsToLoad() throws UiObjectNotFoundException {
        System.out.println("Looking for threads");
        UiObject threadsList = getThreadsList();
        threadsList.waitForExists(30);
        System.out.println("Found thread: " + threadsList.getChildCount());
    }

}
