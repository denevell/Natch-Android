package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;

public final class FailureFactory implements FailureResultFactory {
    @Override
    public FailureResult newInstance(int statusCode, String errorMessage, String errorCode) {
        return new FailureResult(errorCode, errorMessage, statusCode);
    }
}