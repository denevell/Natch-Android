package org.denevell.droidnatch;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.denevell.natch.android.R;

public class Application extends android.app.Application {

    private static RequestQueue requestQueue;
    private static Application appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        setBasePathIfEmpty();
        setAuthKeyIfEmpty();
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
        Log.d("TEST", "entering getRequestQueue");
        Log.d("TEST", "Application instance: " + appInstance);
        Log.d("TEST", "requestQueue instance: " + requestQueue);

        return requestQueue;
    }
}
