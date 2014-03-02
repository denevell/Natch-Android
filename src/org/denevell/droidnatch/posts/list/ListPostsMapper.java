package org.denevell.droidnatch.posts.list;

import java.util.List;

import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
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
		ReceivingClickingAutopaginatingListView listview = (ReceivingClickingAutopaginatingListView) mActivity.findViewById(R.id.list_posts_listview);

        final Button button = new Button(mActivity);
        button.setText("...Loading...");

        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(mActivity.getApplicationContext(), R.layout.posts_list_row);
        
		listview
			.setListAdapter(arrayAdapter)
			.setTypeAdapter(new ThreadResourceResourceToArrayList())
        	.setAvailableItems(new ThreadResourceTotalAvailable())
        	.setErrorView(R.layout.list_view_service_error)
			.addOnPaginationFooterVisibleCallback(new Runnable() {
				@Override
				public void run() {
					pagination.paginate();
					String url = Urls.getBasePath() + mActivity.getString(R.string.url_posts);
					url = url.replace("{thread_id}", mTheadId);
					url += pagination.start + "/" + pagination.range;
					request.getRequest().setUrl(url);
					EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
				}
			})
        	.setPaginationView(button)
			.setKeyboardHider(new HideKeyboard());
        listview.setOnCreateContextMenuListener(new ListPostsContextMenu(arrayAdapter));

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