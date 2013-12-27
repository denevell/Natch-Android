package org.denevell.droidnatch;

import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;

public class MainPageActivity extends FragmentActivity {

    private static final String TAG = MainPageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_main);
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
    }
    
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if(supportFragmentManager.getFragments()==null || 
                    supportFragmentManager.getFragments().size()==0) {
                Log.v(TAG, "Starting initial fragment");
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_holder, new ListThreadsFragment())
                    .commit();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
    }
    
}
