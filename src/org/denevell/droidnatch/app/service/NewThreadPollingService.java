package org.denevell.droidnatch.app.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class NewThreadPollingService extends Service {

	@SuppressWarnings("unused")
	private static final String TAG = NewThreadPollingService.class.getSimpleName();
	private NewThreadPollingHandler mServiceHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread("Sweet Buttery Jesus! This thread has a name!", android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		Looper serviceLooper = thread.getLooper();
		mServiceHandler = new NewThreadPollingHandler(serviceLooper, getApplicationContext());

		Notification notification = createLongRunningServiceNotification();
		startForeground(17, notification); // Because it can't be zero...
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Notification createLongRunningServiceNotification() {
		@SuppressWarnings("deprecation")
		Notification notification = new Notification.Builder(getApplicationContext())
			.setSmallIcon(android.R.drawable.stat_notify_chat)
			.setContentText("Forum, innit")
			.setContentTitle("I'll just wait for new threads.")
			.getNotification();
		notification.flags |= Notification.FLAG_NO_CLEAR;
		return notification;
	}

}
