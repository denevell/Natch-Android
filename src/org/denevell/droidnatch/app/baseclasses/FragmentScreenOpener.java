package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.ScreenOpener;

import android.app.Activity;
import android.util.Log;

public class FragmentScreenOpener implements ScreenOpener {

    private static final String TAG = ScreenOpener.class.getSimpleName();

    public FragmentScreenOpener(Activity mActivity) {
    }

    @Override
    public void openScreen(Class<?> screenClass) {
        try {
            Log.v(TAG, "Opening: " + screenClass.getSimpleName());
        } catch (Exception e) {
            Log.e(TAG, "Couldn't open screen: " + screenClass, e);
        }
    }

}
