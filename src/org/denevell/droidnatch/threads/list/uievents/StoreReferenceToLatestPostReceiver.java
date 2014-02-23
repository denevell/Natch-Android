package org.denevell.droidnatch.threads.list.uievents;

import java.util.List;

import org.denevell.droidnatch.LatestThreadSaver;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.util.Log;

public class StoreReferenceToLatestPostReceiver implements Receiver<ListThreadsResource> {

	private static final String TAG = StoreReferenceToLatestPostReceiver.class.getSimpleName();

	@Override
	public void success(ListThreadsResource result) {
		try {
			List<ThreadResource> threads = result.getThreads();
			for (ThreadResource threadResource : threads) {
				LatestThreadSaver.seenThreads.put(threadResource.getId(), true);
			}
		} catch (Exception e) {
			Log.e(TAG, "Problem parsing results for posts.");
		}
	}

	@Override public void fail(FailureResult r) { }
}
