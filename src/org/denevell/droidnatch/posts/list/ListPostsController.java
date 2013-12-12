package org.denevell.droidnatch.posts.list;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.entities.ListPostsResource;

public class ListPostsController 
    implements Controller,
    ServiceCallbacks<ListPostsResource> {

    private ServiceFetcher<ListPostsResource> mService;

    public ListPostsController(ServiceFetcher<ListPostsResource> service) {
        mService = service;
    }

    @Override
    public void go() {
        if(mService!=null) {
            mService.setServiceCallbacks(this);
            mService.go();
        }
    }

    @Override
    public void onServiceSuccess(ListPostsResource r) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onServiceFail(FailureResult r) {
        // TODO Auto-generated method stub
        
    }

}
