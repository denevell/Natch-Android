package org.denevell.droidnatch.app.utils;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class AndroidUtils {
	
    private static final String TAG = AndroidUtils.class.getSimpleName();

	public static boolean checkPlayServices(Activity context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        Log.i(TAG, "Play services check returned: " + resultCode);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        9000).show();
            } else {
                Log.i(TAG, "This device is not supported for Google play services.");
            }
            return false;
        }
        return true;
    }

	public static boolean isFragmentManagerEmpty(FragmentManager supportFragmentManager) {
    	if(supportFragmentManager.getFragments()==null || supportFragmentManager.getFragments().size()==0) {
    		return true;
    	} else {
    		return false;
    	}
    }



}
