package org.denevell.droidnatch.uitests.utils;

import org.denevell.droidnatch.uitests.overriddenclasses.UiScrollable;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;

public class AppUtils {
    
    public static void openAppNamed(UiDevice uiDevice, String appName) throws Exception {
        uiDevice.pressHome();
        
        UiObject allAppsButton = new UiObject(new UiSelector().description("Apps"));
        allAppsButton.clickAndWaitForNewWindow();
        
        try {
            UiObject appsTab = new UiObject(new UiSelector().text("OK"));
            appsTab.click();
        } catch(Exception e) {
            System.out.println("Couldn't press OK on the coach marks apps menu thing -- doesn't exist?");
        }

        UiObject demoWindow = new UiObject(new UiSelector().text("Apps"));
        appsTab.click();        

        
        UiScrollable appsView = new UiScrollable(new UiSelector().scrollable(true));
        appsView.setMaxSearchSwipes(20);
        appsView.setAsHorizontalList();
        UiObject settingsApp = appsView.getChildByText(
                new UiSelector().className(android.widget.TextView.class.getName()), 
                appName, 
                true);
        settingsApp.clickAndWaitForNewWindow();        
    }

}
