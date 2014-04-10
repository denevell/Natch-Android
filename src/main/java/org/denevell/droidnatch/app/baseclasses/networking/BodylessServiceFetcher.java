package org.denevell.droidnatch.app.baseclasses.networking;

import org.denevell.droidnatch.Application;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

final class BodylessServiceFetcher<I, R> 
	implements ServiceFetcher<Void, Void>,
		ErrorListener,
		Listener<String>{

	private int mMethod;

	/**
	 * @param mUrl2 
	 * @param serviceBuilder
	 */
	BodylessServiceFetcher(int method, String url) {
		this.mMethod = method;
		this.mUrl = url;
	}

	private ServiceCallbacks<Void> mCallbacks;
	private String mUrl;

	@Override public void setServiceCallbacks(ServiceCallbacks<Void> callbacks) {
		mCallbacks = callbacks;
	}

	@Override
	public void go() {
	    RequestQueue queue = Application.getRequestQueue();//Volley.newRequestQueue(mAppContext);
	    StringRequest request = new StringRequest(mMethod, mUrl, this, this);
	    request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
	    queue.add(request);
	    Log.d(ServiceBuilder.TAG, "Sending url: " + request.getUrl());
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		mCallbacks.onServiceFail(null);
	}

	@Override
	public void onResponse(String response) {
		mCallbacks.onServiceSuccess(null);
	}

	@Override
	public Void getBody() {
		return null;
	}

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}
}