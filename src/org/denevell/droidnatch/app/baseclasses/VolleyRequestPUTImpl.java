package org.denevell.droidnatch.app.baseclasses;

import java.util.HashMap;
import java.util.Map;

import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

public class VolleyRequestPUTImpl<T> implements VolleyRequest<T> {
    
    private ObjectStringConverter mResponseConverter;
    private String mUrl;
    private ErrorListener mErrorListener;
    private Listener<JSONObject> mListener;
    private Object mBody;
    
    public VolleyRequestPUTImpl(
            ObjectStringConverter responseConverter, 
            Object body) {
        mResponseConverter = responseConverter;
        mBody = body;
    }
    
    public ObjectStringConverter getResponseConverter() {
        return mResponseConverter;
    }

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        mErrorListener = errorListener;
    }

    @Override
    public void setListener(Listener<JSONObject> listener) {
        mListener = listener;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Request getRequest() {
        String json = mResponseConverter.encode(mBody);
        JSONObject jOb = null;
        try {
            jOb = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest( 
                Request.Method.PUT, mUrl, jOb,
                mListener, mErrorListener) {
            @Override
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
