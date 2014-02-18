package org.denevell.droidnatch.app.baseclasses.controllers;

import android.util.Log;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

@SuppressWarnings("rawtypes")
public class UiEventThenServiceThenUiEvent<T> implements Controller,
        ServiceCallbacks<T>,
        Activator.GenericUiObserver {

    private static final String TAG = UiEventThenServiceThenUiEvent.class.getSimpleName();
    private Receiver<T>[] mNextUiEvents;
    private Activator<T> mUiEvent;
    private ProgressIndicator mLoadingView;
    private ServiceFetcher mService;

    public UiEventThenServiceThenUiEvent(
            Activator<T> activatingUiEvent,
            ServiceFetcher service,
            ProgressIndicator loadingView,
            Receiver<T> ...uiEventForAfterService) {
        mUiEvent = activatingUiEvent;
        mLoadingView = loadingView;
        mService = service;
        mNextUiEvents = uiEventForAfterService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UiEventThenServiceThenUiEvent setup() {
        if(mUiEvent!=null) {
            mUiEvent.setOnSubmitObserver(this);
        } else {
            onUiEventActivated();
        }
        if(mService!=null) {
            mService.setServiceCallbacks(this);
        }
        return this;
    }

    @Override
    public void go() {
        onUiEventActivated();
    }

    @Override
    public void onServiceSuccess(T r) {
        if(mLoadingView!=null) {
            mLoadingView.stop();
        }
        if(mUiEvent!=null) {
            mUiEvent.success(r);
        }
        if(mNextUiEvents!=null) {
            for (Receiver<T> event : mNextUiEvents) {
                if(r!=null && event!=null) {
                    Log.v(TAG, "Calling next ui event");
                    event.success(r);
                }
            }
        }
    }

    @Override
    public void onServiceFail(FailureResult r) {
        if(mUiEvent!=null) {
            mUiEvent.fail(r);
        }
        if(mLoadingView!=null) {
            mLoadingView.stop();
        }
    }

    @Override
    public void onUiEventActivated() {
        if(mService!=null) {
            mService.go();
        }
        if(mLoadingView!=null) {
            mLoadingView.start();
        }
    }

}