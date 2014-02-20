package org.denevell.droidnatch.app.interfaces;


public interface ServiceFetcher<I, T> {
    void go();
    void setServiceCallbacks(ServiceCallbacks<T> callbacks);
    VolleyRequest<I, T> getRequest();
}
