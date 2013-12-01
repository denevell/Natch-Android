package org.denevell.droidnatch.interfaces;

import org.denevell.droidnatch.baseclasses.FailureResult;

public interface ServiceCallbacks<T> {
    interface Success<T> {
        public void success(T success);
    }
    interface Failure {
        public void fail(FailureResult f);
    }
    
    void success(T r);
    void fail(FailureResult r);
}
