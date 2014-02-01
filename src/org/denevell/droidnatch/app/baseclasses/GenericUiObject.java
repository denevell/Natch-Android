package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.GenericUiObservable;

import java.util.ArrayList;
import java.util.List;

public class GenericUiObject<T> implements GenericUiObservable<T> {

    public GenericUiObject() {
    }

    private List<GenericUiObserver> observers = new ArrayList<GenericUiObserver>();
    private List<GenericUiSuccess> successObservers = new ArrayList<GenericUiSuccess>();
    private List<GenericUiFailure> failureObservers = new ArrayList<GenericUiFailure>();

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        observers.add(observer);
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
    public void submit(T object) {
        for (GenericUiObserver observer: observers) {
           observer.onGenericUiEvent(); 
        }
    }

    @Override
    public void success(T object) {
        for (GenericUiSuccess observer: successObservers) {
           observer.onGenericUiSuccess(object);
        }
    }

    @Override
    public void fail(FailureResult f) {
        for (GenericUiFailure observer: failureObservers) {
           observer.onGenericUiFailure(f);
        }
    }
}