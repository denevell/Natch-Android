package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public final class NormalFailureFactory implements FailureResultFactory {
    @Override
    public FailureResult newInstance(VolleyError error) {
    	NetworkResponse nr = error.networkResponse;
		try {
			if(nr!= null && (nr.statusCode==401|| nr.statusCode==403)){
                return new FailureResult("", "Please (re)login", nr.statusCode);
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