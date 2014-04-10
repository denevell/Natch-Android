package org.denevell.droidnatch.app.baseclasses.networking;

import java.util.ArrayList;

import org.denevell.droidnatch.PaginationMapper.PaginationObject;
import org.denevell.droidnatch.app.baseclasses.JsonConverter;
import org.denevell.droidnatch.app.baseclasses.NatchJsonFailureFactory;
import org.denevell.droidnatch.app.baseclasses.ProgressBarIndicator;
import org.denevell.droidnatch.app.baseclasses.networking.JsonVolleyRequest.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

import android.app.Activity;

public class ServiceBuilder<I, R> {

	static final String TAG = ServiceBuilder.class.getSimpleName();

	private PaginationObject mPagination;
	private String mUrl;
	int mMethod; // Request.Method.GET for example
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

	public ServiceFetcher<I, R> createJson(Activity act, Class<R> classInstance) {
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

		return new JsonServiceFetcher<I, R>(request, progressIndicator,
				mResponseConverter, mFailureFactory, classInstance);
	}

	public ServiceFetcher<Void, Void> createNeitherInputOrResponseBody() {
		return new BodylessServiceFetcher<I, R>(mMethod);
	}

	public ServiceFetcher<Void, Void> createNoResponseBodyButInputBody() {
		return new BodylessServiceFetcher<I, R>(mMethod);
	}


}
