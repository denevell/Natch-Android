package org.denevell.droidnatch.thread.delete;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver.OnLongPress;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;

public class DeleteThreadController implements Controller, 
               ServiceCallbacks<DeletePostResourceReturnData>,
               OnLongPress<ThreadResource> {
    @SuppressWarnings("unused")
    private static final String TAG = DeleteThreadController.class.getSimpleName();
    private ServiceFetcher<DeletePostResourceReturnData> mService;
    private Controller mListThreadsController;
    private Context mContext;
    private VolleyRequest<?> mDeleteRequest;
    private ResultsDisplayer<?> mListThreadsResultsDisplayable;
    private OnLongPressObserver<ThreadResource> mLongPressObserver;

    public DeleteThreadController(
            Context appContext,
            VolleyRequest<?> deleteRequest,
            ServiceFetcher<DeletePostResourceReturnData> service,
            Controller listThreadsController,
            ResultsDisplayer<?> listThreadsResultsDisplayable,
            OnLongPressObserver<ThreadResource> longPressObserver) {
        mLongPressObserver = longPressObserver;
        mContext = appContext;
        mDeleteRequest = deleteRequest;
        mService = service;
        mListThreadsController = listThreadsController;
        mListThreadsResultsDisplayable = listThreadsResultsDisplayable;
    }
    
    @Override
    public DeleteThreadController setup() {
        if(mLongPressObserver!=null) {
            mLongPressObserver.addOnLongClickListener(this);
        }
        if(mService!=null) {
           mService.setServiceCallbacks(this);
        }
        return this;
    }

    @Override
    public void go() {
    }
    
    public void startNetworkCall() {
        mService.go();
        if(mListThreadsResultsDisplayable!=null) {
            mListThreadsResultsDisplayable.startLoading();
        }
    }

    @Override
    public void onServiceSuccess(DeletePostResourceReturnData r) {
        if(mListThreadsResultsDisplayable!=null) {
            mListThreadsResultsDisplayable.stopLoading();
        }
        if(mListThreadsController!=null) {
            mListThreadsController.go();
        }
    }

    @Override
    public void onServiceFail(FailureResult r) {
        if(mListThreadsResultsDisplayable!=null) {
            mListThreadsResultsDisplayable.stopLoading();
        }
    }

    @Override
    public void onLongPress(ThreadResource obj, int optionId, String optionName) {
        String url = mContext.getString(R.string.url_baseurl) 
                + mContext.getString(R.string.url_del); 
        mDeleteRequest.setUrl(url+obj.getRootPostId());
        startNetworkCall();
    }

}