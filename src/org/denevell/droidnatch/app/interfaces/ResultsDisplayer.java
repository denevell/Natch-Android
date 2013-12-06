package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface ResultsDisplayer<T> {

    void startLoading();

    void stopLoading();
    
    void onSuccess(T success);
    
    void onFail(FailureResult fail);

}
