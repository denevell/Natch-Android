package org.denevell.droidnatch;

import java.io.IOException;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.entities.PushInput;
import org.denevell.droidnatch.threads.list.entities.SuccessOrError;
import org.denevell.natch.android.R;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Application extends android.app.Application {

    protected static final String TAG = "NatchApplication";
	private static RequestQueue requestQueue;
    private static Application appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        setBasePathIfEmpty();
        setAuthKeyIfEmpty();

        //Commented out until the alarm manager is toggled by a setting
	    //AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    //Intent i = new Intent(getApplicationContext(), NewThreadsBroadcastReceiver.class);
	    //PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
	    //am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 30000, pi);
        registerForPushInBackground();
        boolean prefs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("push_notifications_off", false);
        Log.i(TAG, "They're: " + prefs);
    }

	private void registerForPushInBackground() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(Application.this);
					String regId = gcm.register(getString(R.string.gcm_project));
					Log.i(TAG, regId);
					PushInput pushResource = new PushInput();
					pushResource.setId(regId);
					ServiceFetcher<PushInput, SuccessOrError> service = new ServiceBuilder<PushInput, SuccessOrError>()
							.entity(pushResource)
							.method(Request.Method.PUT)
							.url(Urls.getBasePath() + getString(R.string.url_push_add))
							.create(null, SuccessOrError.class);
					service.setServiceCallbacks(new ServiceCallbacks<SuccessOrError>() {
						@Override
						public void onServiceSuccess(SuccessOrError r) {
							Log.i(TAG, "Adding a push id was successful.");
						}
						@Override
						public void onServiceFail(FailureResult r) {
							Log.e(TAG, "Adding a push id seemed to have failed.");
						}
					});
					service.go();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}

    /**
     * Set the base path of the services if they're not empty.
     * If they are NOT empty, it means some testing framework has set it
     * and so we should probably leave it alone.
     */
    private void setBasePathIfEmpty() {
        if(Urls.getBasePath()==null || Urls.getBasePath().isEmpty()) {
            Urls.setBasePath(getString(R.string.url_baseurl));
        }
        //Urls.setBasePath("http://10.0.2.2:8080/Natch-REST-ForAutomatedTests/rest/");
    }

    private void setAuthKeyIfEmpty() {
        if(Urls.getAuthKey()==null || Urls.getAuthKey().isEmpty()) {
            Urls.setAuthKey(getString(R.string.services_session_id));
        }
    }

    public synchronized static RequestQueue getRequestQueue () {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(appInstance);
        }
        return requestQueue;
    }
}
