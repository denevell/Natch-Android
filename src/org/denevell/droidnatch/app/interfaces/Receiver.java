package org.denevell.droidnatch.app.interfaces;

import org.denevell.droidnatch.app.baseclasses.FailureResult;

public interface Receiver<T> {

    void success(T result);

    void fail(FailureResult r);
}
