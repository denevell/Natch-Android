package org.denevell.droidnatch.uitests.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.ShamefulStatics;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.android.apps.common.testing.ui.espresso.Espresso;

public class NatchAndroidInstrumentation extends ActivityInstrumentationTestCase2<MainPageActivity> {

    private static final String TAG = NatchAndroidInstrumentation.class.getSimpleName();

	@SuppressWarnings("deprecation")
	public NatchAndroidInstrumentation(String pkg, Class<MainPageActivity> activityClass) throws Exception {
        super(pkg, activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //TestUtils.SERVER_HOST = "192.168.43.103";
        TestUtils.deleteDb();

        // Register
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpput = new HttpPut("http://"+TestUtils.SERVER_HOST+":8080/Natch-REST-ForAutomatedTests/rest/user/");
        httpput.addHeader("Content-Type", "application/json");
        httpput.setEntity(new StringEntity("{\"username\":\"aaron\", \"password\":\"aaron\"}"));
        httpclient.execute(httpput);

        ShamefulStatics.setBasePath("http://"+TestUtils.SERVER_HOST+":8080/Natch-REST-ForAutomatedTests/rest/");

        ShamefulStatics.logout(getInstrumentation().getTargetContext());
        getActivity();
        disableAnimation(getInstrumentation().getContext());
        Espresso.registerIdlingResources(new VolleyIdlingResource("VolleyCalls"));
    }
    

    private void disableAnimation(Context c) {
      int permStatus = c.checkCallingOrSelfPermission("android.permission.SET_ANIMATION_SCALE");
      if (permStatus == PackageManager.PERMISSION_GRANTED) {
        if (reflectivelyDisableAnimation()) {
          Log.i(TAG, "All animations disabled.");
        } else {
          Log.i(TAG, "Could not disable animations.");
        }
      } else {
        Log.i(TAG, "Cannot disable animations due to lack of permission.");
      }
    }

    private boolean reflectivelyDisableAnimation() {
      try {
        Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
        Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
        Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
        Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
        Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
        Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales",
            float[].class);
        Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

        IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
        Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
        float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
        for (int i = 0; i < currentScales.length; i++) {
          currentScales[i] = 0.0f;
        }
        setAnimationScales.invoke(windowManagerObj, currentScales);
        return true;
      } catch (ClassNotFoundException cnfe) {
        Log.w(TAG, "Cannot disable animations reflectively.", cnfe);
      } catch (NoSuchMethodException mnfe) {
        Log.w(TAG, "Cannot disable animations reflectively.", mnfe);
      } catch (SecurityException se) {
        Log.w(TAG, "Cannot disable animations reflectively.", se);
      } catch (InvocationTargetException ite) {
        Log.w(TAG, "Cannot disable animations reflectively.", ite);
      } catch (IllegalAccessException iae) {
        Log.w(TAG, "Cannot disable animations reflectively.", iae);
      } catch (RuntimeException re) {
        Log.w(TAG, "Cannot disable animations reflectively.", re);
      }
      return false;
    }
    
}
