package org.denevell.droidnatch.listthreads.service;

import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.json.JSONObject;

import android.content.Context;

public class ListThreadsService extends BaseService<ListThreadsResource> 
{

    private ResponseConverter mResponseConverter;

    public ListThreadsService(Context applicationContext, 
            String url,
            ProgressIndicator progress, 
            ResponseConverter responseConverter, 
            FailureResultFactory failureResultFactory,
            VolleyRequest volleyRequest) {
        super(applicationContext, url, progress, failureResultFactory, volleyRequest);
        mResponseConverter = responseConverter;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        String success = response.toString();
        ListThreadsResource res = mResponseConverter.convert(success, ListThreadsResource.class);
        if(mCallbacks!=null) {
            mCallbacks.onServiceSuccess(res);
        }
    }


}
