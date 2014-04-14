package org.denevell.droidnatch.app.baseclasses.networking;

import org.denevell.droidnatch.Application;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class JsonServiceFetcher<I, T> implements Listener<JSONObject>, ErrorListener, ServiceFetcher<I, T> {

    private static final String TAG = JsonServiceFetcher.class.getSimpleName();
    protected ProgressIndicator mProgress;
    protected ServiceCallbacks<T> mCallbacks;
    private FailureResultFactory mFailureResultFactory;
    protected VolleyRequest<I, T> mVolleyRequest;
    private ObjectToStringConverter mResponseConverter;
    private Class<T> mClass;

    public JsonServiceFetcher(
            VolleyRequest<I, T> volleyRequest,
            ProgressIndicator progress, 
            ObjectToStringConverter responseConverter,
            FailureResultFactory failureResultFactory, 
            Class<T> classInstance) {
        mProgress = progress;
        mFailureResultFactory = failureResultFactory;
        mVolleyRequest = volleyRequest;
        mResponseConverter = responseConverter;
        mClass = classInstance;
        mVolleyRequest.setErrorListener(this);
        mVolleyRequest.setListener(this);
    }

    public void go() {
        RequestQueue queue = Application.getRequestQueue();//Volley.newRequestQueue(mAppContext);
        @SuppressWarnings("rawtypes")
        Request request = mVolleyRequest.getRequest();
        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        try {
            byte[] body = request.getBody();
            if(body!=null) {
                Log.v(TAG, "Sending body: " + body.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        queue.add(request);
        Log.d(TAG, "Sending url: " + request.getUrl());
        if(mProgress!=null) {
            mProgress.start();
        }
    }

    @Override
    public void setServiceCallbacks(ServiceCallbacks<T> callbacks) {
        mCallbacks = callbacks;
    }    
    
    @Override
    public void onResponse(JSONObject response) {
        if(response!=null) Log.v(TAG, "Response: " + response.toString());
        try {
            Object s = response.get("successful");
            if(s!=null && s instanceof Boolean && ((Boolean)s)==false) {
            	
                NetworkResponse nr = new NetworkResponse(200,response.toString().getBytes(),null,false);
                onErrorResponse(new VolleyError(nr));
                return;
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        try {
        	String success = response.toString();
        	T res = mResponseConverter.convert(success, mClass);
        	if(mCallbacks!=null) {
        		mCallbacks.onServiceSuccess(res);
        	}
		} catch (Exception e) {
			Log.e(TAG, "Problem sending success data to listeners", e);
			e.printStackTrace();
		}
        if(mProgress!=null) {
            mProgress.stop();
        }
    }
    
    @Override
    public void onErrorResponse(VolleyError error) {
    	if(error!=null && error.networkResponse!=null) Log.d(TAG, "Volley error status: " + error.networkResponse.statusCode);
    	if(error!=null) Log.d(TAG, "Volley error message: " + error.getMessage());
    	if(error!=null && error.getMessage()!=null && 
    			(error.getMessage().contains("No authentication challenges found") ||
    			 error.getMessage().contains("authentication challenge"))) {
    		error = new VolleyError(new NetworkResponse(403, null, null, false));
    	}
        if(error!=null) Log.e(TAG, "Service error: " +  error.toString(), error.getCause());
        if(mProgress!=null) {
            mProgress.stop();
        }        
        FailureResult f = mFailureResultFactory.newInstance(error);
        if(mCallbacks!=null) {
            mCallbacks.onServiceFail(f);
        }
    }

	@Override
	public I getBody() {
		return mVolleyRequest.getBody();
	}

	@Override
	public void setUrl(String url) {
		mVolleyRequest.setUrl(url);
	}

}
