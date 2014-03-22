package org.denevell.droidnatch.threads.list.uievents;

import java.util.List;

import org.denevell.droidnatch.SeenThreadsSaver;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.content.Context;
import android.util.Log;

public class StoreReferenceToLatestPostReceiver implements Receiver<ListThreadsResource> {

	private static final String TAG = StoreReferenceToLatestPostReceiver.class.getSimpleName();
	private Context mAppContext;
	
	public StoreReferenceToLatestPostReceiver(Context appContext) {
		mAppContext = appContext;
	}

	@Override
	public void success(ListThreadsResource result) {
		try {
			List<ThreadResource> threads = result.getThreads();
			SeenThreadsSaver.recoverFromDiskIfNeeded(mAppContext);
			for (ThreadResource threadResource : threads) {
				SeenThreadsSaver.addThread(threadResource.getId(), threadResource.getModification());
			}
			SeenThreadsSaver.commitToStorage(mAppContext);
		} catch (Exception e) {
			Log.e(TAG, "Problem parsing results for posts.");
		}
	}

	@Override public void fail(FailureResult r) { }
}
