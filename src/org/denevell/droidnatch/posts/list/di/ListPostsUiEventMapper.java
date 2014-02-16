package org.denevell.droidnatch.posts.list.di;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent.AvailableItems;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.posts.list.ListPostsArrayAdapter;
import org.denevell.droidnatch.posts.list.ListPostsContextMenu;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
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
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false, library=true)
public class ListPostsUiEventMapper {
   
    private static final String PROVIDES_LIST_POSTS_PAGINATION_BUTTON = "list_posts_button";
	private Activity mActivity;
	private String mTheadId;

    public ListPostsUiEventMapper(Activity activity, String threadId) {
        mActivity = activity;
        mTheadId = threadId;
    }
    
    @Provides @Singleton
    public Receiver<ThreadResource> providesReceivingUiObject (
            Context appContext, 
            ClickableListView<PostResource> listView,
            @Named(PROVIDES_LIST_POSTS_PAGINATION_BUTTON) Button moreButton) {
        //View loading = (View) mActivity.findViewById(R.id.list_posts_loading);
        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(appContext, R.layout.list_posts_row);
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
                        moreButton);
        return displayer;
    } 

    @Provides @Singleton @Named(PROVIDES_LIST_POSTS_PAGINATION_BUTTON)
    public Button providesOnPaginationButton(
    		final VolleyRequest<ThreadResource> request,
    		final ListPostsPaginationObject pagination) {
        final Button button = new Button(mActivity);
        button.setText("More");
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pagination.range+=1;
			    String url = mActivity.getString(R.string.url_baseurl)+mActivity.getString(R.string.url_posts);
                url = url.replace("{thread_id}", mTheadId);
                url += pagination.start+"/"+pagination.range;
			    request.setUrl(url);
			    EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
			}
		});
        return button;
	}

    @SuppressWarnings("unchecked")
	@Provides @Singleton
    public ClickableListView<PostResource> provideListView() {
        @SuppressWarnings("rawtypes")
		ClickableListView lv = (ClickableListView) mActivity.findViewById(R.id.list_posts_listview);
        lv.setKeyboadHider(new HideKeyboard());
        lv.setOnCreateContextMenuListener(new ListPostsContextMenu());
        return lv;
    } 

}