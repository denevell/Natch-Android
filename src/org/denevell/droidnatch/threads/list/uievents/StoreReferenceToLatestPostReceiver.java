package org.denevell.droidnatch.threads.list.uievents;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.denevell.droidnatch.LatestThreadSaver;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;

import android.util.Log;

public class StoreReferenceToLatestPostReceiver implements Receiver<ListThreadsResource> {

	private static final String TAG = StoreReferenceToLatestPostReceiver.class.getSimpleName();
	private boolean mLookingAtFirstItemInList;

	public StoreReferenceToLatestPostReceiver(
			VolleyRequest<Void, ListThreadsResource> request) {
		try {
			String url = request.getRequest().getUrl();
			//Pattern pattern = Pattern.compile(".*/\\(\\.*\\)/\\d*$");
			Pattern pattern = Pattern.compile(".*(\\d)$");
			Matcher matcher = pattern.matcher(url);
			matcher.matches();
			String startIndexOfListUrl = matcher.group(1);
			int startIndex = Integer.valueOf(startIndexOfListUrl);
			if (startIndex == 0) {
				mLookingAtFirstItemInList = true;
			} else {
				mLookingAtFirstItemInList = false;
			}
		} catch (Exception e) {
			Log.e(TAG, "Problem parsing url for pagination index", e);
		}
	}

	@Override
	public void success(ListThreadsResource result) {
		if(mLookingAtFirstItemInList) {
			try {
				LatestThreadSaver.latestThreadId = result.getThreads().get(0).getId();
			} catch (Exception e) {
				Log.e(TAG, "Problem parsing results for first post.");
			}
		}
	}

	@Override public void fail(FailureResult r) { }
}
