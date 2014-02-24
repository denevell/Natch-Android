package org.denevell.droidnatch;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * This class needs a good refactor.
 * Just playing around at the moment, innit. INNIT. 
 */
public class SeenThreadsSaver {

	private static final String TAG = SeenThreadsSaver.class.getSimpleName();
	private static Set<String> seenThreads = new HashSet<String>();

	/**
	 * @param id
	 * @return false is we've not seen anything...
	 */
	public static boolean isThisIdNew(Context c, String id) {
		boolean areThingsInThreadSet = seenThreads.size()>0;
		if(!areThingsInThreadSet) {
			areThingsInThreadSet = addThreadsInPreferencesToStaticVariable(c);
		}
		Log.v(TAG, "Looked at " + seenThreads.size() + " saved threads.");
		return areThingsInThreadSet && !seenThreads.contains(id);
	}

	public static void addThreadId(Context c, String id) {
		seenThreads.add(id);

		SharedPreferences sp = c.getSharedPreferences("droidnatch", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putStringSet("ids", seenThreads);
		edit.commit();
	}

	private static boolean addThreadsInPreferencesToStaticVariable(Context c) {
		boolean areThingsInThreadSet = false;
		if(seenThreads.size()<=0) {
			Log.w(TAG, "We've got an empty saved threads in the static variables map...");
		}
		SharedPreferences sp = c.getSharedPreferences("droidnatch", Context.MODE_PRIVATE);
		Set<String> ids = sp.getStringSet("ids", null);
		if(ids!=null) {
			if(seenThreads.size()<=0) {
				Log.w(TAG, "BUT, happily, we've got some saved.");
			}
			seenThreads.addAll(ids);
			areThingsInThreadSet = true;
		} else {
			if(seenThreads.size()<=0) {
				Log.w(TAG, "AND we've got an empty saved threads map in shared preferences...");
			}
		}
		return areThingsInThreadSet;
	}
	

}
