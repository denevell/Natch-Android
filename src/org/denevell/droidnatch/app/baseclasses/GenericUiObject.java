package org.denevell.droidnatch.app.baseclasses;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.app.interfaces.GenericUiObservable;

public class GenericUiObject implements GenericUiObservable {

    public GenericUiObject() {
    }

    private List<GenericUiObserver> observers = new ArrayList<GenericUiObserver>();
    private List<GenericUiSuccess> successObservers = new ArrayList<GenericUiSuccess>();
    private List<GenericUiFailure> failureObservers = new ArrayList<GenericUiFailure>();

    @Override
    public void setObserver(GenericUiObserver observer) {
        observers.add(observer);
    }

    @Override
    public void submit() {
        for (GenericUiObserver observer: observers) {
           observer.onGenericUiEvent(); 
        }
    }

    @Override
    public void setOnSuccess(GenericUiSuccess success) {
        successObservers.add(success);
    }

    @Override
    public void setOnFail(GenericUiFailure failure) {
        failureObservers.add(failure);
    }

    @Override
    public void success() {
        for (GenericUiSuccess observer: successObservers) {
           observer.onGenericUiSuccess();
        }
    }

    @Override
    public void fail(FailureResult f) {
        for (GenericUiFailure observer: failureObservers) {
           observer.onGenericUiFailure(f);
        }
    }
}