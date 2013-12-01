package org.denevell.droidnatch.login.service;

import org.denevell.droidnatch.baseclasses.BaseService;
import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.ProgressIndicatable;
import org.denevell.droidnatch.interfaces.ServiceCallable;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;

public class LoginService extends BaseService implements ServiceCallable<String>{

    private Success<String> mSuccessCallback;
    private Failure mFailCallback;

    public LoginService(Context applicationContext, 
            String url,
            ProgressIndicatable progress) {
        super(applicationContext, url, progress);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        String success = response.toString();
        mSuccessCallback.success(success);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        FailureResult f = new FailureResult("", error.toString(), -1);
        mFailCallback.fail(f);
    }

    @Override
    public void setSuccessCallback(Success<String> r) {
        mSuccessCallback = r;
    }

    @Override
    public void setFailureCallback(Failure r) {
        mFailCallback = r;
    }

}
