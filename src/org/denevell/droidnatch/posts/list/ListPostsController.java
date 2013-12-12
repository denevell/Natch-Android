package org.denevell.droidnatch.posts.list;

import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

public class ListPostsController 
    implements Controller, OnPress<ThreadResource> {

    private OnPressObserver<ThreadResource> mOnPressObserver;
    private ScreenOpener mScreenCreator;

    public ListPostsController(OnPressObserver<ThreadResource> onPressObserver, 
            ScreenOpener screenCreator) {
        mOnPressObserver = onPressObserver;
        mScreenCreator = screenCreator;
    }

    @Override
    public void go() {
        mOnPressObserver.addOnLongClickListener(this);
    }

    @Override
    public void onPress(ThreadResource obj) {
        mScreenCreator.openScreen(ListPostsFragment.class);
    }

}
