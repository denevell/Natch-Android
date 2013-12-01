package org.denevell.droidnatch.interfaces;

import org.denevell.droidnatch.baseclasses.FailureResult;

public interface ResultsDisplayer<T> {
    
    void onSuccess(T success);
    
    void onFail(FailureResult fail);
}
