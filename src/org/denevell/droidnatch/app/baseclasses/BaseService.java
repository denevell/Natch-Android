package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
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

public class BaseService<T> implements Listener<JSONObject>, ErrorListener, ServiceFetcher<T> {

    private static final String TAG = BaseService.class.getSimpleName();
    private Context mAppContext;
    protected String mUrl;
    protected ProgressIndicator mProgress;
    protected ServiceCallbacks<T> mCallbacks;
    private FailureResultFactory mFailureResultFactory;
    protected VolleyRequest mVolleyRequest;
    private ResponseConverter mResponseConverter;
    private Class<T> mClass;

    public BaseService(
            Context applicationContext, 
            String url, 
            VolleyRequest volleyRequest, 
            ProgressIndicator progress, 
            ResponseConverter responseConverter,
            FailureResultFactory failureResultFactory, 
            Class<T> classInstance) {
        mAppContext = applicationContext;
        mUrl = url;
        mProgress = progress;
        mFailureResultFactory = failureResultFactory;
        mVolleyRequest = volleyRequest;
        mResponseConverter = responseConverter;
        mClass = classInstance;
        mVolleyRequest.setErrorListener(this);
        mVolleyRequest.setListener(this);
        mVolleyRequest.setUrl(url);
    }

    public void go() {
        Log.v(TAG, "Sending out url: " + mUrl); 
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        queue.add(mVolleyRequest.getRequest());
        mProgress.start();
    }
    
    @Override
    public void onResponse(JSONObject response) {
        if(response!=null) Log.v(TAG, "Response: " + response.toString());
        if(mProgress!=null) {
            mProgress.stop();
        }
        String success = response.toString();
        T res = mResponseConverter.convert(success, mClass);
        if(mCallbacks!=null) {
            mCallbacks.onServiceSuccess(res);
        }
    }
    
    @Override
    public void setServiceCallbacks(ServiceCallbacks<T> callbacks) {
        mCallbacks = callbacks;
    }    
    
    @Override
    public void onErrorResponse(VolleyError error) {
        if(error!=null) Log.e(TAG, "Service error: " + error.toString(), error.getCause());
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
