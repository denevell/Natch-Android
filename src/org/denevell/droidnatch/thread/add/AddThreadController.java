package org.denevell.droidnatch.thread.add;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TextEditable;
import org.denevell.droidnatch.app.interfaces.TextEditable.OnTextSubmitted;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;

public class AddThreadController implements Controller, OnTextSubmitted, ServiceCallbacks<AddPostResourceReturnData> {
    private TextEditable mTextEditable;
    private ResultsDisplayer<ListThreadsResource> mListThreadsView;
    private ServiceFetcher<AddPostResourceReturnData> mService;
    private Controller mListThreadsController;

    public AddThreadController(
            TextEditable textEditable, 
            ServiceFetcher<AddPostResourceReturnData> service,
            ResultsDisplayer<ListThreadsResource> listThreadsView, 
            Controller listThreadsController) {
        mTextEditable = textEditable;
        mListThreadsView = listThreadsView;
        mService = service;
        mListThreadsController = listThreadsController;
    }

    @Override
    public void go() {
        if(mTextEditable!=null) {
            mTextEditable.addTextInputCallack(this);
        }
        if(mService!=null) {
            mService.setServiceCallbacks(this);
        }
    }

    @Override
    public void onTextSubmitted(String textSubmitted) {
        if(mService!=null) {
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
        if(mListThreadsController!=null) {
            mTextEditable.setText("");
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