package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public final class NatchJsonFailureFactory implements FailureResultFactory {
    @Override
    public FailureResult newInstance(VolleyError error) {
    	NetworkResponse nr = error.networkResponse;
    	JSONObject json;
		try {
			json = new JSONObject(new String(error.networkResponse.data));
			String errorString = json.getString("error");
    		return new FailureResult("", errorString, nr.statusCode);
		} catch (JSONException e) {
			e.printStackTrace();
    		return new FailureResult("", "Unknown error", nr.statusCode);
		}
    }
}