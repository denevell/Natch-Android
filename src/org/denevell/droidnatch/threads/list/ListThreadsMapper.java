package org.denevell.droidnatch.threads.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.AppWideMapper.ListThreadsPaginationObject;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResourceTotalAvailable;
import org.denevell.droidnatch.threads.list.entities.ListThreadsToList;
import org.denevell.droidnatch.threads.list.entities.LoginResourceInput;
import org.denevell.droidnatch.threads.list.entities.LoginResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.LogoutResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

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
        ReceivingClickingAutopaginatingListView listView = (ReceivingClickingAutopaginatingListView) mActivity.findViewById(R.id.threads_listview);

        Button button = new Button(mActivity);
        button.setText("...Loading...");

        ListThreadsArrayAdapter listAdapter = new ListThreadsArrayAdapter(appContext, R.layout.threads_list_row);

		listView
			.setListAdapter(listAdapter)
        	.setTypeAdapter(new ListThreadsToList())
			.setPaginationView(button)
			.setContenxtMenuListener(new ListThreadsContextMenu(listAdapter))
        	.setErrorView(R.layout.list_view_service_error)
			.addOnPaginationFooterVisibleCallback(new Runnable() {
				@Override public void run() {
					pagination.paginate();
					String url = Urls.getBasePath()
							+ mActivity.getString(R.string.url_threads) + ""
							+ pagination.start + "/" + pagination.range;
					listService.getRequest().setUrl(url);
					EventBus.getBus().post(new ListThreadsViewStarter.CallControllerListThreads());
				}})
			.setAvailableItems(new ListThreadsResourceTotalAvailable())
        	.setKeyboardHider(new HideKeyboard());

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
		String url = Urls.getBasePath()+mActivity.getString(R.string.url_threads);
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
		String url = Urls.getBasePath()+mActivity.getString(R.string.url_logout);
		ServiceFetcher<Void, LogoutResourceReturnData> listThreadsService
        	= new ServiceBuilder<Void, LogoutResourceReturnData>()
        		.url(url)
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override
					public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", Urls.getAuthKey());
					}
				})
        		.method(Request.Method.DELETE)
        		.create(mActivity, LogoutResourceReturnData.class);
		return listThreadsService;
	}

    @Provides @Singleton 
    public ServiceFetcher<LoginResourceInput, LoginResourceReturnData> providesLoginService(
    		ListThreadsPaginationObject pagination) {
		String url = Urls.getBasePath() + mActivity.getString(R.string.url_login);
		return new ServiceBuilder<LoginResourceInput, LoginResourceReturnData>()
				.url(url).method(Request.Method.POST)
				.entity(new LoginResourceInput())
				.create(mActivity, LoginResourceReturnData.class);
	}

}
