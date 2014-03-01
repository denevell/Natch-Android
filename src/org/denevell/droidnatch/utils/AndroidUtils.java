package org.denevell.droidnatch.utils;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class AndroidUtils {
	
    private static final String TAG = AndroidUtils.class.getSimpleName();

	public static boolean checkPlayServices(Activity context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
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


}
