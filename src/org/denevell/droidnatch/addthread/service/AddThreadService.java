package org.denevell.droidnatch.addthread.service;

import org.denevell.droidnatch.addthread.entities.AddPostResourceInput;
import org.denevell.droidnatch.addthread.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
import org.denevell.droidnatch.app.interfaces.TextEditable;
import org.denevell.droidnatch.app.interfaces.TextEditable.OnTextInputted;
import org.denevell.droidnatch.app.interfaces.VolleyRequestPUT;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AddThreadService extends BaseService<AddPostResourceReturnData> {

    private ResponseConverter mResponseConverter;
    private Context mAppContext;
    private AddPostResourceInput mAddPostResourceInput = new AddPostResourceInput();
    private VolleyRequestPUT mVolleyRequestPUT;

    public AddThreadService(Context applicationContext, 
            String url,
            ProgressIndicator progress, 
            ResponseConverter responseConverter, 
            FailureResultFactory failureResultFactory, 
            TextEditable textEditable,
            VolleyRequestPUT volleyRequest) {
        super(applicationContext, url, progress, failureResultFactory, volleyRequest);
        mVolleyRequestPUT = volleyRequest;
        mAppContext = applicationContext;
        mResponseConverter = responseConverter;
        textEditable.addTextInputCallack(new OnTextInputted() {
            @Override
            public void onTextSubmitted(String textSubmitted) {
                mAddPostResourceInput.setContent("-");
                mAddPostResourceInput.setSubject(textSubmitted);
            }
        });
    }
    
    public void go() {
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        mVolleyRequestPUT.setBody(mAddPostResourceInput);
        Request<?> jsObjRequest = mVolleyRequestPUT.getRequest();
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
