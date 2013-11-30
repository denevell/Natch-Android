package org.denevell.natch.android.service;

import org.denevell.natch.android.views.LoginResultDisplayer;
import org.denevell.natch.android.views.ProgressIndicator;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;

public class LoginService extends BaseService {

    private ProgressIndicator mProgressIndicator;
    private LoginResultDisplayer mResultsDisplayer;

    public LoginService(Context applicationContext, 
            String url,
            ProgressIndicator progress, 
            LoginResultDisplayer resultDisplayer) {
        super(applicationContext, url);
        mResultsDisplayer = resultDisplayer;
        mProgressIndicator = progress;
    }

    @Override
    public void onResponse(JSONObject response) {
        mResultsDisplayer.onSuccess(response.toString());
        mProgressIndicator.stop();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mResultsDisplayer.onFail(error.toString());
        mProgressIndicator.stop();
    }

}
