package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.ProgressIndicator;

import android.app.Activity;

public class ProgressBarIndicator implements ProgressIndicator {
    
    private Activity mActivity;

    public ProgressBarIndicator(Activity activity) {
        mActivity = activity;
    }
    
    @Override
    public void start() {
        mActivity.setProgressBarIndeterminateVisibility(true);
    }
    
    @Override
    public void stop() {
        mActivity.setProgressBarIndeterminateVisibility(false);
    }

}
