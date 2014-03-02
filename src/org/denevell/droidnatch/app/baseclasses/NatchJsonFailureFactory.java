package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public final class NatchJsonFailureFactory implements FailureResultFactory {
    @Override
    public FailureResult newInstance(VolleyError error) {
    	NetworkResponse nr = error.networkResponse;
    	JSONObject json;
		try {
			if(nr!= null && (nr.statusCode==401|| nr.statusCode==403)){
                return new FailureResult("", "Please (re)login", nr.statusCode);
			} else if(nr!=null && nr.data!=null) {
				json = new JSONObject(new String(error.networkResponse.data));
                String errorString = json.getString("error");
                return new FailureResult("", errorString, nr.statusCode);
			} else {
				int sc = (nr!=null) ? nr.statusCode : -1;
				return new FailureResult("", "Unknown error", sc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			int sc = (nr!=null) ? nr.statusCode : -1;
    		return new FailureResult("", "Unknown error", sc);
		}
    }
}