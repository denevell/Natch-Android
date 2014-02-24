package org.denevell.droidnatch.app.service;

import org.denevell.droidnatch.SeenThreadsSaver;
import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;

public class NewThreadsBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = NewThreadsBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(final Context context, Intent intent) {
		ServiceFetcher<Void, ListThreadsResource> mServiceFetcher = createListLatestPostService(context);
		mServiceFetcher.setServiceCallbacks(new ServiceCallbacks<ListThreadsResource>() {
					@Override
					public void onServiceSuccess(ListThreadsResource r) {
						ThreadResource threadResource = r.getThreads().get(0);
						showNotificationIfNewThread(context, threadResource);
					}

					@Override
					public void onServiceFail(FailureResult r) {
					}
				});
		mServiceFetcher.go();
	}

	private void showNotificationIfNewThread(Context context, ThreadResource threadFromServer) {
		String latestId = threadFromServer.getId();
		Log.i(TAG, "Latest from service: " + latestId);
		if (latestId!= null && SeenThreadsSaver.isThisIdNew(context, latestId)) {
			SeenThreadsSaver.addThreadId(context, latestId);
			Intent i = new Intent(context, MainPageActivity.class);
			PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);	
			
			@SuppressWarnings("deprecation")
			Notification notification = new Notification.Builder(
					context)
					.setSmallIcon(android.R.drawable.stat_notify_chat)
					.setContentTitle("New thread")
					.setTicker("New thread: " + threadFromServer.getSubject())
					.setContentIntent(pi)
					.setAutoCancel(true)
					.setContentText(threadFromServer.getSubject())
					.getNotification();
			NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mgr.notify(0, notification);
		} else {
			Log.i(TAG, "We've already got the latest post apparently.");
		}
	}

	private ServiceFetcher<Void, ListThreadsResource> createListLatestPostService(Context context) {
		String url = Urls.getBasePath() + context.getString(R.string.url_threads_latest);
		return new ServiceBuilder<Void, ListThreadsResource>().url(url)
				.method(Request.Method.GET)
				.create(null, ListThreadsResource.class);
	}

}
