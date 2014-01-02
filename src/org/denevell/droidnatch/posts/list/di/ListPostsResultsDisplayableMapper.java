package org.denevell.droidnatch.posts.list.di;

import java.util.List;

import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.ListViewResultDisplayer;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.posts.list.ListPostsArrayAdapter;
import org.denevell.droidnatch.posts.list.ListPostsContextMenu;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false, library=true)
public class ListPostsResultsDisplayableMapper {
   
    private Activity mActivity;
    private ObservableFragment mObservableFragment;

    public ListPostsResultsDisplayableMapper(ObservableFragment listPostsFragment) {
        mObservableFragment = listPostsFragment;
        mActivity = listPostsFragment.getActivity();
    }
    
    @Provides @Singleton
    public ResultsDisplayer<List<PostResource>> providResultDisplayer(
            Context appContext, 
            ClickableListView<PostResource> listView) {
        ListPostsArrayAdapter arrayAdapter = new ListPostsArrayAdapter(appContext, R.layout.list_threads_row);
        ListViewResultDisplayer<PostResource, List<PostResource>> displayer = 
                new ListViewResultDisplayer<PostResource, List<PostResource>>(
                        listView.getListView(), 
                        arrayAdapter, 
                        null,
                        appContext);
        return displayer;
    } 

    @Provides
    public ClickableListView<PostResource> provideListView() {
        ListView lv = (ListView) mActivity.findViewById(R.id.list_posts_listview);
        ClickableListView<PostResource> clv = new ClickableListView<PostResource>(lv, 
                mObservableFragment, 
                new ListPostsContextMenu());
        return clv;
    } 

}