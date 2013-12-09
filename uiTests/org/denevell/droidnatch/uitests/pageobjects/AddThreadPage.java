package org.denevell.droidnatch.uitests.pageobjects;

import android.view.KeyEvent;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class AddThreadPage {
    
    private final UiDevice uiDevice;

    public AddThreadPage(UiDevice uiDevice) {
        this.uiDevice = uiDevice;
        uiDevice.waitForIdle();
    }
    
    public UiObject getThreadsEditText() {
       return new UiObject(new UiSelector().description("addthread_edittext"));
    }
    
    public void setThreadText(String text) throws UiObjectNotFoundException {
        UiObject editText = getThreadsEditText();
        editText.waitForExists(3000);
        editText.clearTextField();
        editText.setText(text);
        System.out.println("Pressing enter on add thread text");
        uiDevice.pressKeyCode(KeyEvent.KEYCODE_ENTER);
    }

}
