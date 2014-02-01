package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;

public class PreviousScreenUiEvent extends GenericUiObject {
    
    @SuppressWarnings("unused")
    private static final String TAG = PreviousScreenUiEvent.class.getSimpleName();
    private ScreenOpener mScreenOpener;

    public PreviousScreenUiEvent(ScreenOpener screenOpener) {
        mScreenOpener = screenOpener;
        setOnSubmitObserver(new GenericUiObserver() {
            @Override
            public void onGenericUiEvent(Object o) {
                mScreenOpener.gotoPreviousScreen();
            }
        });
    }

}