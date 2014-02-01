package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface GenericUiObservable<T> {

    public static interface GenericUiObserver<T> {
        void onGenericUiEvent(T object);
    }

    public static interface GenericUiSuccess<T> {
        void onGenericUiSuccess(T object);
    }

    public static interface GenericUiFailure {
        void onGenericUiFailure(FailureResult f);
    }

    void setOnSubmitObserver(GenericUiObserver observer);

    void submit(T object);

    void success(T result);

    void fail(FailureResult r);
    
    void setOnSuccess(GenericUiSuccess success);

    void setOnFail(GenericUiFailure failure);
}
