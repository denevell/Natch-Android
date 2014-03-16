package org.denevell.droidnatch.notifications;

import java.util.HashMap;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.ListThreadsArrayAdapter;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResourceTotalAvailable;
import org.denevell.droidnatch.threads.list.entities.ListThreadsToList;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.newfivefour.android.manchester.R;

import dagger.Module;
import dagger.Provides;

@Module(complete=false, library=true)
public class AnnouncementsMapper {
    
    private static final String TAG = AnnouncementsMapper.class.getSimpleName();
    private Activity mActivity;

    public AnnouncementsMapper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides @Singleton @Named("announce")
    public ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> providesListView(
    		@Named("announce") ServiceFetcher<Void, ListThreadsResource> listService,
    		@Named("announce") OnPress<ThreadResource> onPressListener,
    		Context appContext
    		) {
        final ReceivingClickingAutopaginatingListView listView = (ReceivingClickingAutopaginatingListView) mActivity.findViewById(R.id.announcements_listview);

        ListThreadsArrayAdapter listAdapter = new ListThreadsArrayAdapter(appContext, R.layout.threads_list_row);

		listView
			.setListAdapter(listAdapter)
        	.setTypeAdapter(new ListThreadsToList())
			.setPaginationView(R.layout.pagination_button_generic)
            .setErrorViewId(R.id.list_view_service_error)
			.setAvailableItems(new ListThreadsResourceTotalAvailable())
        	.setKeyboardHider(new HideKeyboard());
		
        listView.addOnPressListener(onPressListener);
        
        return listView;
    }

	@Provides @Singleton @Named("announce")
	public OnPress<ThreadResource> providesOnListClickAction(
			final ScreenOpener screenOpener) {
		OnPress<ThreadResource> onPress = new OnPress<ThreadResource>() {
			@Override
			public void onPress(ThreadResource obj) {
				Log.v(TAG, "Opening new list posts fragment");
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getId());
				hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, obj.getSubject());
				screenOpener.openScreen(ListPostsFragment.class, hm, true);
			}
		};
		return onPress;
	}
    
    @Provides @Singleton @Named("announce")
    public ServiceFetcher<Void, ListThreadsResource> provideListAnnouncementsService() {
		String url = ShamefulStatics.getBasePath()+mActivity.getString(R.string.url_announcements);
		ServiceFetcher<Void, ListThreadsResource> listThreadsService
        	= new ServiceBuilder<Void, ListThreadsResource>()
        		.url(url)
        		.method(Request.Method.GET)
        		.create(mActivity, ListThreadsResource.class);
		return listThreadsService;
	}

}
