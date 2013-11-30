package org.denevell.natch.android.service;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public abstract class BaseService implements Listener<JSONObject>, ErrorListener {

    private Context mAppContext;
    private String mUrl;

    public BaseService(Context applicationContext, 
            String url) {
        mAppContext = applicationContext;
        mUrl = url;
    }

    public void go() {
        RequestQueue queue = Volley.newRequestQueue(mAppContext);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, mUrl, null,
                this, this) {
        };
        queue.add(jsObjRequest);
    }

}
