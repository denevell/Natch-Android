package org.denevell.droidnatch.thread.delete;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedHolder;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class DeleteThreadController implements Controller, 
               ContextItemSelected,
               ServiceCallbacks<DeletePostResourceReturnData> {
    private static final String TAG = DeleteThreadController.class.getSimpleName();
    private ServiceFetcher<DeletePostResourceReturnData> mService;
    private Controller mListThreadsController;
    private Context mContext;
    private VolleyRequest<?> mDeleteRequest;
    private ListView mListView;
    private ResultsDisplayer<?> mListThreadsResultsDisplayable;
    private ContextItemSelectedHolder mContextSelectedHolder;

    public DeleteThreadController(
            Context appContext,
            VolleyRequest<?> deleteRequest,
            ListView listView,          
            ServiceFetcher<DeletePostResourceReturnData> service,
            Controller listThreadsController,
            ResultsDisplayer<?> listThreadsResultsDisplayable, 
            ContextItemSelectedHolder contextSelectedHolder) {
        mContext = appContext;
        mDeleteRequest = deleteRequest;
        mListView = listView;
        mService = service;
        mListThreadsController = listThreadsController;
        mListThreadsResultsDisplayable = listThreadsResultsDisplayable;
        mContextSelectedHolder = contextSelectedHolder;
    }

    @Override
    public void go() {
        if(mContextSelectedHolder!=null) {
            mContextSelectedHolder.addContextItemSelectedCallback(this);
        }
        if(mService!=null) {
           mService.setServiceCallbacks(this);
        }
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
    public boolean onContextItemSelected(MenuItem item) {
        try {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            ThreadResource tr = (ThreadResource) mListView.getAdapter().getItem(index);
    
            String url = mContext.getString(R.string.url_baseurl) 
                    + mContext.getString(R.string.url_del); 
            mDeleteRequest.setUrl(url+tr.getRootPostId());
            startNetworkCall();
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process oncontextitemselected event.", e);
        }
        return true;
    }      

}