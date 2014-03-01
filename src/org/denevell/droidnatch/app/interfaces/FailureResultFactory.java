package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

import com.android.volley.VolleyError;

public interface FailureResultFactory {
    FailureResult newInstance(VolleyError error);
}
