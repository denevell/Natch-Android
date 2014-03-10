package org.denevell.droidnatch.threads.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.PaginationMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.PaginationMapper.ListThreadsPaginationObject;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment.ContextMenuItemHolder;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.utils.AndroidUtils;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResourceTotalAvailable;
import org.denevell.droidnatch.threads.list.entities.ListThreadsToList;
import org.denevell.droidnatch.threads.list.entities.LoginResourceInput;
import org.denevell.droidnatch.threads.list.entities.LoginResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.LogoutResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.uievents.AddThreadViewActivator;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete=false, library=true)
public class ListThreadsMapper {
    


	public static final String PROVIDES_LIST_THREADS_LIST_CLICK = "list_threads_list_click";
    private static final String TAG = ListThreadsMapper.class.getSimpleName();
    private Activity mActivity;

    public ListThreadsMapper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides @Singleton
    public ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> providesListView(
    		final ServiceFetcher<Void, ListThreadsResource> listService,
    		final ListThreadsPaginationObject pagination,
    		OnPress<ThreadResource> onPressListener,
    		Context appContext
    		) {
        final ReceivingClickingAutopaginatingListView listView = (ReceivingClickingAutopaginatingListView) mActivity.findViewById(R.id.threads_listview);

        ListThreadsArrayAdapter listAdapter = new ListThreadsArrayAdapter(appContext, R.layout.threads_list_row);

		listView
			.setListAdapter(listAdapter)
        	.setTypeAdapter(new ListThreadsToList())
			.setPaginationView(R.layout.pagination_button_generic)
            .setErrorViewId(R.id.list_view_service_error)
			.addOnPaginationFooterVisibleCallback(new Runnable() {
				@Override public void run() {
					pagination.paginate();
					String url = ShamefulStatics.getBasePath()
							+ mActivity.getString(R.string.url_threads) + ""
							+ pagination.start + "/" + pagination.range;
					listService.getRequest().setUrl(url);
					EventBus.getBus().post(new ListThreadsViewStarter.CallControllerListThreads());
				}})
			.setAvailableItems(new ListThreadsResourceTotalAvailable())
        	.setKeyboardHider(new HideKeyboard());
		
		listView.addHeaderView(new AddThreadViewActivator(mActivity, null));

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				if(position==0) return false; // This is the add thread header;
				Callback callback = new CallbackImplementation(listView, position);
				((FragmentActivity)parent.getContext()).startActionMode(callback);
				return true;
				}
			});

        listView.addOnPressListener(onPressListener);
        
        return listView;
    }

	@Provides
	@Singleton
	public OnPress<ThreadResource> providesOnListClickAction(
			final ScreenOpener screenOpener,
			final ListPostsPaginationObject pagination) {
		OnPress<ThreadResource> onPress = new OnPress<ThreadResource>() {
			@Override
			public void onPress(ThreadResource obj) {
				Log.v(TAG, "Opening new list posts fragment");
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getId());
				hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, obj.getSubject());
				pagination.range = pagination.defaultRange;
				screenOpener.openScreen(ListPostsFragment.class, hm, true);
			}
		};
		return onPress;
	}
    
    @Provides @Singleton 
    public ServiceFetcher<Void, ListThreadsResource> provideListThreadsService(
    		ListThreadsPaginationObject pagination) {
		String url = ShamefulStatics.getBasePath()+mActivity.getString(R.string.url_threads);
		ServiceFetcher<Void, ListThreadsResource> listThreadsService
        	= new ServiceBuilder<Void, ListThreadsResource>()
        		.url(url)
        		.method(Request.Method.GET)
        		.pagination(pagination)
        		.create(mActivity, ListThreadsResource.class);
		return listThreadsService;
	}

    @Provides @Singleton 
    public ServiceFetcher<Void, LogoutResourceReturnData> provideLogoutService() {
		String url = ShamefulStatics.getBasePath()+mActivity.getString(R.string.url_logout);
		ServiceFetcher<Void, LogoutResourceReturnData> listThreadsService
        	= new ServiceBuilder<Void, LogoutResourceReturnData>()
        		.url(url)
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override
					public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", ShamefulStatics.getAuthKey(mActivity.getApplicationContext()));
					}
				})
        		.method(Request.Method.DELETE)
        		.create(mActivity, LogoutResourceReturnData.class);
		return listThreadsService;
	}

    @Provides @Singleton 
    public ServiceFetcher<LoginResourceInput, LoginResourceReturnData> providesLoginService(
    		ListThreadsPaginationObject pagination) {
		String url = ShamefulStatics.getBasePath() + mActivity.getString(R.string.url_login);
		return new ServiceBuilder<LoginResourceInput, LoginResourceReturnData>()
				.url(url).method(Request.Method.POST)
				.entity(new LoginResourceInput())
				.create(mActivity, LoginResourceReturnData.class);
	}

	private final class CallbackImplementation implements Callback {
		private final ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> listView;
		private final int position;

		private CallbackImplementation(ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> listView, int position) {
			this.listView = listView;
			this.position = position;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			ReceivingClickingAutopaginatingListView.sCurrentActionMode = mode;
			return false;
		}

		@Override public void onDestroyActionMode(ActionMode mode) { }

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			if(position==0) return false; // 0 is the header
			try {
				ThreadResource ob = (ThreadResource) listView.getAdapter() .getItem(position);
				String author = ob.getAuthor();
				String username = ShamefulStatics.getUsername(mActivity .getApplicationContext());
				if (!ShamefulStatics.emptyUsername(mActivity.getApplicationContext())
						&& author != null
						&& !author.equals(username)) {
					mode.getMenuInflater().inflate(
							R.menu.not_yours_context_option_menu, menu);
				} else if (ShamefulStatics.emptyUsername(mActivity .getApplicationContext())) {
					mode.getMenuInflater().inflate( R.menu.please_login_context_option_menu, menu);
				} else {
					mode.getMenuInflater().inflate( R.menu.list_threads_context_option_menu, menu);
				}
				return true;
			} catch (Exception e) {
				Log.d(TAG, "Couldnt open action menu", e);
				return false;
			}
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			EventBus.getBus().post(new ContextMenuItemHolder(item, position));
			mode.finish();
			return true;
		}
	}
}
