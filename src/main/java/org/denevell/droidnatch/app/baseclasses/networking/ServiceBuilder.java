package org.denevell.droidnatch.app.baseclasses.networking;

import java.util.ArrayList;

import org.denevell.droidnatch.Application;
import org.denevell.droidnatch.PaginationMapper.PaginationObject;
import org.denevell.droidnatch.app.baseclasses.JsonConverter;
import org.denevell.droidnatch.app.baseclasses.NatchJsonFailureFactory;
import org.denevell.droidnatch.app.baseclasses.ProgressBarIndicator;
import org.denevell.droidnatch.app.baseclasses.networking.JsonVolleyRequest.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

import android.app.Activity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class ServiceBuilder<I, R> {

	private static final String TAG = ServiceBuilder.class.getSimpleName();

	private PaginationObject mPagination;
	private String mUrl;
	private int mMethod; // Request.Method.GET for example
	private ObjectToStringConverter mResponseConverter = new JsonConverter();
	private FailureResultFactory mFailureFactory = new NatchJsonFailureFactory();
	private ArrayList<LazyHeadersCallback> mLazyHeaders = new ArrayList<JsonVolleyRequest.LazyHeadersCallback>();
	private I mEntity; // That we send up in the request

	public ServiceBuilder<I, R> pagination(PaginationObject p) {
		mPagination = p;
		return this;
	}

	public ServiceBuilder<I, R> url(String url) {
		mUrl = url;
		return this;
	}

	public ServiceBuilder<I, R> addLazyHeader(LazyHeadersCallback callback) {
		mLazyHeaders.add(callback);
		return this;
	}

	public ServiceBuilder<I, R> method(int method) {
		mMethod = method;
		return this;
	}

	public ServiceBuilder<I, R> entity(I entity) {
		mEntity = entity;
		return this;
	}

	public ServiceFetcher<I, R> create(Activity act, Class<R> classInstance) {
		ProgressBarIndicator progressIndicator = null;
		if(act!=null) {
			progressIndicator = new ProgressBarIndicator(act);
		}

		if(mPagination!=null) {
			mUrl += "" + mPagination.start + "/" + mPagination.range;
		}

		JsonVolleyRequest<I, R> request = new JsonVolleyRequest<I, R>(
				mResponseConverter,
				mEntity, 
				mMethod);
		request.setUrl(mUrl);
		for (LazyHeadersCallback callback: mLazyHeaders) {
			request.addLazyHeader(callback);
		}

		return new BaseService<I, R>(request, progressIndicator,
				mResponseConverter, mFailureFactory, classInstance);
	}

	public ServiceFetcher<Void, Void> createBasic(Activity act) {
/*		ProgressBarIndicator progressIndicator = null;
		if(act!=null) {
			progressIndicator = new ProgressBarIndicator(act);
		}*/

		if(mPagination!=null) {
			mUrl += "" + mPagination.start + "/" + mPagination.range;
		}

		return new ServiceFetcherBasic();
	}

	private final class ServiceFetcherBasic 
		implements ServiceFetcher<Void, Void>,
			ErrorListener,
			Listener<String>{
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
		    Log.d(TAG, "Sending url: " + request.getUrl());
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


}
