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
public class UiEventThenServiceCallController implements Controller, 
        ServiceCallbacks, 
        GenericUiObserver {

    public static interface NextControllerHaltable {
        boolean shouldHalt();
    }
    
    public static class NextControllerNeverHalter implements NextControllerHaltable {
        @Override
        public boolean shouldHalt() {
            return false;
        }
    }

    private static final String TAG = UiEventThenServiceCallController.class.getSimpleName();
    private GenericUiObservable mUiEvent;
    private ResultsDisplayer mLoadingView;
    private ServiceFetcher mService;
    private Controller mNextController;
    private NextControllerHaltable mNextControllerHalter;

    public UiEventThenServiceCallController(
            GenericUiObservable uiEvent, 
            ServiceFetcher service,
            ResultsDisplayer loadingView, 
            NextControllerHaltable nextControllerHalter,
            Controller nextController) {
        mUiEvent = uiEvent;
        mLoadingView = loadingView;
        mService = service;
        mNextControllerHalter = nextControllerHalter;
        mNextController = nextController;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public UiEventThenServiceCallController setup() {
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
        boolean shouldHalt = false;
        if(mNextControllerHalter!=null) {
            shouldHalt = mNextControllerHalter.shouldHalt();
        }
        if(!shouldHalt && mNextController!=null) {
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
    public void onGenericUiEvent() {
        if(mService!=null) {
            mService.go();
        }
        if(mLoadingView!=null) {
            mLoadingView.startLoading();
        }
    }

}