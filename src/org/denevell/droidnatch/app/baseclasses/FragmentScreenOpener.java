package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.natch.android.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class FragmentScreenOpener implements ScreenOpener {

    private static final String TAG = ScreenOpener.class.getSimpleName();
    private FragmentActivity mActivity;

    public FragmentScreenOpener(FragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public void openScreen(Class<?> screenClass) {
        try {
            Fragment newInstance = (Fragment) screenClass.newInstance();
            Log.v(TAG, "Opening: " + screenClass.getSimpleName());
            mActivity.getSupportFragmentManager()
            .beginTransaction()
            .addToBackStack(newInstance.getClass().getSimpleName())
            .replace(R.id.fragment_holder, newInstance) 
            .commit();            
        } catch (Exception e) {
            Log.e(TAG, "Couldn't open screen: " + screenClass, e);
        }
    }

}