package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface ServiceCallbacks<T> {
    interface Success<T> {
        public void success(T success);
    }
    interface Failure {
        public void fail(FailureResult f);
    }
    
    void onServiceSuccess(T r);
    void onServiceFail(FailureResult r);
}
