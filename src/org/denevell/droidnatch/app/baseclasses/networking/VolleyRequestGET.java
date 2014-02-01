package org.denevell.droidnatch.app.baseclasses.networking;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.json.JSONObject;

public class VolleyRequestGET<T> implements VolleyRequest<T>{
    
    private String mUrl;
    private ErrorListener errorListener;
    private Listener<JSONObject> listener;

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
    
    @Override
    public void setListener(Listener<JSONObject> listener) {
        this.listener = listener;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Request getRequest() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, mUrl, null,
                listener, errorListener) {
        };
       return jsObjRequest; 
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public void addHeader(String header, String value) {

    }

}
