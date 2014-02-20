package org.denevell.droidnatch.threads.list;

import java.util.HashMap;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListThreadsPaginationObject;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent.AvailableItems;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;
import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete=false, library=true)
public class ListThreadsUiEventMapper {
    
    public static final String PROVIDES_LIST_THREADS_PAGINATION_BUTTON = "List view pagination button";
	public static final String PROVIDES_LIST_THREADS_LIST_CLICK = "list_threads_list_click";
    private static final String TAG = ListThreadsUiEventMapper.class.getSimpleName();
    private Activity mActivity;

    public ListThreadsUiEventMapper(Activity activity) {
        mActivity = activity;
    }

    @Provides @Singleton
    public Receiver<ListThreadsResource> providesReceivingUiObject(
            Context appContext, 
            ClickableListView<ThreadResource> listView,
            // We're taking in the OnPress simply so it's constructed.
            OnPress<ThreadResource> listClickListener,
    		@Named(PROVIDES_LIST_THREADS_PAGINATION_BUTTON) Button moreButton
    		) {
        //View loadingListView = mActivity.findViewById(R.id.list_threads_loading);
        ListThreadsArrayAdapter listAdapter = new ListThreadsArrayAdapter(appContext, R.layout.list_threads_row);
        ListViewUiEvent<ThreadResource, List<ThreadResource>, ListThreadsResource> displayer =
                new ListViewUiEvent<ThreadResource, List<ThreadResource>, ListThreadsResource>(
                        listView,
                        listAdapter, 
                        null,
                        appContext,
                        new TypeAdapter<ListThreadsResource, List<ThreadResource>>() {
                            @Override
                            public List<ThreadResource> convert(ListThreadsResource ob) {
                                return ob.getThreads();
                            }
                        },
                        new AvailableItems<ListThreadsResource>() {
							@Override
							public int getTotalAvailableForList(ListThreadsResource ob) {
								if(ob==null) return 0;
								else return (int) ob.getNumOfThreads();
							}
						},
						moreButton);
        return displayer;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides @Singleton
    public ClickableListView<ThreadResource> providesListView() {
        ClickableListView listView = (ClickableListView) mActivity.findViewById(R.id.list_threads_listview);
        listView.setKeyboadHider(new HideKeyboard());
        listView.setOnCreateContextMenuListener(new ListThreadsContextMenu());
        return listView;
    }

    @Provides @Singleton @Named(PROVIDES_LIST_THREADS_PAGINATION_BUTTON)
    public Button providesOnPaginationButton(
    		final ServiceFetcher<Void, ListThreadsResource> request,
    		final ListThreadsPaginationObject pagination) {
        final Button button = new Button(mActivity);
        button.setText("More");
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pagination.paginate();
			    String url = Urls.getBasePath() + mActivity.getString(R.string.url_threads) + "" + pagination.start + "/" + pagination.range;
			    request.getRequest().setUrl(url);
			    EventBus.getBus().post(new ListThreadsViewStarter.CallControllerListThreads());
			}
		});
        return button;
	}
    
    @Provides @Singleton 
    public ServiceFetcher<Void, ListThreadsResource> providesService(
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
    public OnPress<ThreadResource> providesOnListClickAction(
            final ClickableListView<ThreadResource> onPressObserver,
            final ScreenOpener screenOpener) {
        OnPress<ThreadResource> onPress = new OnPress<ThreadResource>() {
                    @Override
                    public void onPress(ThreadResource obj) {
                        Log.v(TAG, "Opening new list posts fragment");
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getId());
                        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, obj.getSubject());
                        screenOpener.openScreen(ListPostsFragment.class, hm);
                    }
                };
        onPressObserver.addOnPressListener(onPress);
        return onPress;
    }

}
