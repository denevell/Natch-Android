package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;

public class PreviousScreenReceiver implements Receiver {

    @SuppressWarnings("unused")
    private static final String TAG = PreviousScreenReceiver.class.getSimpleName();
    private ScreenOpener mScreenOpener;

    public PreviousScreenReceiver(ScreenOpener screenOpener) {
        mScreenOpener = screenOpener;
    }

    @Override
    public void success(Object result) {
        mScreenOpener.gotoPreviousScreen();
    }

    @Override
    public void fail(FailureResult r) {

    }

}