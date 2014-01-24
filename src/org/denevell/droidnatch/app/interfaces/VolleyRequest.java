package org.denevell.droidnatch.app.interfaces;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface VolleyRequest<T> {
    
    public void setErrorListener(ErrorListener errorListener);
    public void setListener(Listener<JSONObject> listener);
    public void setUrl(String url);
    public void addHeader(String header, String value);
    
    @SuppressWarnings("rawtypes")
    public Request getRequest();

}
