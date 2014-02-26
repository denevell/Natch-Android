package org.denevell.droidnatch;

import java.io.IOException;

import org.denevell.natch.android.R;

import android.os.AsyncTask;
import android.util.Log;

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

        //Commented out since I /think/ we'll be using an alarm manager from now on.
    	//Intent serviceIntent = new Intent(this, NewThreadPollingService.class);
		//startService(serviceIntent);
		
        //Commented out until the alarm manager is toggled by a setting
	    //AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    //Intent i = new Intent(getApplicationContext(), NewThreadsBroadcastReceiver.class);
	    //PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
	    //am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 30000, pi);
        registerInBackground();
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                	GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(Application.this);
                    String regid = gcm.register(getString(R.string.gcm_project));
                    Log.i(TAG, regid);
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
