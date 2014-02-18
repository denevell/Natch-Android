package org.denevell.droidnatch.uitests.utils;

import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.uitests.pageobjects.LoginPO;
import org.denevell.droidnatch.uitests.utils.LoginResourceReturnData;
import org.denevell.droidnatch.uitests.utils.TestUtils;
import org.denevell.droidnatch.uitests.utils.VolleyIdlingResource;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.gson.Gson;

public class NatchAndroidInstrumentationWithLogin extends ActivityInstrumentationTestCase2<MainPageActivity> {

    @SuppressWarnings("deprecation")
	public NatchAndroidInstrumentationWithLogin(String pkg, Class<MainPageActivity> activityClass) throws Exception {
        super(pkg, activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestUtils.deleteDb();

        // Register
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpput = new HttpPut("http://10.0.2.2:8080/Natch-REST-ForAutomatedTests/rest/user/");
        httpput.addHeader("Content-Type", "application/json");
        httpput.setEntity(new StringEntity("{\"username\":\"aaron\", \"password\":\"aaron\"}"));
        httpclient.execute(httpput);

        Urls.setBasePath("http://10.0.2.2:8080/Natch-REST-ForAutomatedTests/rest/");

        getActivity();
        Espresso.registerIdlingResources(new VolleyIdlingResource("VolleyCalls"));
        new LoginPO().loginWithDefaultCredential();
    }
}
