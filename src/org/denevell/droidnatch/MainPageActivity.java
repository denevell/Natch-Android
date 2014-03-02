package org.denevell.droidnatch;

import java.util.HashMap;
import java.util.Map;

import org.denevell.droidnatch.app.baseclasses.FragmentScreenOpener;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.natch.android.R;

import android.content.Intent;
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
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	setIntent(intent);
    }
    
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        FragmentScreenOpener sopner = new FragmentScreenOpener(this);

        Intent intent = getIntent();
    	if(intent!=null && intent.getExtras()!=null) {
    		Bundle extras = intent.getExtras();
    		Map<String, String> map = new HashMap<String, String>();
    		String threadId = extras.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID);
    		String threadName = extras.getString(ListPostsFragment.BUNDLE_KEY_THREAD_NAME);
    		if(threadId!=null && threadName!=null) {
    			map.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, threadId);
    			map.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, threadName);
    			if(isFragmentManagerEmpty()) {
    				gotoMainFragment(sopner);
    			} 
				sopner.openScreen(ListPostsFragment.class, map, true);
    			return;
    		}
    	}
    	if(isFragmentManagerEmpty()) {
    		gotoMainFragment(sopner);
    	}
    }

	private void gotoMainFragment(FragmentScreenOpener sopner) {
		sopner.openScreen(ListThreadsFragment.class, null, false);
	}

    private boolean isFragmentManagerEmpty() {
    	FragmentManager supportFragmentManager = getSupportFragmentManager();
    	if(supportFragmentManager.getFragments()==null || supportFragmentManager.getFragments().size()==0) {
    		return true;
    	} else {
    		return false;
    	}
    }

}
