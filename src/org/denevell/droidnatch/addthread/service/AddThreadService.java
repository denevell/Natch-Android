package org.denevell.droidnatch.addthread.service;

import org.denevell.droidnatch.addthread.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
import org.json.JSONObject;

import android.content.Context;

public class AddThreadService extends BaseService<AddPostResourceReturnData> {

    private ResponseConverter mResponseConverter;

    public AddThreadService(Context applicationContext, 
            String url,
            ProgressIndicator progress, 
            ResponseConverter responseConverter, 
            FailureResultFactory failureResultFactory) {
        super(applicationContext, url, progress, failureResultFactory);
        mResponseConverter = responseConverter;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        String success = response.toString();
        AddPostResourceReturnData res = mResponseConverter.convert(success, AddPostResourceReturnData.class);
        if(mCallbacks!=null) {
            mCallbacks.onServiceSuccess(res);
        }
    }


}
