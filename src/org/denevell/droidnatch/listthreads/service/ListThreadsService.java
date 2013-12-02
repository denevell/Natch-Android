package org.denevell.droidnatch.listthreads.service;

import org.denevell.droidnatch.baseclasses.BaseService;
import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.FailureResultFactory;
import org.denevell.droidnatch.interfaces.ProgressIndicator;
import org.denevell.droidnatch.interfaces.ResponseConverter;
import org.denevell.droidnatch.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.interfaces.ServiceFetcher;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public class ListThreadsService extends BaseService 
    implements ServiceFetcher<ListThreadsResource> {

    private ServiceCallbacks<ListThreadsResource> mCallbacks;
    private ResponseConverter mResponseConverter;
    private FailureResultFactory mFailureResultFactory;

    public ListThreadsService(Context applicationContext, 
            String url,
            ProgressIndicator progress, 
            ResponseConverter responseConverter, 
            FailureResultFactory failureResultFactory) {
        super(applicationContext, url, progress);
        mResponseConverter = responseConverter;
        mFailureResultFactory = failureResultFactory;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        String success = response.toString();
        ListThreadsResource res = mResponseConverter.convert(success, ListThreadsResource.class);
        if(mCallbacks!=null) {
            mCallbacks.success(res);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        NetworkResponse networkResponse = error.networkResponse;
        int status = -1;
        if(networkResponse!=null) {
            status = networkResponse.statusCode;
        } 
        FailureResult f = mFailureResultFactory.newInstance(status, error.toString(), "");
        if(mCallbacks!=null) {
            mCallbacks.fail(f);
        }
    }

    @Override
    public void setServiceCallbacks(ServiceCallbacks<ListThreadsResource> callbacks) {
        mCallbacks = callbacks;
    }

}
