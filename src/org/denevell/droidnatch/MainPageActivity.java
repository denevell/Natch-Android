package org.denevell.droidnatch;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.addthread.AddThreadMapper;
import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.JsonConverter;
import org.denevell.droidnatch.app.baseclasses.ProgressBarIndicator;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.deletethread.DeleteThreadController;
import org.denevell.droidnatch.deletethread.DeleteThreadMapper;
import org.denevell.droidnatch.deletethread.entities.ListPostsResource;
import org.denevell.droidnatch.listthreads.ListThreadsMapper;
import org.denevell.droidnatch.listthreads.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import dagger.ObjectGraph;

public class MainPageActivity extends FragmentActivity {

    private static final String TAG = MainPageActivity.class.getSimpleName();
    @Inject @Named("listthreads") Controller mController;
    @Inject @Named("addthread") Controller mControllerAddThread;
    @Inject @Named("listthreads_listview") ListView mThreadsListView;
    @Inject @Named("deletethread") DeleteThreadController mDeleteThreadController;
    @Inject @Named("deletethread_service_request") VolleyRequest mVolleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_main);
            ObjectGraph.create(
                    new ListThreadsMapper(this),
                    new AddThreadMapper(this),
                    new DeleteThreadMapper(this),
                    new CommonMapper(this))
                    .inject(this);
            if(mThreadsListView!=null) {
                registerForContextMenu(mThreadsListView);
            }
            mController.go();
            mControllerAddThread.go();
            mDeleteThreadController.go();
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 0, 0, "Delete");
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean b= super.onContextItemSelected(item);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        ThreadResource tr = (ThreadResource) mThreadsListView.getAdapter().getItem(index);

        VolleyRequestGET volleyRequest = new VolleyRequestGET();
        volleyRequest.setUrl(getString(R.string.url_baseurl)+"post/thread/"+tr.getId()+"/0/1");

        BaseService<ListPostsResource> posts = new BaseService<ListPostsResource>(
                this, 
                volleyRequest,
                new ProgressBarIndicator(this),
                new JsonConverter(),
                new FailureFactory(),
                ListPostsResource.class);
        posts.setServiceCallbacks(new ServiceCallbacks<ListPostsResource>() {
            @Override
            public void onServiceSuccess(ListPostsResource r) {
                String postId = String.valueOf(r.getPosts().get(0).getId());
                String url = getString(R.string.url_baseurl) + getString(R.string.url_del); 
                mVolleyRequest.setUrl(url+postId);
                mDeleteThreadController.startNetworkCall();
            }
            @Override
            public void onServiceFail(FailureResult r) {
            }
        });
        posts.go();
        return b;
    }
    

}
