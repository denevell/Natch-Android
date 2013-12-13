package org.denevell.droidnatch.posts.list;

import java.util.List;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.entities.ListPostsResource;
import org.denevell.droidnatch.posts.entities.PostResource;

public class ListPostsController 
    implements Controller,
    ServiceCallbacks<ListPostsResource> {

    private ServiceFetcher<ListPostsResource> mService;
    private ResultsDisplayer<List<PostResource>> mResultsPane;

    public ListPostsController(
            ServiceFetcher<ListPostsResource> service, 
            ResultsDisplayer<List<PostResource>> resultsPane) {
        mService = service;
        mResultsPane = resultsPane;
    }

    @Override
    public void go() {
        if(mService!=null) {
            mService.setServiceCallbacks(this);
            mService.go();
            mResultsPane.startLoading();
        }
    }

    @Override
    public void onServiceSuccess(ListPostsResource r) {
        if(mResultsPane!=null) {
            mResultsPane.stopLoading();
            mResultsPane.onSuccess(r.getPosts());
        }
    }

    @Override
    public void onServiceFail(FailureResult r) {
        if(mResultsPane!=null) {
            mResultsPane.stopLoading();
        }
        
    }

}
