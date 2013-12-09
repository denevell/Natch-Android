package org.denevell.droidnatch.threads.list;

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

public class ListThreadsController 
    implements Controller, ServiceCallbacks<ListThreadsResource>,
               ContextItemSelected {
    
    private ServiceFetcher<ListThreadsResource> mService;
    private ResultsDisplayer<ListThreadsResource> mResultsDisplayable;
    private Context mAppContext;
    private ServiceFetcher<DeletePostResourceReturnData> mDeleteService;
    private VolleyRequest mDeleteRequest;
    private ListView mListView;

    public ListThreadsController(
            Context appContext,
            ServiceFetcher<ListThreadsResource> listThreadsService, 
            ResultsDisplayer<ListThreadsResource> resultsDisplayable,
            VolleyRequest deleteRequest,
            ServiceFetcher<DeletePostResourceReturnData> deleteService, 
            ListView listview) {
        mAppContext = appContext;
        mService = listThreadsService;
        mResultsDisplayable = resultsDisplayable;
        mDeleteRequest = deleteRequest;
        mDeleteService = deleteService;
        mListView = listview;
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
    
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        ThreadResource tr = (ThreadResource) mListView.getAdapter().getItem(index);

        // <NastyHack> 
        VolleyRequestGET volleyRequest = new VolleyRequestGET();
        volleyRequest.setUrl(mAppContext.getString(R.string.url_baseurl)
                +"post/thread/"
                +tr.getId()+"/0/1");
        BaseService<ListPostsResource> posts = new BaseService<ListPostsResource>(
                mAppContext, 
                volleyRequest,
                null,
                new JsonConverter(),
                new FailureFactory(),
                ListPostsResource.class);
        posts.setServiceCallbacks(new ServiceCallbacks<ListPostsResource>() {
            @Override
            public void onServiceSuccess(ListPostsResource r) {
                String postId = String.valueOf(r.getPosts().get(0).getId());
                String url = mAppContext.getString(R.string.url_baseurl) 
                        + mAppContext.getString(R.string.url_del); 
                mDeleteRequest.setUrl(url+postId);
                mDeleteService.go();
            }
            @Override public void onServiceFail(FailureResult r) { }
        });
        posts.go();
        // </NastyHack> 
        
        mDeleteService.setServiceCallbacks(new ServiceCallbacks<DeletePostResourceReturnData>() {
            @Override
            public void onServiceSuccess(DeletePostResourceReturnData r) {
                mResultsDisplayable.stopLoading();
                go();
            }
            @Override
            public void onServiceFail(FailureResult r) {
                mResultsDisplayable.stopLoading();
            }
        });
        mResultsDisplayable.startLoading();
        return true;
    }        

}
