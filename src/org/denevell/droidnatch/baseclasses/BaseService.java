package org.denevell.droidnatch.baseclasses;

import org.denevell.droidnatch.interfaces.ProgressIndicatable;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public abstract class BaseService implements Listener<JSONObject>, ErrorListener {

    private Context mAppContext;
    private String mUrl;
    private ProgressIndicatable mProgress;

    public BaseService(Context applicationContext, String url, ProgressIndicatable progress) {
        mAppContext = applicationContext;
        mUrl = url;
        mProgress = progress;
    }

    public void go() {
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, mUrl, null,
                this, this) {
        };
        queue.add(jsObjRequest);
        mProgress.start();
    }
    
    @Override
    public void onResponse(JSONObject response) {
        mProgress.stop();
    }
    
    @Override
    public void onErrorResponse(VolleyError error) {
        mProgress.stop();
    }

}
