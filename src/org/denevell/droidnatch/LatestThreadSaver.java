package org.denevell.droidnatch;

import java.util.HashMap;

public class LatestThreadSaver {

	public static HashMap<String, Boolean> seenThreads = new HashMap<String, Boolean>();

	/**
	 * @param id
	 * @return false is we've not seen anything...
	 */
	public static boolean isThisIdNew(String id) {
		return seenThreads.size()>0 
				&& !seenThreads.containsKey(id);
	}

}
