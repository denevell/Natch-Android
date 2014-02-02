package org.denevell.droidnatch.app.baseclasses.controllers;

import android.util.Log;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ActivatingUiObject;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

@SuppressWarnings("rawtypes")
public class UiEventThenServiceCallController<T> implements Controller,
        ServiceCallbacks, 
        ActivatingUiObject.GenericUiObserver {

    private static final String TAG = UiEventThenServiceCallController.class.getSimpleName();
    private ActivatingUiObject mUiEvent;
    private ResultsDisplayer mLoadingView;
    private ServiceFetcher mService;
    private Controller mNextController;

    public UiEventThenServiceCallController(
            ActivatingUiObject uiEvent,
            ServiceFetcher service,
            ResultsDisplayer loadingView, 
            Controller nextController) {
        mUiEvent = uiEvent;
        mLoadingView = loadingView;
        mService = service;
        mNextController = nextController;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public UiEventThenServiceCallController setup() {
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
    public void onServiceSuccess(Object r) {
        if(mLoadingView!=null) {
            mLoadingView.stopLoading();
        }
        if(mUiEvent!=null) {
            mUiEvent.success(r);
        }
        if(mNextController!=null) {
            Log.v(TAG, "Calling next controller");
            mNextController.go();
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