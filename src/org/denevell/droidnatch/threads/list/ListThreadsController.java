package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

public class ListThreadsController 
    implements Controller, 
        ServiceCallbacks<ListThreadsResource>,
        OnPress<ThreadResource> {
    
    private ServiceFetcher<ListThreadsResource> mService;
    private ResultsDisplayer<ListThreadsResource> mResultsDisplayable;
    private OnPressObserver<ThreadResource> mOnPressObserver;
    private ScreenOpener mScreenCreator;

    public ListThreadsController(
            ServiceFetcher<ListThreadsResource> listThreadsService, 
            ResultsDisplayer<ListThreadsResource> resultsDisplayable,
            OnPressObserver<ThreadResource> onPressObserver, 
            ScreenOpener screenCreator) {
        mService = listThreadsService;
        mResultsDisplayable = resultsDisplayable;
        mOnPressObserver = onPressObserver;
        mScreenCreator = screenCreator;
    }
    
    public void go() {
        mService.setServiceCallbacks(this);
        mService.go();
        mResultsDisplayable.startLoading();
        mOnPressObserver.addOnPressListener(this);
    }

    @Override
    public void onServiceSuccess(ListThreadsResource r) {
        mResultsDisplayable.onSuccess(r);
        mResultsDisplayable.stopLoading();
    }

    @Override
    public void onServiceFail(FailureResult r) {
        mResultsDisplayable.onFail(r);
        mResultsDisplayable.stopLoading();
    }
    
    @Override
    public void onPress(ThreadResource obj) {
        mScreenCreator.openScreen(ListPostsFragment.class);
    }    
      
}
