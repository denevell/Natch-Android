package org.denevell.droidnatch.app.baseclasses;

import java.util.Map;
import java.util.Map.Entry;

import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class FragmentScreenOpener implements ScreenOpener {

    private static final String TAG = ScreenOpener.class.getSimpleName();
    private FragmentActivity mActivity;

    public FragmentScreenOpener(FragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public void openScreen(Class<?> screenClass, 
    		Map<String, String> passedVars, 
    		boolean backStack) {
        try {
            Fragment newInstance = (Fragment) screenClass.newInstance();
            Bundle b = new Bundle();
            if(passedVars!=null) {
            	for (Entry<String, String> i: passedVars.entrySet()) {
            		b.putString(i.getKey(), i.getValue());
            	}
            }
            newInstance.setArguments(b);
            Log.v(TAG, "Opening: " + screenClass.getSimpleName());
            FragmentTransaction f = mActivity.getSupportFragmentManager()
            .beginTransaction()
            //.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)      
            .setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_left, R.anim.slide_in_left, R.anim.fade_out_right)      
            .replace(R.id.fragment_holder, newInstance);
            if(backStack) {
            	f.addToBackStack(newInstance.getClass().getSimpleName());
            }
            f.commit();            
        } catch (Exception e) {
            Log.e(TAG, "Couldn't open screen: " + screenClass, e);
        }
    }

    @Override
    public void gotoPreviousScreen() {
        try {
            mActivity.getSupportFragmentManager().popBackStack();
        } catch (Exception e) {
            Log.e(TAG, "Couldn't pop screen.", e);
        }
    }

}