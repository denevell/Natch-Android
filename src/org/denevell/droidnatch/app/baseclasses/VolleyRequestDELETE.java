package org.denevell.droidnatch.app.baseclasses;

import java.util.HashMap;
import java.util.Map;

import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

public class VolleyRequestDELETE implements VolleyRequest {
    
    private String mUrl;
    private ErrorListener errorListener;
    private Listener<JSONObject> listener;

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
    
    @Override
    public void setListener(Listener<JSONObject> listener) {
        this.listener = listener;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Request getRequest() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.DELETE, mUrl, null,
                listener, errorListener) {
            public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> headers = super.getHeaders();
               HashMap<String, String> map = new HashMap<String, String>(headers);
               map.put("AuthKey", "2a640fd6-e3f2-4896-b224-2eba63afae54");
               return map;
            }
        };
       return jsObjRequest; 
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

}
