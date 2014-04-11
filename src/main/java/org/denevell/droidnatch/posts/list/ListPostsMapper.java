package org.denevell.droidnatch.posts.list;

import java.util.List;

import javax.inject.Singleton;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.PaginationMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment.ContextMenuItemHolder;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.posts.list.entities.ThreadResourceResourceToArrayList;
import org.denevell.droidnatch.posts.list.entities.ThreadResourceTotalAvailable;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.android.volley.Request;
import com.newfivefour.android.manchester.R;

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

        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(mActivity, R.layout.posts_list_row);
        
		listview
			.setListAdapter(arrayAdapter)
			.setTypeAdapter(new ThreadResourceResourceToArrayList())
        	.setAvailableItems(new ThreadResourceTotalAvailable())
        	.setErrorViewId(R.id.list_view_service_error)
			.addOnPaginationFooterVisibleCallback(new Runnable() {
				@Override
				public void run() {
					pagination.paginate();
					String url = ShamefulStatics.getBasePath() + mActivity.getString(R.string.url_posts);
					url = url.replace("{thread_id}", mTheadId);
					url += pagination.start + "/" + pagination.range;
					request.setUrl(url);
					EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts(false));
				}
			})
			.setPaginationView(R.layout.pagination_button_generic)
			.setKeyboardHider(new HideKeyboard());
    	listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				Callback callback = new ActionMenuImplementation(listview, position, view);
				((FragmentActivity)parent.getContext()).startActionMode(callback);
				return true;
				}
			});        

        return listview;
    } 

    
    @Provides @Singleton 
    public ServiceFetcher<Void, ThreadResource> providesService(
    		ListPostsPaginationObject pagination) {
        String url = ShamefulStatics.getBasePath() + mActivity.getString(R.string.url_posts);
        url = url.replace("{thread_id}", mTheadId);
		ServiceFetcher<Void, ThreadResource> service
        	= new ServiceBuilder<Void, ThreadResource>()
        		.url(url)
        		.method(Request.Method.GET)
        		.pagination(pagination)
        		.createJson(mActivity, ThreadResource.class);
		return service;
	}
    
  	private final class ActionMenuImplementation implements Callback {
  		@SuppressWarnings("rawtypes")
  		private final ReceivingClickingAutopaginatingListView mListview;
  		private final int mPosition;
		private View mSelectedView;

  		@SuppressWarnings("rawtypes")
  		private ActionMenuImplementation(
  				ReceivingClickingAutopaginatingListView listview, 
  				int position, 
  				View selectedView) {
  			this.mListview = listview;
  			this.mPosition = position;
  			this.mSelectedView = selectedView;
  			this.mSelectedView.setSelected(true);
  		}

  		@Override
  		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
  			ReceivingClickingAutopaginatingListView.sCurrentActionMode = mode;
  			return false;
  		}

  		@Override
  		public void onDestroyActionMode(ActionMode mode) {
  			if(this.mSelectedView!=null) this.mSelectedView.setSelected(false);
  		}

  		@Override
  		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
  			PostResource ob = (PostResource) mListview.getAdapter().getItem(mPosition);
  			String author = ob.getUsername();
  		    String username = ShamefulStatics.getUsername(mActivity.getApplicationContext());
  			if (!ShamefulStatics.emptyUsername(mActivity.getApplicationContext()) &&  author!=null && !author.equals(username)) {
  				mode.getMenuInflater().inflate(R.menu.not_yours_context_option_menu, menu);
  			} else if (ShamefulStatics.emptyUsername(mActivity.getApplicationContext())) {
  				mode.getMenuInflater().inflate(R.menu.please_login_context_option_menu, menu);
  			} else if(mPosition==0){
  				mode.getMenuInflater().inflate(R.menu.list_posts_context_thread_option_menu, menu);
  			} else {
  				mode.getMenuInflater().inflate(R.menu.list_posts_context_post_option_menu, menu);
  			}
  			return true;
  		}

  		@Override
  		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
  			EventBus.getBus().post(new ContextMenuItemHolder(item, mPosition, mListview));
  			mode.finish();
  			return true;
  		}
  	}

  	public static final class NotLoggedInActionMenuImplementation implements Callback {
  		
  		private View selectedView;

  		@Override
  		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
  			ReceivingClickingAutopaginatingListView.sCurrentActionMode = mode;
  			return false;
  		}

  		@Override
  		public void onDestroyActionMode(ActionMode mode) {
  			if(this.selectedView!=null) this.selectedView.setSelected(false);
  		}

  		@Override
  		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
  			mode.getMenuInflater().inflate(R.menu.please_login_context_option_menu, menu);
  			return true;
  		}

  		@Override
  		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
  			EventBus.getBus().post(new ObservableFragment.OptionMenuItemHolder(item));
  			mode.finish();
  			return true;
  		}
  	}


}