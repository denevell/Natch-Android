package org.denevell.droidnatch;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;

public final class FailureFactory implements FailureResultFactory {
    @Override
    public FailureResult newInstance(int statusCode, String errorMessage, String errorCode) {
        return new FailureResult(errorCode, errorMessage, statusCode);
    }
}