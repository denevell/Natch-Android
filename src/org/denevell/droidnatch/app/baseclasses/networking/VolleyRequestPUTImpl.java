package org.denevell.droidnatch.app.baseclasses.networking;

import java.util.HashMap;
import java.util.Map;

import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

public class VolleyRequestPUTImpl<T> implements VolleyRequest<T> {
    
    private ObjectToStringConverter mResponseConverter;
    private String mUrl;
    private ErrorListener mErrorListener;
    private Listener<JSONObject> mListener;
    private Map<String, String> headersMap = new HashMap<String, String>();
    private Object mBody;
    
    public VolleyRequestPUTImpl(
            ObjectToStringConverter responseConverter, 
            Object body) {
        mResponseConverter = responseConverter;
        mBody = body;
    }
    
    public ObjectToStringConverter getResponseConverter() {
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
               map.putAll(headersMap);
               return map;
            }
        };
        return jsObjRequest;
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public void addHeader(String header, String value) {
        headersMap.put(header, value);
    }


}
