package org.denevell.droidnatch.app.push;

import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.entities.PushInput;
import org.denevell.droidnatch.threads.list.entities.SuccessOrError;
import com.newfivefour.android.manchester.R;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmServerRegister {

	protected static final String TAG = GcmServerRegister.class.getSimpleName();

	public static String sRegSuccessful = "Not yet attempted";

	public static void registerForPushInBackground(final Context appContext) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(appContext);
					String regId = gcm.register(appContext.getString(R.string.gcm_project));
					Log.i(TAG, regId);
					PushInput pushResource = new PushInput();
					pushResource.setId(regId);
					ServiceFetcher<PushInput, SuccessOrError> service = new ServiceBuilder<PushInput, SuccessOrError>()
							.entity(pushResource)
							.method(Request.Method.PUT)
							.url(ShamefulStatics.getBasePath() + appContext.getString(R.string.url_push_add))
							.createJson(null, SuccessOrError.class);
					service.setServiceCallbacks(new ServiceCallbacks<SuccessOrError>() {
						@Override
						public void onServiceSuccess(SuccessOrError r) {
							sRegSuccessful = "Successful";
							Log.i(TAG, "Adding a push id was successful.");
						}
						@Override
						public void onServiceFail(FailureResult r) {
							sRegSuccessful = "Failed";
							Log.e(TAG, "Adding a push id seemed to have failed.");
						}
					});
					service.go();
				} catch (Exception ex) {
					sRegSuccessful = "Failed";
					ex.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
		sRegSuccessful = "Attempting";
	}	
}
