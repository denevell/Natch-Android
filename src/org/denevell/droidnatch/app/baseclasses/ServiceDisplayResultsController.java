package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;

public class ServiceDisplayResultsController<T, U> implements Controller, 
        ServiceCallbacks<T> {
    
    private ServiceFetcher<T> mService;
    private ResultsDisplayer<U> mResultsDisplayable;
    private TypeAdapter<T, U> mTypeAdapter;

    public ServiceDisplayResultsController(
            ServiceFetcher<T> listThreadsService, 
            ResultsDisplayer<U>resultsDisplayable,
            TypeAdapter<T, U> typeAdapter
            ) {
        mService = listThreadsService;
        mResultsDisplayable = resultsDisplayable;
        mTypeAdapter = typeAdapter;
    }
    
    public void go() {
        mService.setServiceCallbacks(this);
        mService.go();
        if(mResultsDisplayable!=null) {
            mResultsDisplayable.startLoading();
        }
    }

    @Override
    public void onServiceSuccess(T r) {
        U u = mTypeAdapter.convert(r);
        if(mResultsDisplayable!=null) {
            mResultsDisplayable.onSuccess(u);
            mResultsDisplayable.stopLoading();
        }
    }

    @Override
    public void onServiceFail(FailureResult r) {
        if(mResultsDisplayable!=null) {
            mResultsDisplayable.onFail(r);
            mResultsDisplayable.stopLoading();
        }
    }
      
}
