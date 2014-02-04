package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface Activator<T> {

    public static interface GenericUiObserver {
        void onUiEventActivated();
    }

    void setOnSubmitObserver(GenericUiObserver observer);

    void success(T result);

    void fail(FailureResult r);

}
