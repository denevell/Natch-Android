package org.denevell.droidnatch.app.baseclasses.controllers;

import android.util.Log;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ActivatingUiObject;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

@SuppressWarnings("rawtypes")
public class UiEventThenServiceThenUiEvent<T> implements Controller,
        ServiceCallbacks<T>,
        ActivatingUiObject.GenericUiObserver {

    private static final String TAG = UiEventThenServiceThenUiEvent.class.getSimpleName();
    private ActivatingUiObject<T> mUiEvent;
    private ResultsDisplayer mLoadingView;
    private ServiceFetcher mService;
    private ReceivingUiObject mNextUiEvent;

    public UiEventThenServiceThenUiEvent(
            ActivatingUiObject<T> activatingUiEvent,
            ServiceFetcher service,
            ResultsDisplayer loadingView, 
            ReceivingUiObject<T> uiEventForAfterService) {
        mUiEvent = activatingUiEvent;
        mLoadingView = loadingView;
        mService = service;
        mNextUiEvent = uiEventForAfterService;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public UiEventThenServiceThenUiEvent setup() {
        if(mUiEvent!=null) {
            mUiEvent.setOnSubmitObserver(this);
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
    public void onServiceSuccess(T r) {
        if(mLoadingView!=null) {
            mLoadingView.stopLoading();
        }
        if(mUiEvent!=null) {
            mUiEvent.success(r);
        }
        if(mNextUiEvent!=null) {
            Log.v(TAG, "Calling next ui event");
            mNextUiEvent.success(r);
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
    public void onUiEventActivated() {
        if(mService!=null) {
            mService.go();
        }
        if(mLoadingView!=null) {
            mLoadingView.startLoading();
        }
    }
}