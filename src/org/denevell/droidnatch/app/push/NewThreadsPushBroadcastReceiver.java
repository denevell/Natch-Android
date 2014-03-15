package org.denevell.droidnatch.app.push;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.SeenThreadsSaver;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.CutDownThreadResource;
import com.newfivefour.android.manchester.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

public class NewThreadsPushBroadcastReceiver extends BroadcastReceiver {

	public static String TAG = NewThreadsPushBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Got gcm broadcast.");

        boolean shouldntNotify = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.settings_push_notifs_off), false);
        if(shouldntNotify) return;

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				Log.i(TAG, "Received: " + extras.toString());
				String threadString =  extras.getString("thread");
				CutDownThreadResource thread = new Gson().fromJson(threadString, CutDownThreadResource.class);
				String threadAuthor = thread.getAuthor();
				String appAuthor = ShamefulStatics.getUsername(context);
				if(appAuthor == null || threadAuthor == null || !threadAuthor.equals(appAuthor)) {
					sendNotification(context, thread);
				} else {
					Log.w(TAG, "Not notifying the user about their own thread.");
				}
			} else {
				Log.w(TAG, "Got an odd message type" + messageType);
			}
		}
		setResultCode(Activity.RESULT_OK);
	}

	private void sendNotification(Context context, CutDownThreadResource threadFromServer) {
		String latestId = threadFromServer.getId();
		if (latestId!= null && SeenThreadsSaver.isThisIdNew(context, latestId)) {
			SeenThreadsSaver.addThreadId(context, latestId);
			Intent i = new Intent(context, MainPageActivity.class);
			i.putExtra(ListPostsFragment.BUNDLE_KEY_THREAD_ID, threadFromServer.getId());
			i.putExtra(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, threadFromServer.getSubject());
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);	
			
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

}