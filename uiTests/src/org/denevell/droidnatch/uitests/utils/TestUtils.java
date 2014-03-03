package org.denevell.droidnatch.uitests.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.denevell.droidnatch.Urls;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;

/**
 * Created by user on 31/01/14.
 */
public class TestUtils {

    public static void deleteDb() throws Exception {

        final String DBDriver = "org.postgresql.Driver";
        Class.forName(DBDriver);

        Properties connectionProps = new Properties();
        connectionProps.put("user", "denevell");
        connectionProps.put("password", "user");

        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://" +
                        "10.0.2.2" +
                        ":5432" + "/testnatch",
                connectionProps);

        Statement statement = conn.createStatement();
        statement.execute("delete from thread_posts");
        statement.execute("delete from post_tags");
        statement.execute("delete from ThreadEntity");
        statement.execute("delete from PostEntity");
    }

	public static void toggleOrientationChange(Activity act, Instrumentation instru) {
		int orientation = act.getRequestedOrientation();
        if(orientation==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        	act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
        	act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        instru.waitForIdleSync();
	}
        	
	public static void addPostViaRest() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpput = new HttpPut("http://10.0.2.2:8080/Natch-REST-ForAutomatedTests/rest/post/addthread");
        httpput.addHeader("Content-Type", "application/json");
        httpput.setEntity(new StringEntity("{\"subject\":\"...\", \"content\":\"...\"}"));
        httpput.setHeader("AuthKey", Urls.getAuthKey());
        HttpResponse resp = httpclient.execute(httpput);
	}

}
