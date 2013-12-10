package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;

public class ListThreadsController 
    implements Controller, ServiceCallbacks<ListThreadsResource>
               {
    
    private ServiceFetcher<ListThreadsResource> mService;
    private ResultsDisplayer<ListThreadsResource> mResultsDisplayable;

    public ListThreadsController(
            ServiceFetcher<ListThreadsResource> listThreadsService, 
            ResultsDisplayer<ListThreadsResource> resultsDisplayable) {
        mService = listThreadsService;
        mResultsDisplayable = resultsDisplayable;
    }
    
    public void go() {
        mService.setServiceCallbacks(this);
        mService.go();
        mResultsDisplayable.startLoading();
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
    
      

}
