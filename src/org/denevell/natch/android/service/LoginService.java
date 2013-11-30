package org.denevell.natch.android.service;

import org.denevell.natch.android.R;
import org.denevell.natch.android.views.LoginResultDisplayer;
import org.denevell.natch.android.views.ProgressIndicator;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class LoginService {

    private Context mAppContext;
    private String mUrl;
    private ProgressIndicator mProgressIndicator;
    private LoginResultDisplayer mResultsDisplayer;

    public LoginService(Context applicationContext, ProgressIndicator progress, LoginResultDisplayer resultDisplayer) {
        mResultsDisplayer = resultDisplayer;
        mProgressIndicator = progress;
        mAppContext = applicationContext;
        mUrl = mAppContext.getString(R.string.url_baseurl) + mAppContext.getString(R.string.url_threads);
    }

    public void go() {
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mResultsDisplayer.onSuccess(response.toString());
                        mProgressIndicator.stop();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mResultsDisplayer.onFail(error.toString());
                        mProgressIndicator.stop();
                    }
                }) {
        };
        queue.add(jsObjRequest);
        mProgressIndicator.start();
    }

}
