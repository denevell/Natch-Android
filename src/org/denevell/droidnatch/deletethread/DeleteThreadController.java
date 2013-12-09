package org.denevell.droidnatch.deletethread;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.deletethread.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;

public class DeleteThreadController implements Controller, ServiceCallbacks<DeletePostResourceReturnData> {
    private ResultsDisplayer<ListThreadsResource> mListThreadsView;
    private ServiceFetcher<DeletePostResourceReturnData> mService;
    private Controller mListThreadsController;

    public DeleteThreadController(
            ServiceFetcher<DeletePostResourceReturnData> service,
            ResultsDisplayer<ListThreadsResource> listThreadsView, 
            Controller listThreadsController) {
        mListThreadsView = listThreadsView;
        mService = service;
        mListThreadsController = listThreadsController;
    }

    @Override
    public void go() {
        if(mService!=null) {
            mService.setServiceCallbacks(this);
        }
    }
    
    public void startNetworkCall() {
        mService.go();
    }

    @Override
    public void onServiceSuccess(DeletePostResourceReturnData r) {
        if(mListThreadsView!=null) {
            mListThreadsView.stopLoading();
        }
        if(mListThreadsController!=null) {
            mListThreadsController.go();
        }
    }

    @Override
    public void onServiceFail(FailureResult r) {
        if(mListThreadsView!=null) {
            mListThreadsView.stopLoading();
        }
    }

}