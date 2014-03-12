package org.denevell.droidnatch;

import java.util.HashMap;
import java.util.Map;

import org.denevell.droidnatch.app.baseclasses.FragmentScreenOpener;
import org.denevell.droidnatch.app.utils.AndroidUtils;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.natch.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

public class MainPageActivity extends FragmentActivity {

    private static final String TAG = MainPageActivity.class.getSimpleName();
	private FragmentScreenOpener mOpener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            mOpener = new FragmentScreenOpener(this);
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

        Intent intent = getIntent();
    	if(intent!=null && intent.getExtras()!=null) {
    		gotoThreadPageFromIntent(intent);
    	}
    	if(AndroidUtils.isFragmentManagerEmpty(getSupportFragmentManager())) {
    		gotoMainFragment(mOpener);
    	}
    }

	private void gotoThreadPageFromIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		Map<String, String> map = new HashMap<String, String>();
		String threadId = extras.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID);
		String threadName = extras.getString(ListPostsFragment.BUNDLE_KEY_THREAD_NAME);
		if(threadId!=null && threadName!=null) {
			map.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, threadId);
			map.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, threadName);
			if(AndroidUtils.isFragmentManagerEmpty(getSupportFragmentManager())) {
				gotoMainFragment(mOpener);
			} 
			mOpener.openScreen(ListPostsFragment.class, map, true);
			setIntent(null);
			return;
		}
		setIntent(null);
	}

	private void gotoMainFragment(FragmentScreenOpener sopner) {
		sopner.openScreen(ListThreadsFragment.class, null, false);
	}
    
	
}
