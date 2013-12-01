package org.denevell.droidnatch.interfaces;

import org.denevell.droidnatch.baseclasses.FailureResult;

public interface ResultsDisplayable<T> {
    
    void onSuccess(T success);
    
    void onFail(FailureResult fail);
}
