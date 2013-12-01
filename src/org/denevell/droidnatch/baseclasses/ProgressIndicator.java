package org.denevell.droidnatch.baseclasses;

import org.denevell.droidnatch.interfaces.ProgressIndicatable;

import android.app.Activity;

public class ProgressIndicator implements ProgressIndicatable {
    
    private Activity mActivity;

    public ProgressIndicator(Activity activity) {
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
