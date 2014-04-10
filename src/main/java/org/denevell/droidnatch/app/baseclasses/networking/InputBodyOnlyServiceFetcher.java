package org.denevell.droidnatch.app.baseclasses.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.denevell.droidnatch.Application;
import org.denevell.droidnatch.app.baseclasses.NormalFailureFactory;
import org.denevell.droidnatch.app.baseclasses.networking.JsonVolleyRequest.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

final class InputBodyOnlyServiceFetcher<I, R> 
	implements ServiceFetcher<I, Void>,
		ErrorListener,
		Listener<String>{
	private static final String TAG = InputBodyOnlyServiceFetcher.class.getSimpleName();
	private int mMethod;
	private I mBody;
	private List<LazyHeadersCallback> mLazyHeaders;

	/**
	 * @param mLazyHeaders 
	 * @param mUrl2 
	 * @param serviceBuilder
	 */
	InputBodyOnlyServiceFetcher(int method, I body, String url, ArrayList<LazyHeadersCallback> lazyHeaders) {
		mMethod = method;
		mBody = body;
		mUrl = url;
		mLazyHeaders = lazyHeaders;
	}

	private ServiceCallbacks<Void> mCallbacks;
	private String mUrl;

	@Override public void setServiceCallbacks(ServiceCallbacks<Void> callbacks) {
		mCallbacks = callbacks;
	}

	@Override
	public void go() {
	    RequestQueue queue = Application.getRequestQueue();//Volley.newRequestQueue(mAppContext);
	    StringWithJsonBodyRequest request = new StringWithJsonBodyRequest(mMethod, mUrl, this, this)  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> headers = super.getHeaders();
               HashMap<String, String> map = new HashMap<String, String>(headers);
               for (LazyHeadersCallback i: mLazyHeaders) {
            	   i.run(map);
               }
               return map;
            }
	    };
	    String json = new Gson().toJson(mBody);
		Log.d(TAG, "Sending body: " + json);
		request.setBody(json);
	    request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
	    queue.add(request);
	    Log.d(ServiceBuilder.TAG, "Sending url: " + request.getUrl());
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d(TAG, "Service returned an error: " + error.getMessage());
    	if(error!=null && error.getMessage()!=null && 
    			(error.getMessage().contains("No authentication challenges found") ||
    			 error.getMessage().contains("authentication challenge"))) {
    		error = new VolleyError(new NetworkResponse(403, null, null, false));
    	}
		mCallbacks.onServiceFail(new NormalFailureFactory().newInstance(error));
	}

	@Override
	public void onResponse(String response) {
		Log.d(TAG, "Service returned normally: " + response);
		mCallbacks.onServiceSuccess(null);
	}

	@Override
	public I getBody() {
		return mBody;
	}

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}
}