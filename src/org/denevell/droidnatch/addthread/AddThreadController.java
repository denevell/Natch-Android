package org.denevell.droidnatch.addthread;

import org.denevell.droidnatch.addthread.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TextEditable;
import org.denevell.droidnatch.app.interfaces.TextEditable.OnTextInputted;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;

public class AddThreadController implements Controller, OnTextInputted, ServiceCallbacks<AddPostResourceReturnData> {
    private TextEditable mTextEditable;
    private ResultsDisplayer<ListThreadsResource> mListThreadsView;
    private ServiceFetcher<AddPostResourceReturnData> mService;

    public AddThreadController(
            TextEditable textEditable, 
            ResultsDisplayer<ListThreadsResource> listThreadsView, 
            ServiceFetcher<AddPostResourceReturnData> service) {
        mTextEditable = textEditable;
        mListThreadsView = listThreadsView;
        mService = service;
    }

    @Override
    public void go() {
        if(mTextEditable!=null) {
            mTextEditable.setTextInputCallack(this);
        }
    }

    @Override
    public void onTextSubmitted(String textSubmitted) {
        if(mService!=null) {
            mService.setServiceCallbacks(this);
            mService.go();
        }
        if(mListThreadsView!=null) {
            mListThreadsView.startLoading();
        }
    }

    @Override
    public void onServiceSuccess(AddPostResourceReturnData r) {
        if(mListThreadsView!=null) {
            mListThreadsView.stopLoading();
        }
    }

    @Override
    public void onServiceFail(FailureResult r) {
        if(mListThreadsView!=null) {
            mListThreadsView.stopLoading();
        }
    }

}