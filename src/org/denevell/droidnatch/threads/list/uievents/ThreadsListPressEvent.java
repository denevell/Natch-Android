package org.denevell.droidnatch.threads.list.uievents;

import java.util.Map;

import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.util.Log;

public class ThreadsListPressEvent implements OnPress<ThreadResource> {
    private static final String TAG = ThreadsListPressEvent.class.getSimpleName();
    private final ScreenOpener screenOpener;
    private final OnPressObserver<ThreadResource> onPressObserver;
    private Map<String, String> mPassedValuesMap;

    public ThreadsListPressEvent(ScreenOpener screenOpener,
            OnPressObserver<ThreadResource> onPressObserver,
            Map<String, String> passedValuesMap) {
        this.mPassedValuesMap = passedValuesMap;
        this.screenOpener = screenOpener;
        this.onPressObserver = onPressObserver;
        this.onPressObserver.addOnPressListener(this);
    }

    @Override
    public void onPress(ThreadResource obj) {
        Log.v(TAG, "Opening new list posts fragment");
        mPassedValuesMap.clear();
        mPassedValuesMap.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getId());
        mPassedValuesMap.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, obj.getSubject());
        screenOpener.openScreen(ListPostsFragment.class, mPassedValuesMap);
    }
}