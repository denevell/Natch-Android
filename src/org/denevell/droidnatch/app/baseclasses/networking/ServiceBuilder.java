package org.denevell.droidnatch.app.baseclasses.networking;

import org.denevell.droidnatch.AppWideMapper.PaginationObject;
import org.denevell.droidnatch.app.baseclasses.FailureFactory;
import org.denevell.droidnatch.app.baseclasses.JsonConverter;
import org.denevell.droidnatch.app.baseclasses.ProgressBarIndicator;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;

import android.app.Activity;

public class ServiceBuilder<I, R> {

	@SuppressWarnings("unused")
	private static final String TAG = ServiceBuilder.class.getSimpleName();

	private PaginationObject mPagination;
	private String mUrl;
	private int mMethod; // Request.Method.GET for example
	private ObjectToStringConverter mResponseConverter = new JsonConverter();
	private FailureResultFactory mFailureFactory = new FailureFactory();

	public ServiceBuilder<I, R> pagination(PaginationObject p) {
		mPagination = p;
		return this;
	}

	public ServiceBuilder<I, R> url(String url) {
		mUrl = url;
		return this;
	}

	public ServiceBuilder<I, R> method(int method) {
		mMethod = method;
		return this;
	}

	public ServiceFetcher<I, R> create(Activity act, Class<R> classInstance) {
		ProgressIndicator progressIndicator = new ProgressBarIndicator(act);

		mUrl += "" + mPagination.start + "/" + mPagination.range;
		VolleyRequestImpl<I, R> request = new VolleyRequestImpl<I, R>(null,
				null, mMethod);
		request.setUrl(mUrl);

		return new BaseService<I, R>(request, progressIndicator,
				mResponseConverter, mFailureFactory, classInstance);

	}

}
