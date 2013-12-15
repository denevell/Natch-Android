package org.denevell.droidnatch.app.baseclasses;

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
    private static final String TAG = UiEventThenServiceCallController.class.getSimpleName();
    private GenericUiObservable mUiEvent;
    private ResultsDisplayer mLoadingView;
    private ServiceFetcher mService;
    private Controller mNextController;

    public UiEventThenServiceCallController(
            GenericUiObservable uiEvent, 
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
    public void onGenericUiEvent() {
        if(mService!=null) {
            mService.go();
        }
        if(mLoadingView!=null) {
            mLoadingView.startLoading();
        }
    }

}