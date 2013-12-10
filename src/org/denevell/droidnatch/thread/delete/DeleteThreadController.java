package org.denevell.droidnatch.thread.delete;

import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.FailureFactory;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.JsonConverter;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.thread.delete.entities.ListPostsResource;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class DeleteThreadController implements Controller, 
               ContextItemSelected,
               ServiceCallbacks<DeletePostResourceReturnData> {
    private ServiceFetcher<DeletePostResourceReturnData> mService;
    private Controller mListThreadsController;
    private Context mContext;
    private VolleyRequest<DeletePostResourceReturnData> mDeleteRequest;
    private ListView mListView;
    private ResultsDisplayer<ListThreadsResource> mListThreadsResultsDisplayable;

    public DeleteThreadController(
            Context appContext,
            VolleyRequest<DeletePostResourceReturnData> deleteRequest,
            ListView listView,          
            ServiceFetcher<DeletePostResourceReturnData> service,
            Controller listThreadsController,
            ResultsDisplayer<ListThreadsResource> listThreadsResultsDisplayable) {
        mContext = appContext;
        mDeleteRequest = deleteRequest;
        mListView = listView;
        mService = service;
        mListThreadsController = listThreadsController;
        mListThreadsResultsDisplayable = listThreadsResultsDisplayable;
    }

    @Override
    public void go() {
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
    
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        ThreadResource tr = (ThreadResource) mListView.getAdapter().getItem(index);

        // <NastyHack> 
        VolleyRequestGET<ListPostsResource> volleyRequest = new VolleyRequestGET<ListPostsResource>();
        volleyRequest.setUrl(mContext.getString(R.string.url_baseurl)
                +"post/thread/"
                +tr.getId()+"/0/1");
        BaseService<ListPostsResource> posts = new BaseService<ListPostsResource>(
                mContext, 
                volleyRequest,
                null,
                new JsonConverter(),
                new FailureFactory(),
                ListPostsResource.class);
        posts.setServiceCallbacks(new ServiceCallbacks<ListPostsResource>() {
            @Override
            public void onServiceSuccess(ListPostsResource r) {
                String postId = String.valueOf(r.getPosts().get(0).getId());
                String url = mContext.getString(R.string.url_baseurl) 
                        + mContext.getString(R.string.url_del); 
                mDeleteRequest.setUrl(url+postId);
                startNetworkCall();
            }
            @Override public void onServiceFail(FailureResult r) { }
        });
        posts.go();
        // </NastyHack> 
        return true;
    }      

}