package org.denevell.droidnatch.posts.list;

import java.util.List;

import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment.ContextMenuItemHolder;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.posts.list.entities.ThreadResourceResourceToArrayList;
import org.denevell.droidnatch.posts.list.entities.ThreadResourceTotalAvailable;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library=true)
public class ListPostsMapper {
   
	private Activity mActivity;
	private String mTheadId;

    public ListPostsMapper(Activity activity, String threadId) {
        mActivity = activity;
        mTheadId = threadId;
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides @Singleton
    public ReceivingClickingAutopaginatingListView<ThreadResource, PostResource, List<PostResource>> provideListView(
    		final ServiceFetcher<Void, ThreadResource> request,
    		final ListPostsPaginationObject pagination) {
		final ReceivingClickingAutopaginatingListView listview = (ReceivingClickingAutopaginatingListView) mActivity.findViewById(R.id.list_posts_listview);

        final Button button = new Button(mActivity);
        button.setText("...Loading...");

        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(mActivity.getApplicationContext(), R.layout.posts_list_row);
        
		listview
			.setListAdapter(arrayAdapter)
			.setTypeAdapter(new ThreadResourceResourceToArrayList())
        	.setAvailableItems(new ThreadResourceTotalAvailable())
        	.setErrorViewId(R.id.list_view_service_error)
			.addOnPaginationFooterVisibleCallback(new Runnable() {
				@Override
				public void run() {
					pagination.paginate();
					String url = Urls.getBasePath() + mActivity.getString(R.string.url_posts);
					url = url.replace("{thread_id}", mTheadId);
					url += pagination.start + "/" + pagination.range;
					request.getRequest().setUrl(url);
					EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts(false));
				}
			})
        	.setPaginationView(button)
			.setKeyboardHider(new HideKeyboard());
    	listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				Callback callback = new Callback() {
					@Override
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						ReceivingClickingAutopaginatingListView.sCurrentActionMode = mode;
						return false;
					}
					
					@Override
					public void onDestroyActionMode(ActionMode mode) {
					}
					
					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						PostResource ob = (PostResource) listview.getAdapter().getItem(position);
            			String author = ob.getUsername();
                        String username = Urls.getUsername();
						if (!Urls.emptyUsername() &&  author!=null && !author.equals(username)) {
							mode.getMenuInflater().inflate(R.menu.not_yours_context_option_menu, menu);
						} else if (Urls.emptyUsername()) {
							mode.getMenuInflater().inflate(R.menu.please_login_context_option_menu, menu);
						} else if(position==0){
							mode.getMenuInflater().inflate(R.menu.list_posts_context_thread_option_menu, menu);
						} else {
							mode.getMenuInflater().inflate(R.menu.list_posts_context_post_option_menu, menu);
						}
						return true;
					}
					
					@Override
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						EventBus.getBus().post(new ContextMenuItemHolder(item, position));
						mode.finish();
						return true;
					}
				};
				((FragmentActivity)parent.getContext()).startActionMode(callback);
				return true;
				}
			});        

        return listview;
    } 

    
    @Provides @Singleton 
    public ServiceFetcher<Void, ThreadResource> providesService(
    		ListPostsPaginationObject pagination) {
        String url = Urls.getBasePath() + mActivity.getString(R.string.url_posts);
        url = url.replace("{thread_id}", mTheadId);
		ServiceFetcher<Void, ThreadResource> service
        	= new ServiceBuilder<Void, ThreadResource>()
        		.url(url)
        		.method(Request.Method.GET)
        		.pagination(pagination)
        		.create(mActivity, ThreadResource.class);
		return service;
	}

}