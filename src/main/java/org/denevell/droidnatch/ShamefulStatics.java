package org.denevell.droidnatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ShamefulStatics {
	
	private static final String TAG = ShamefulStatics.class.getSimpleName();
	private static String sUsername = "";
	private static String sBasePath = "";
    private static String sAuthKey = "";

	public static void setBasePath(String base) {
		sBasePath = base;
	}
	
	public static String getBasePath() {
		return sBasePath;
	}

    public static String getAuthKey(Context appContext) {
    	if(sAuthKey.length()==0) {
    		sAuthKey = PreferenceManager
    			.getDefaultSharedPreferences(appContext)
    			.getString("auth_key", "");
    	}
        return sAuthKey;
    }

    public static void setAuthKey(String sAuthKey, Context appContext) {
        ShamefulStatics.sAuthKey = sAuthKey;
 		PreferenceManager
    		.getDefaultSharedPreferences(appContext)
    		.edit()
    		.putString("auth_key", sAuthKey)
    		.commit();
    }

	public static String getUsername(Context appContext) {
    	if(sUsername.trim().length()==0) {
    		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
			sUsername = defaultSharedPreferences.getString("username", "");
    	}
		return sUsername;
	}

	public static void setUsername(String sUsername, Context appContext) {
		ShamefulStatics.sUsername = sUsername;
   		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    	boolean committed = defaultSharedPreferences.edit()
    		.putString("username", sUsername)
    		.commit();
    	Log.i(TAG, "New username commited: " + committed);
	}
	
	public static boolean emptyUsername(Context appContext) {
		String username = getUsername(appContext);
		return username == null || username.trim().length()==0;
	}

	public static void logout(Context applicationContext) {
		ShamefulStatics.setAuthKey("", applicationContext);
		ShamefulStatics.setUsername("", applicationContext);
	}

}
