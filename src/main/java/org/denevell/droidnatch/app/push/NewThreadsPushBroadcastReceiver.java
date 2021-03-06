package org.denevell.droidnatch.app.push;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.settings.SettingsActivity;
import org.denevell.droidnatch.threads.list.entities.CutDownThreadResource;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.newfivefour.android.manchester.R;

public class NewThreadsPushBroadcastReceiver extends BroadcastReceiver {

	public static String TAG = NewThreadsPushBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Got gcm broadcast.");
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
					Log.d(TAG, "Not notifying the user about their own thread.");
				}
			} else {
				Log.w(TAG, "Got an odd message type" + messageType);
			}
		}
		setResultCode(Activity.RESULT_OK);
	}

	private void sendNotification(Context context, CutDownThreadResource threadFromServer) {
        boolean wantsNewThreads = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.settings_push_new_threads_on), true);
        boolean wantsAnnouncements = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.settings_push_announcements_on), true);
        boolean isAnnouncement = threadFromServer.getTags()!=null && threadFromServer.getTags().contains("announcements");
        if(!wantsNewThreads && !isAnnouncement) {
        	Log.d(TAG, "We don't want notifications of normal threads. Bailing.");
        	return;
        }
        if(!wantsAnnouncements && isAnnouncement) {
        	Log.d(TAG, "We don't want notifications of announcements. Bailing.");
        	return;
        }

		String latestId = threadFromServer.getId();
		if (latestId!= null) {
			Intent i = new Intent(context, MainPageActivity.class);
			i.putExtra(ListPostsFragment.BUNDLE_KEY_THREAD_ID, threadFromServer.getId());
			i.putExtra(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, threadFromServer.getSubject());
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(context, threadFromServer.getId().hashCode(), i, PendingIntent.FLAG_ONE_SHOT);	
			
			Notification.Builder builder = new Notification.Builder(
					context)
					.setSmallIcon(android.R.drawable.stat_notify_chat)
					.setContentTitle((isAnnouncement) ? "Announcement" : "New thread")
					.setTicker(((isAnnouncement) ? "Announcement: " : "New thread: ") + threadFromServer.getSubject())
					.setContentIntent(pi)
					.setAutoCancel(true)
					.setContentText(threadFromServer.getSubject());
			@SuppressWarnings("deprecation")
			Notification notification = addActionToNotification(context, builder).getNotification();
			NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mgr.notify((isAnnouncement) ? 1 : 0, notification);
		} else {
			Log.i(TAG, "We've already got the latest post apparently.");
		}		
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private Notification.Builder addActionToNotification(
			Context context,
			Notification.Builder notification) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return notification.addAction(
					android.R.drawable.ic_lock_silent_mode,
					context.getString(R.string.turn_off_notifications), 
					PendingIntent.getActivity(
							context, 0, new Intent(context,
								SettingsActivity.class), 0));
		} else {
			return notification;
		}

	}

}