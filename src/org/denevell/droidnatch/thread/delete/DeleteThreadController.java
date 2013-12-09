package org.denevell.droidnatch.thread.delete;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;

public class DeleteThreadController implements Controller, ServiceCallbacks<DeletePostResourceReturnData> {
    private ServiceFetcher<DeletePostResourceReturnData> mService;

    public DeleteThreadController(ServiceFetcher<DeletePostResourceReturnData> service) {
        mService = service;
    }

    @Override
    public void go() {
        //if(mService!=null) {
         //   mService.setServiceCallbacks(this);
        //}
    }
    
    public void startNetworkCall() {
        mService.go();
    }

    @Override
    public void onServiceSuccess(DeletePostResourceReturnData r) {
//        if(mListThreadsView!=null) {
//            mListThreadsView.stopLoading();
//        }
        //if(mListThreadsController!=null) {
         //   mListThreadsController.go();
        //}
    }

    @Override
    public void onServiceFail(FailureResult r) {
//        if(mListThreadsView!=null) {
//            mListThreadsView.stopLoading();
//        }
    }

}