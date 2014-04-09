package org.denevell.droidnatch.app.interfaces;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface VolleyRequest<I, T> {
    
    public void setErrorListener(ErrorListener errorListener);
    public void setListener(@SuppressWarnings("rawtypes") Listener listener);
    public I getBody();
    public void setUrl(String url);
    public void addHeader(String header, String value);
    
    @SuppressWarnings("rawtypes")
    public Request getRequest();

}
