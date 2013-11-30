package org.denevell.natch.android.views;

import android.app.Activity;

public class ProgressIndicator {
    
    private Activity mActivity;

    public ProgressIndicator(Activity activity) {
        mActivity = activity;
    }
    
    public void start() {
        mActivity.setProgressBarIndeterminateVisibility(true);
    }
    
    public void stop() {
        mActivity.setProgressBarIndeterminateVisibility(false);
    }

}
