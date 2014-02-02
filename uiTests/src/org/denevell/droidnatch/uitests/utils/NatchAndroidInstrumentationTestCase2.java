package org.denevell.droidnatch.uitests.utils;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.Urls;

import java.io.InputStreamReader;

public class NatchAndroidInstrumentationTestCase2 extends ActivityInstrumentationTestCase2<MainPageActivity> {

    public NatchAndroidInstrumentationTestCase2(String pkg, Class<MainPageActivity> activityClass) throws Exception {
        super(pkg, activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestUtils.deleteDb();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.2.2:8080/Natch-REST-ForAutomatedTests/rest/user/login");
        httppost.addHeader("Content-Type", "application/json");
        httppost.setEntity(new StringEntity("{\"username\":\"aaron\", \"password\":\"aaron\"}"));
        HttpResponse response = httpclient.execute(httppost);
        LoginResourceReturnData login = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), LoginResourceReturnData.class);

        Urls.setBasePath("http://10.0.2.2:8080/Natch-REST-ForAutomatedTests/rest/");
        Urls.setsAuthKey(login.getAuthKey());

        getActivity();
        Espresso.registerIdlingResources(new VolleyIdlingResource("VolleyCalls"));
    }
}
