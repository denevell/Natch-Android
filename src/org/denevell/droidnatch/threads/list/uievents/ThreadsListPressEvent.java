package org.denevell.droidnatch.threads.list.uievents;

import java.util.HashMap;

import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.util.Log;

public class ThreadsListPressEvent implements Runnable, OnPress<ThreadResource> {
    private static final String TAG = ThreadsListPressEvent.class.getSimpleName();
    private final ScreenOpener screenOpener;
    private final OnPressObserver<ThreadResource> onPressObserver;

    public ThreadsListPressEvent(ScreenOpener screenOpener,
            OnPressObserver<ThreadResource> onPressObserver) {
        this.screenOpener = screenOpener;
        this.onPressObserver = onPressObserver;
    }

    @Override
    public void run() {
        onPressObserver.addOnPressListener(this);
    }

    @Override
    public void onPress(ThreadResource obj) {
        Log.v(TAG, "Opening new list posts fragment");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getId());
        screenOpener.openScreen(ListPostsFragment.class, map);
    }
}