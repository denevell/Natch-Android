package org.denevell.droidnatch.uitests.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.google.gson.Gson;

/**
 * Created by user on 31/01/14.
 */
public class TestUtils {

    public static String SERVER_HOST = "10.0.2.2";

	public static void deleteDb() throws Exception {

        final String DBDriver = "org.postgresql.Driver";
        Class.forName(DBDriver);

        Properties connectionProps = new Properties();
        connectionProps.put("user", "denevell");
        connectionProps.put("password", "user");

        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://" +
                        SERVER_HOST +
                        ":5432" + "/testnatch",
                connectionProps);

        Statement statement = conn.createStatement();
        statement.execute("delete from thread_posts");
        statement.execute("delete from post_tags");
        statement.execute("delete from ThreadEntity");
        statement.execute("delete from PostEntity");
        statement.execute("delete from UserEntity");
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

	public static String addPostViaRest(Context c) throws Exception {
		return addPostViaRest(c, null);
	}

	@SuppressWarnings("serial")
	public static String addAnnouncement(Context c, String subject, String content) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpput = new HttpPut("http://"+SERVER_HOST+":8080/Natch-REST-ForAutomatedTests/rest/post/addthread");
        httpput.addHeader("Content-Type", "application/json");
        String addString = new Gson().toJson(new AddPostResourceInput(subject, content, new ArrayList<String>(){{ add("announcements");}}), AddPostResourceInput.class).toString();
		httpput.setEntity(new StringEntity(addString));
        httpput.setHeader("AuthKey", ShamefulStatics.getAuthKey(c));
        HttpResponse resp = httpclient.execute(httpput);
        BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        ThreadResource thread = new Gson().fromJson(br, AddPostResourceReturnData.class).getThread();
		return thread.getId();
	}
        	
	public static String addPostViaRest(Context c, String threadId) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpput = new HttpPut("http://"+SERVER_HOST+":8080/Natch-REST-ForAutomatedTests/rest/post/addthread");
        httpput.addHeader("Content-Type", "application/json");
        String addString = null;
        if(threadId!=null) {
        	addString = "{\"subject\":\"...\", \"content\":\"...\", \"threadId\": \""+threadId+"\"}";
        } else {
        	addString = "{\"subject\":\"...\", \"content\":\"...\"}";
        }
		httpput.setEntity(new StringEntity(addString));
        httpput.setHeader("AuthKey", ShamefulStatics.getAuthKey(c));
        HttpResponse resp = httpclient.execute(httpput);
        BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        return new Gson().fromJson(br, AddPostResourceReturnData.class).getThread().getId();
	}

}
