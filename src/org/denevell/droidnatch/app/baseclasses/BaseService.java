package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public abstract class BaseService<T> implements Listener<JSONObject>, ErrorListener, ServiceFetcher<T> {

    private Context mAppContext;
    private String mUrl;
    protected ProgressIndicator mProgress;
    protected ServiceCallbacks<T> mCallbacks;
    private FailureResultFactory mFailureResultFactory;

    public BaseService(Context applicationContext, String url, ProgressIndicator progress, FailureResultFactory failureResultFactory) {
        mAppContext = applicationContext;
        mUrl = url;
        mProgress = progress;
        mFailureResultFactory = failureResultFactory;
    }

    public void go() {
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, mUrl, null,
                this, this) {
        };
        queue.add(jsObjRequest);
        mProgress.start();
    }
    
    @Override
    public void onResponse(JSONObject response) {
        if(mProgress!=null) {
            mProgress.stop();
        }
    }
    
    @Override
    public void setServiceCallbacks(ServiceCallbacks<T> callbacks) {
        mCallbacks = callbacks;
    }    
    
    @Override
    public void onErrorResponse(VolleyError error) {
        if(mProgress!=null) {
            mProgress.stop();
        }        
        NetworkResponse networkResponse = error.networkResponse;
        int status = -1;
        if(networkResponse!=null) {
            status = networkResponse.statusCode;
        } 
        FailureResult f = mFailureResultFactory.newInstance(status, error.toString(), "");
        if(mCallbacks!=null) {
            mCallbacks.onServiceFail(f);
        }
    }
    

}
