package org.denevell.droidnatch.addthread.service;

import org.denevell.droidnatch.addthread.entities.AddPostResourceInput;
import org.denevell.droidnatch.addthread.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;

public class AddThreadService extends BaseService<AddPostResourceReturnData> {

    private ResponseConverter mResponseConverter;
    private Context mAppContext;

    public AddThreadService(Context applicationContext, 
            String url,
            ProgressIndicator progress, 
            ResponseConverter responseConverter, 
            FailureResultFactory failureResultFactory) {
        super(applicationContext, url, progress, failureResultFactory);
        mAppContext = applicationContext;
        mResponseConverter = responseConverter;
    }
    
    public void go() {
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        // TODO: Add auth header
        // TODO: Input auth object
        AddPostResourceInput addPostResourceInput = new AddPostResourceInput();
        String json = mResponseConverter.encode(addPostResourceInput);
        JSONObject jOb = null;
        try {
            jOb = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST, "", jOb,
                this, this) {
        };
        queue.add(jsObjRequest);
        mProgress.start();
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
