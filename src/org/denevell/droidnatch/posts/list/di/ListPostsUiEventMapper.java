package org.denevell.droidnatch.posts.list.di;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.posts.list.ListPostsArrayAdapter;
import org.denevell.droidnatch.posts.list.ListPostsContextMenu;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.droidnatch.posts.list.entities.ListPostsResourceToArrayList;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false, library=true)
public class ListPostsUiEventMapper {
   
    private Activity mActivity;
    private ObservableFragment mObservableFragment;

    public ListPostsUiEventMapper(ObservableFragment listPostsFragment) {
        mObservableFragment = listPostsFragment;
        mActivity = listPostsFragment.getActivity();
    }
    
    @Provides @Singleton
    public ReceivingUiObject<ListPostsResource> providesReceivingUiObject (
            Context appContext, 
            ClickableListView<PostResource> listView) {
        View loading = (View) mActivity.findViewById(R.id.list_posts_loading);
        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(appContext, R.layout.list_posts_row);
        ListViewUiEvent<PostResource, List<PostResource>, ListPostsResource> displayer =
                new ListViewUiEvent<PostResource, List<PostResource>, ListPostsResource>(
                        listView.getListView(), 
                        arrayAdapter, 
                        null,
                        appContext,
                        new ListPostsResourceToArrayList());
        return displayer;
    } 

    @Provides
    public ClickableListView<PostResource> provideListView() {
        ListView lv = (ListView) mActivity.findViewById(R.id.list_posts_listview);
        ClickableListView<PostResource> clv = new ClickableListView<PostResource>(lv, 
                mObservableFragment,
                new HideKeyboard(),
                new ListPostsContextMenu());
        return clv;
    } 

}