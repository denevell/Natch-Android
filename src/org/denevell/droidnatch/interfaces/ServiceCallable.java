package org.denevell.droidnatch.interfaces;

import org.denevell.droidnatch.baseclasses.FailureResult;



public interface ServiceCallable<T> {
    interface Success<T> {
        public void success(T success);
    }
    interface Failure {
        public void fail(FailureResult f);
    }
    
    void go();
    void setSuccessCallback(Success<T> r);
    void setFailureCallback(Failure r);
}
