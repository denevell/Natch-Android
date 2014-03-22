package org.denevell.droidnatch;

import java.util.Hashtable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class needs a good refactor.
 * Just playing around at the moment, innit. INNIT. 
 */
public class SeenThreadsSaver {

	public static class ThreadModification {
		public String id;
		public long modification;
		public long visitedModificationDate;
		public ThreadModification(String id, long modification) {
			this.id = id;
			this.modification = modification;
		}
		public ThreadModification(String id, long modification, long visitedModification) {
			this.id = id;
			this.modification = modification;
			this.visitedModificationDate = visitedModification;
		}

	}

	private static final String TAG = SeenThreadsSaver.class.getSimpleName();
	private static Hashtable<String, ThreadModification> seenThreads = new Hashtable<String, ThreadModification>();

	/**
	 * @param id
	 * @return false is we've not seen anything...
	 */
	public static boolean isThisIdNew(Context c, String id) {
		boolean areThingsInThreadSet = recoverFromDiskIfNeeded(c);
		Log.v(TAG, "Looked at " + seenThreads.size() + " saved threads.");
		return areThingsInThreadSet && !seenThreads.containsKey(id);
	}

	public static boolean isThisThreadVisited(String id, long time) {
		ThreadModification threadModification = seenThreads.get(id);
		return threadModification!=null && 
				threadModification.visitedModificationDate>0 && 
				threadModification.visitedModificationDate==time;
	}


	public static boolean recoverFromDiskIfNeeded(Context c) {
		boolean areThingsInThreadSet = seenThreads.size()>0;
		if(!areThingsInThreadSet) {
			areThingsInThreadSet = addThreadsInPreferencesToStaticVariable(c);
		}
		return areThingsInThreadSet;
	}

	public static void addThread(String id, long modification) {
		if(!seenThreads.containsKey(id)) {
			seenThreads.put(id, new ThreadModification(id, modification));
		}
	}

	public static void addVistedThread(String id, long modification) {
		ThreadModification thread = seenThreads.get(id);
		if(thread==null) {
			seenThreads.put(id, new ThreadModification(id, modification, modification));
		} else {
			if(modification>thread.visitedModificationDate) {
				thread.visitedModificationDate = modification;
				seenThreads.put(id, thread);
			}
		}
	}

	public static void commitToStorage(Context c) {
		String json = new Gson().toJson(seenThreads, new TypeToken<Hashtable<String, ThreadModification>>(){}.getType());
		SharedPreferences sp = c.getSharedPreferences("droidnatch", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("ids", json);
		edit.commit();
		
	}

	private static boolean addThreadsInPreferencesToStaticVariable(Context c) {
		boolean areThingsInThreadSet = false;
		if(seenThreads.size()<=0) {
			Log.w(TAG, "We've got an empty saved threads in the static variables map...");
		}
		try {
			SharedPreferences sp = c.getSharedPreferences("droidnatch", Context.MODE_PRIVATE);
			String ids = sp.getString("ids", null);
			if (ids != null) {
				Hashtable<String, ThreadModification> idsObjs = new Gson().fromJson(ids, new TypeToken<Hashtable<String, ThreadModification>>(){}.getType());
				if (seenThreads.size() <= 0) {
					Log.w(TAG, "BUT, happily, we've got some saved.");
					seenThreads.putAll(idsObjs);
					areThingsInThreadSet = true;
				}
			} else {
				if (seenThreads.size() <= 0) {
					Log.w(TAG, "AND we've got an empty saved threads map in shared preferences...");
				}
			}
			return areThingsInThreadSet;
		} catch (Exception e) {
			Log.e(TAG, "Error getting seen posts out of storage", e);
			return false;
		}
	}


}
