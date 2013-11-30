package org.denevell.natch.android.views;

import android.content.Context;
import android.widget.Toast;

public class LoginResultDisplayer {
    
    private Context mAppContext;

    public LoginResultDisplayer(Context appContext) {
        mAppContext = appContext;
    }

    public void onSuccess(final String success) {
        Toast.makeText(mAppContext, success, Toast.LENGTH_LONG) .show();
    }

    public void onFail(final String fail) {
        Toast.makeText(mAppContext, fail, Toast.LENGTH_LONG) .show();
    }
}