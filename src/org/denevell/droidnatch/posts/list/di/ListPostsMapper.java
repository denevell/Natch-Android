package org.denevell.droidnatch.posts.list.di;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.ListViewResultDisplayer;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.controllers.ServiceCallThenDisplayController;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.list.ListPostsArrayAdapter;
import org.denevell.droidnatch.posts.list.ListPostsContextMenu;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.droidnatch.posts.list.entities.ListPostsResourceToArrayList;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false)
public class ListPostsMapper {
   
    public static final String PROVIDES_LIST_POSTS_LISTVIEW = "list_posts_listview";
    public static final String PROVIDES_LIST_POSTS = "list_posts";
    private Activity mActivity;
    private ObservableFragment mObservableFragment;

    public ListPostsMapper(ObservableFragment listPostsFragment) {
        mObservableFragment = listPostsFragment;
        mActivity = listPostsFragment.getActivity();
    }
    
    // Controller
    
    @Provides @Named(PROVIDES_LIST_POSTS) @Singleton
    public Controller providesController(
            ServiceFetcher<ListPostsResource> listPostsService, 
            ResultsDisplayer<List<PostResource>> resultsPane) {
        ServiceCallThenDisplayController<ListPostsResource, List<PostResource>> controller = 
                new ServiceCallThenDisplayController<ListPostsResource, List<PostResource>>(
                listPostsService, 
                resultsPane,
                new ListPostsResourceToArrayList());
        return controller;
    }    
    
    // List view

    @Provides @Singleton
    public ResultsDisplayer<List<PostResource>> providResultDisplayer(
            Context appContext, 
            ArrayAdapter<PostResource> arrayAdapter, 
            @Named(PROVIDES_LIST_POSTS_LISTVIEW) ClickableListView<PostResource> listView) {
        ListViewResultDisplayer<PostResource, List<PostResource>> displayer = 
                new ListViewResultDisplayer<PostResource, List<PostResource>>(
                        listView.getListView(), 
                        arrayAdapter, 
                        null,
                        appContext);
        return displayer;
    } 
    
    @Provides @Singleton
    public ArrayAdapter<PostResource> providesListAdapter(Context appContext) {
        return new ListPostsArrayAdapter(appContext, R.layout.list_threads_row);
    }

    @Provides @Named(PROVIDES_LIST_POSTS_LISTVIEW)
    public ClickableListView<PostResource> provideListView(
            OnCreateContextMenuListener onCreateContextMenu, 
            ContextItemSelectedObserver contextSelectedObservable) {
        ListView lv = (ListView) mActivity.findViewById(R.id.list_posts_listview);
        ClickableListView<PostResource> clv = new ClickableListView<PostResource>(lv, 
                contextSelectedObservable, 
                onCreateContextMenu);
        return clv;
    } 
    
    @Provides @Singleton
    public ContextItemSelectedObserver providesContextItemSelectedObserver() {
        return mObservableFragment;
    }

    @Provides @Singleton
    public OnCreateContextMenuListener providesContextMenuCreator() {
        return new ListPostsContextMenu();
    }

}
