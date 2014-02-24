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
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;

public class NewThreadPollingHandler extends Handler {
	private static final String TAG = NewThreadPollingService.class.getSimpleName();
	private ServiceFetcher<Void, ListThreadsResource> mServiceFetcher;
	private Context mAppContext;

	public NewThreadPollingHandler(Looper serviceLooper, Context appContext) {
		super(serviceLooper);
		mAppContext = appContext;
		mServiceFetcher = createListLatestPostService();
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		while(true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mServiceFetcher.setServiceCallbacks(new ServiceCallbacks<ListThreadsResource>() {
				@Override
				public void onServiceSuccess(ListThreadsResource r) {
					ThreadResource threadResource = r.getThreads().get(0);
					showNotificationIfNewThread(threadResource);
				}
				@Override public void onServiceFail(FailureResult r) {}
			});
			mServiceFetcher.go();
		}
	}

	private ServiceFetcher<Void, ListThreadsResource> createListLatestPostService() {
		String url = Urls.getBasePath() + mAppContext.getString(R.string.url_threads_latest);
		return new ServiceBuilder<Void, ListThreadsResource>()
			.url(url)
			.method(Request.Method.GET)
			.create(null, ListThreadsResource.class);
	}
	private void showNotificationIfNewThread(ThreadResource threadFromServer) {
		String latestId = threadFromServer.getId();
		Log.i(TAG, "Latest from service: " + latestId);
		if (latestId!= null && SeenThreadsSaver.isThisIdNew(mAppContext, latestId)) {
			SeenThreadsSaver.addThreadId(mAppContext, latestId);
			Intent i = new Intent(mAppContext, MainPageActivity.class);
			PendingIntent pi = PendingIntent.getActivity(mAppContext, 0, i, 0);	
			
			@SuppressWarnings("deprecation")
			Notification notification = new Notification.Builder(
					mAppContext)
					.setSmallIcon(android.R.drawable.stat_notify_chat)
					.setContentTitle("New thread")
					.setTicker("New thread: " + threadFromServer.getSubject())
					.setContentIntent(pi)
					.setAutoCancel(true)
					.setContentText(threadFromServer.getSubject())
					.getNotification();
			NotificationManager mgr = (NotificationManager) mAppContext.getSystemService(Context.NOTIFICATION_SERVICE);
			mgr.notify(0, notification);
		} else {
			Log.i(TAG, "We've already got the latest post apparently.");
		}
	}

}

