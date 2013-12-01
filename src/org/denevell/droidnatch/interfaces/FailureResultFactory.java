package org.denevell.droidnatch.interfaces;

import org.denevell.droidnatch.baseclasses.FailureResult;

public interface FailureResultFactory {
    FailureResult newInstance(int statusCode, String errorMessage, String errorCode);
}
