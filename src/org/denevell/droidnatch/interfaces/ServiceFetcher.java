package org.denevell.droidnatch.interfaces;


public interface ServiceFetcher<T> {
    void go();
    void setServiceCallbacks(ServiceCallbacks<T> callbacks);
}
