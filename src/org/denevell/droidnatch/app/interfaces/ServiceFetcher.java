package org.denevell.droidnatch.app.interfaces;


public interface ServiceFetcher<T> {
    void go();
    void setServiceCallbacks(ServiceCallbacks<T> callbacks);
}
