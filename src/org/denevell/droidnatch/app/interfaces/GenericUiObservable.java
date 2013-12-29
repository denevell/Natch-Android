package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface GenericUiObservable {

    public static interface GenericUiObserver {
        void onGenericUiEvent();
    }

    public static interface GenericUiSuccess {
        void onGenericUiSuccess();
    }

    public static interface GenericUiFailure {
        void onGenericUiFailure(FailureResult f);
    }

    void setOnSubmitObserver(GenericUiObserver observer);

    void submit();

    void success();

    void fail(FailureResult r);
    
    void setOnSuccess(GenericUiSuccess success);

    void setOnFail(GenericUiFailure failure);
}
