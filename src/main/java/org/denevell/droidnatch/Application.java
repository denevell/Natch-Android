package org.denevell.droidnatch;

import org.denevell.droidnatch.app.push.GcmServerRegister;
import org.denevell.droidnatch.app.utils.AndroidUtils;
import org.denevell.droidnatch.app.visited_db.VisitedPostsTable;

import android.content.Context;
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
	private static VisitedPostsTable sVisitedPostsTable;

    @Override
    public void onCreate() {
        super.onCreate();
        if(getResources().getBoolean(R.bool.trust_all_certs)) {
        	AndroidUtils.NukeSSLCerts.nuke();
        }
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
        //ShamefulStatics.setBasePath("http://192.168.43.103:8080/Natch-REST-ForAutomatedTests/rest/");
        //ShamefulStatics.setBasePath("http://denevell.org:8912/Natch-REST-ForAutomatedTests/rest/");
    }

    public synchronized static RequestQueue getRequestQueue () {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(appInstance);
        }
        return requestQueue;
    }    
    
    public static VisitedPostsTable getVisitedPostsDatabase(Context context) {
    	if(sVisitedPostsTable==null) {
    		sVisitedPostsTable = new VisitedPostsTable(context);
    		sVisitedPostsTable.open();
    	}
		return sVisitedPostsTable;
    }
    
}
