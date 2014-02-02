package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface ReceivingUiObject<T> {

    void success(T result);

    void fail(FailureResult r);
}
