package org.denevell.droidnatch.app.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;

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

	public static int getStatusBarHeight(Context activityContext) {
	      int result = 0;
	      int resourceId = activityContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = activityContext.getResources().getDimensionPixelSize(resourceId);
	      }
	      return result;
	}

	public static int getNavigationBarWidth(Context activityContext) {
	      int result = 0;
	      int resourceId = activityContext.getResources().getIdentifier("navigation_bar_width", "dimen", "android");
	      if (resourceId > 0) {
	          result = activityContext.getResources().getDimensionPixelSize(resourceId);
	      }
	      return result;
	}

	public static int getNavigationBarHeight(Context activityContext) {
	      int result = 0;
	      int resourceId = activityContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = activityContext.getResources().getDimensionPixelSize(resourceId);
	      }
	      return result;
	}
	
	public static int getActionbarHeight(Context activityContext) {
		TypedValue tv = new TypedValue();
		int actionBarHeight = 0;
		if (activityContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,activityContext.getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}


}
