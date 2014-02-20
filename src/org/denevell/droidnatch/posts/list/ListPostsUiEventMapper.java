package org.denevell.droidnatch.posts.list;

import java.util.List;

import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent.AvailableItems;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.posts.list.entities.ThreadResourceResourceToArrayList;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library=true)
public class ListPostsUiEventMapper {
   
	private Activity mActivity;
	private String mTheadId;

    public ListPostsUiEventMapper(Activity activity, String threadId) {
        mActivity = activity;
        mTheadId = threadId;
    }
    
    @Provides @Singleton
    public Receiver<ThreadResource> providesReceivingUiObject (
            Context appContext, 
            ServiceFetcher<Void, ThreadResource> request, 
            ListPostsPaginationObject pagination) {
        //View loading = (View) mActivity.findViewById(R.id.list_posts_loading);

        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(appContext, R.layout.list_posts_row);
        Button paginationButton = providesOnPaginationButton(request, pagination);
        ClickableListView<PostResource> listView = provideListView(); 

		ListViewUiEvent<PostResource, List<PostResource>, ThreadResource> displayer =
                new ListViewUiEvent<PostResource, List<PostResource>, ThreadResource>(
                        listView,
                        arrayAdapter, 
                        null,
                        appContext,
                        new ThreadResourceResourceToArrayList(),
                        new AvailableItems<ThreadResource>() {
							@Override
							public int getTotalAvailableForList(ThreadResource ob) {
								return ob.getNumPosts();
							}
						},
                        paginationButton);
        return displayer;
    } 

    private Button providesOnPaginationButton(
    		final ServiceFetcher<Void, ThreadResource> request,
    		final ListPostsPaginationObject pagination) {
        final Button button = new Button(mActivity);
        button.setText("More");
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pagination.paginate();
			    String url = Urls.getBasePath()+mActivity.getString(R.string.url_posts);
                url = url.replace("{thread_id}", mTheadId);
                url += pagination.start+"/"+pagination.range;
			    request.getRequest().setUrl(url);
			    EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
			}
		});
        return button;
	}

	@SuppressWarnings("unchecked")
    private ClickableListView<PostResource> provideListView() {
        @SuppressWarnings("rawtypes")
		ClickableListView lv = (ClickableListView) mActivity.findViewById(R.id.list_posts_listview);
        lv.setKeyboadHider(new HideKeyboard());
        lv.setOnCreateContextMenuListener(new ListPostsContextMenu());
        return lv;
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