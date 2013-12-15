package org.denevell.droidnatch.app.baseclasses.controllers;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;

import android.util.Log;

public class ServiceCallThenDisplayController<T, U> implements Controller, 
        ServiceCallbacks<T> {
    
    private static final String TAG = ServiceCallThenDisplayController.class.getSimpleName();
    private ServiceFetcher<T> mService;
    private ResultsDisplayer<U> mResultsDisplayable;
    private TypeAdapter<T, U> mTypeAdapter;
    private Runnable[] mUiElementListeners;

    public ServiceCallThenDisplayController(
            ServiceFetcher<T> listThreadsService, 
            ResultsDisplayer<U>resultsDisplayable,
            TypeAdapter<T, U> typeAdapter, 
            Runnable ...uiElementListeners
            ) {
        mService = listThreadsService;
        mResultsDisplayable = resultsDisplayable;
        mTypeAdapter = typeAdapter;
        mUiElementListeners = uiElementListeners;
    }
    
    @Override
    public ServiceCallThenDisplayController<T, U> setup() {
        Log.v(TAG, "Setting up controller");
        mService.setServiceCallbacks(this);
        if(mUiElementListeners!=null) {
            for (Runnable listener: mUiElementListeners) {
                listener.run();
            }
        }
        return this;
    }
    
    @Override
    public void go() {
        Log.v(TAG, "Starting controller");
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
