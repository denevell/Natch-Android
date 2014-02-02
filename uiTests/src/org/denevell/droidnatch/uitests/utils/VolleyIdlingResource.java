package org.denevell.droidnatch.uitests.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.android.apps.common.testing.ui.espresso.IdlingResource;

import org.denevell.droidnatch.Application;

import java.lang.reflect.Field;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

    public class VolleyIdlingResource implements IdlingResource {
        private static final String TAG = "VolleyIdlingResource";
        private final String resourceName;

        // written from main thread, read from any thread.
        private volatile ResourceCallback resourceCallback;

        private Field mCurrentRequests;
        private RequestQueue mVolleyRequestQueue;

        public VolleyIdlingResource(String resourceName) throws SecurityException, NoSuchFieldException {
            this.resourceName = checkNotNull(resourceName);

            mVolleyRequestQueue = Application.getRequestQueue();

            mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
            mCurrentRequests.setAccessible(true);
        }

        @Override
        public String getName() {
            return resourceName;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean isIdleNow() {
            try {
                Set<Request> set = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);
                int count = set.size();
                if (set != null) {

                    if (count == 0) {
                        Log.d(TAG, "Volley is idle now! with: " + count);
                        resourceCallback.onTransitionToIdle();
                    } else {
                        Log.d(TAG, "Not idle... " + count);
                    }
                    return count == 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }

    }
