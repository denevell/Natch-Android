package org.denevell.droidnatch.app.baseclasses.controllers;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable.GenericUiObserver;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

import android.util.Log;

@SuppressWarnings("rawtypes")
public class UiEventThenServiceThenUiEvent implements Controller, 
        ServiceCallbacks, 
        GenericUiObserver {

    private static final String TAG = UiEventThenServiceThenUiEvent.class.getSimpleName();
    private GenericUiObservable mUiEvent;
    private ResultsDisplayer mLoadingView;
    private ServiceFetcher mService;
    private GenericUiObservable mNextUiEvent;

    public UiEventThenServiceThenUiEvent(
            GenericUiObservable uiEvent, 
            ServiceFetcher service,
            ResultsDisplayer loadingView, 
            GenericUiObservable uiEventForAfterService) {
        mUiEvent = uiEvent;
        mLoadingView = loadingView;
        mService = service;
        mNextUiEvent = uiEventForAfterService;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public UiEventThenServiceThenUiEvent setup() {
        if(mUiEvent!=null) {
            mUiEvent.setObserver(this);
        }
        if(mService!=null) {
            mService.setServiceCallbacks(this);
        }
        return this;
    }

    @Override
    public void go() {
    }

    @Override
    public void onServiceSuccess(Object r) {
        if(mLoadingView!=null) {
            mLoadingView.stopLoading();
        }
        if(mUiEvent!=null) {
            mUiEvent.success();
        }
        if(mNextUiEvent!=null) {
            Log.v(TAG, "Calling next ui event");
            mNextUiEvent.submit();
        }
    }

    @Override
    public void onServiceFail(FailureResult r) {
        if(mUiEvent!=null) {
            mUiEvent.fail(r);
        }
        if(mLoadingView!=null) {
            mLoadingView.stopLoading();
        }
    }

    @Override
    public void onGenericUiEvent() {
        if(mService!=null) {
            mService.go();
        }
        if(mLoadingView!=null) {
            mLoadingView.startLoading();
        }
    }

}