package org.denevell.droidnatch.app.interfaces;

import java.util.Map;

public interface ScreenOpener {
    
    void openScreen(Class<?> screenClass, Map<String, String> passedVars);

    void gotoPreviousScreen();

}
