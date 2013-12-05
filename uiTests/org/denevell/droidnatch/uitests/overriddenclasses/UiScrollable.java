package org.denevell.droidnatch.uitests.overriddenclasses;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class UiScrollable extends com.android.uiautomator.core.UiScrollable {

    public UiScrollable(UiSelector container) {
        super(container);
    }
    
    @Override
    public boolean scrollIntoView(UiSelector selector) throws UiObjectNotFoundException {
        if (exists(getSelector().childSelector(selector))) {
            return (true);
        } else {
            System.out.println("It doesn't exist on this page");
            // we will need to reset the search from the beginning to start search
            scrollToBeginning(getMaxSearchSwipes());
            System.out.println("I appear to have swiped to the beginning.");
            if (exists(getSelector().childSelector(selector))) {
                return (true);
            }
            for (int x = 0; x < getMaxSearchSwipes(); x++) {
                System.out.println("I'm going forward a page: " + x);
                if(!scrollForward() && x!=0) {
                    return false;
                }

                if(exists(getSelector().childSelector(selector))) {
                    return true;
                }
            }
        }
        return false;
    }    
    
}
