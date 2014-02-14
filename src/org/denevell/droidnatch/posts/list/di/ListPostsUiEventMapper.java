package org.denevell.droidnatch.posts.list.di;

import java.util.List;

import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.posts.list.ListPostsArrayAdapter;
import org.denevell.droidnatch.posts.list.ListPostsContextMenu;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.droidnatch.posts.list.entities.ListPostsResourceToArrayList;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false, library=true)
public class ListPostsUiEventMapper {
   
    private Activity mActivity;

    public ListPostsUiEventMapper(Activity activity) {
        mActivity = activity;
    }
    
    @Provides @Singleton
    public Receiver<ListPostsResource> providesReceivingUiObject (
            Context appContext, 
            ClickableListView<PostResource> listView) {
        //View loading = (View) mActivity.findViewById(R.id.list_posts_loading);
        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(appContext, R.layout.list_posts_row);
        ListViewUiEvent<PostResource, List<PostResource>, ListPostsResource> displayer =
                new ListViewUiEvent<PostResource, List<PostResource>, ListPostsResource>(
                        listView,
                        arrayAdapter, 
                        null,
                        appContext,
                        new ListPostsResourceToArrayList());
        return displayer;
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