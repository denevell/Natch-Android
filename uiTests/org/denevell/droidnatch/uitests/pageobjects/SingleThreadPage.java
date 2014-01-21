package org.denevell.droidnatch.uitests.pageobjects;

import android.view.KeyEvent;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class SingleThreadPage {
    
    private final UiDevice uiDevice;

    public SingleThreadPage(UiDevice uiDevice) {
        this.uiDevice = uiDevice;
        uiDevice.waitForIdle();
    }
    
    public UiObject getLoadingView() {
        System.out.println("Looking for loading view");
        return new UiObject(new UiSelector().description("list_posts_loading_view"));
     }
    
    private UiObject getPostsList() {
        return new UiObject(new UiSelector().description("list_posts_listview"));
     }    
    
    public UiObject getPostRow(int pos) {
        return new UiObject(new UiSelector().description("list_posts_row"+String.valueOf(pos)));
     }    
    
    public UiObject waitForPostsToLoad() throws UiObjectNotFoundException, InterruptedException {
        System.out.println("Looking for posts");
        UiObject threadRow = getPostRow(0);
        threadRow.waitForExists(10000);
        UiObject postsList = getPostsList();
        System.out.println("Found posts: " + postsList.getChildCount());
        getLoadingView().waitUntilGone(10000);
        return postsList;
    }    
    
    public UiObject getThreadsEditText() {
        return new UiObject(new UiSelector().description("add_post_edittext"));
     }
     
     public void addPost(String text) throws UiObjectNotFoundException {
         UiObject editText = getThreadsEditText();
         editText.waitForExists(3000);
         editText.clearTextField();
         editText.setText(text);
         System.out.println("Pressing enter on add post text");
         uiDevice.pressKeyCode(KeyEvent.KEYCODE_ENTER);
     }    
     
     public UiObject longPressDeleteThreadRow(int pos) throws UiObjectNotFoundException {
         UiObject row = getPostRow(pos);
         System.out.println("Long pressing on list item");
         uiDevice.swipe(row.getBounds().centerX(), row.getBounds().centerY(), 
                 row.getBounds().centerX(), row.getBounds().centerY(), 400);
         System.out.println("Searching for delete item");
         UiSelector deleteSelector = new UiSelector().text("Delete thread");
         UiObject deleteObject = new UiObject(deleteSelector);
         System.out.println("Clicking on delete item");
         deleteObject.click();
         return null;
     }     

}
