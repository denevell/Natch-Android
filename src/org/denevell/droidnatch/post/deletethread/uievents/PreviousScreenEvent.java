package org.denevell.droidnatch.post.deletethread.uievents;

import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable.GenericUiObserver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;

public class PreviousScreenEvent {
    
    @SuppressWarnings("unused")
    private static final String TAG = PreviousScreenEvent.class.getSimpleName();
    private GenericUiObject mGenericObject = new GenericUiObject();
    private ScreenOpener mScreenOpener;

    public PreviousScreenEvent(ScreenOpener screenOpener) {
        mScreenOpener = screenOpener;
        mGenericObject.setOnSubmitObserver(new GenericUiObserver() {
            @Override
            public void onGenericUiEvent() {
                mScreenOpener.gotoPreviousScreen();
            }
        });
    }

    public GenericUiObservable getUiEvent() {
        return mGenericObject;
    }

}