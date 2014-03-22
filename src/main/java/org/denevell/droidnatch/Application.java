package org.denevell.droidnatch;

import org.denevell.droidnatch.app.push.GcmServerRegister;

import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bugsense.trace.BugSenseHandler;
import com.newfivefour.android.manchester.R;

public class Application extends android.app.Application {

    protected static final String TAG = "NatchApplication";
	private static RequestQueue requestQueue;
    private static Application appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
	BugSenseHandler.initAndStartSession(Application.this, "c7e316a1");
        appInstance = this;
        setBasePathIfEmpty();

        GcmServerRegister.registerForPushInBackground(this);
        boolean prefs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("push_notifications_off", false);
        Log.i(TAG, "They're: " + prefs);
    }

    /**
     * Set the base path of the services if they're not empty.
     * If they are NOT empty, it means some testing framework has set it
     * and so we should probably leave it alone.
     */
    private void setBasePathIfEmpty() {
        if(ShamefulStatics.getBasePath()==null || ShamefulStatics.getBasePath().isEmpty()) {
            ShamefulStatics.setBasePath(getString(R.string.url_baseurl));
        }
        //ShamefulStatics.setBasePath("http://169.254.8.212:8080/Natch-REST-ForAutomatedTests/rest/");
        //ShamefulStatics.setBasePath("http://denevell.org:8912/Natch-REST-ForAutomatedTests/rest/");
    }

    public synchronized static RequestQueue getRequestQueue () {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(appInstance);
        }
        return requestQueue;
    }
}
