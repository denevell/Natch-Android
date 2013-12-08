package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public abstract class BaseService<T> implements Listener<JSONObject>, ErrorListener, ServiceFetcher<T> {

    private static final String TAG = BaseService.class.getSimpleName();
    private Context mAppContext;
    protected String mUrl;
    protected ProgressIndicator mProgress;
    protected ServiceCallbacks<T> mCallbacks;
    private FailureResultFactory mFailureResultFactory;
    protected VolleyRequest mVolleyRequest;

    public BaseService(
            Context applicationContext, 
            String url, 
            ProgressIndicator progress, 
            FailureResultFactory failureResultFactory, 
            VolleyRequest volleyRequest) {
        mAppContext = applicationContext;
        mUrl = url;
        mProgress = progress;
        mFailureResultFactory = failureResultFactory;
        mVolleyRequest = volleyRequest;
        mVolleyRequest.setErrorListener(this);
        mVolleyRequest.setListener(this);
        mVolleyRequest.setUrl(url);
    }

    public void go() {
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        queue.add(mVolleyRequest.getRequest());
        mProgress.start();
    }
    
    @Override
    public void onResponse(JSONObject response) {
        if(response!=null) Log.v(TAG, response.toString());
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
        if(error!=null) Log.e(TAG, error.toString(), error.getCause());
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
