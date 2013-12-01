package org.denevell.droidnatch.login.views;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.ResultsDisplayable;

import android.content.Context;
import android.widget.Toast;

public class LoginResultDisplayer implements ResultsDisplayable<String> {
    
    private Context mAppContext;

    public LoginResultDisplayer(Context appContext) {
        mAppContext = appContext;
    }

    public void onSuccess(final String success) {
        Toast.makeText(mAppContext, success, Toast.LENGTH_LONG) .show();
    }

    public void onFail(final FailureResult fail) {
        Toast.makeText(mAppContext, fail.errorMessage, Toast.LENGTH_LONG) .show();
    }

}