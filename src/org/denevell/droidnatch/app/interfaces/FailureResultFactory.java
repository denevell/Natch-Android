package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface FailureResultFactory {
    FailureResult newInstance(int statusCode, String errorMessage, String errorCode);
}
