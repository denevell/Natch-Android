package org.denevell.droidnatch;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
        if(getResources().getBoolean(R.bool.trust_all_certs)) {
        	NukeSSLCerts.nuke();
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
    
    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";
     
        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] { 
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
            	        /* Create a new array with room for an additional trusted certificate. */
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];  
                            return myTrustedAnchors;
                        }
            						
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            		  
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
                };
    			
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) { 
                // pass
            }
        }
    }    
    
}
