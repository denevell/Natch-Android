package org.denevell.droidnatch.app.baseclasses;

import java.lang.ref.WeakReference;

import org.denevell.droidnatch.app.interfaces.ProgressIndicator;

import android.app.Activity;

public class ProgressBarIndicator implements ProgressIndicator {
    
    private WeakReference<Activity> mActivity;

    public ProgressBarIndicator(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }
    
    @Override
    public void start() {
    	if(mActivity!=null && mActivity.get()!=null) {
    		mActivity.get().setProgressBarIndeterminateVisibility(true);
    	}
    }
    
    @Override
    public void stop() {
    	if(mActivity!=null && mActivity.get()!=null) {
    		mActivity.get().setProgressBarIndeterminateVisibility(false);
    	}
    }

}
