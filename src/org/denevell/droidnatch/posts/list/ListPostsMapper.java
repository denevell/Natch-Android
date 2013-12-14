package org.denevell.droidnatch.posts.list;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.ListViewResultDisplayer;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ServiceDisplayResultsController;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.entities.ListPostsResource;
import org.denevell.droidnatch.posts.entities.PostResource;
import org.denevell.droidnatch.posts.list.adapters.ListPostsArrayAdapter;
import org.denevell.droidnatch.posts.list.adapters.ListPostsResourceToListAdapter;
import org.denevell.droidnatch.posts.list.views.ListPostsContextMenu;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
    private Bundle mBundle;
    private ObservableFragment mObservableFragment;

    public ListPostsMapper(ObservableFragment listPostsFragment) {
        mObservableFragment = listPostsFragment;
        mActivity = listPostsFragment.getActivity();
        mBundle = listPostsFragment.getArguments();
    }
    
    @Provides @Named(PROVIDES_LIST_POSTS)
    public Controller providesController(
            ServiceFetcher<ListPostsResource> listPostsService, 
            ResultsDisplayer<List<PostResource>> resultsPane) {
        ServiceDisplayResultsController<ListPostsResource, List<PostResource>> controller = 
                new ServiceDisplayResultsController<ListPostsResource, List<PostResource>>(
                listPostsService, 
                resultsPane,
                new ListPostsResourceToListAdapter());
        return controller;
    }    
    
    @Provides
    public ServiceFetcher<ListPostsResource> provideService(
            ObjectStringConverter responseConverter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<ListPostsResource> volleyRequest, 
            Context appContext, 
            ProgressIndicator progress) {
        return new BaseService<ListPostsResource>(
                appContext, 
                volleyRequest,
                progress, 
                responseConverter,
                failureFactory,
                ListPostsResource.class);
    }    
    
    @Provides
    public VolleyRequest<ListPostsResource> providesRequest (
            Context appContext) {
        String url = appContext.getString(R.string.url_baseurl) 
                + appContext.getString(R.string.url_posts);
        url = url.replace("{thread_id}", (CharSequence) mBundle.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID));
        VolleyRequestGET<ListPostsResource> v = new VolleyRequestGET<ListPostsResource>();
        v.setUrl(url);
        return v;
    } 
    
    @Provides
    public ArrayAdapter<PostResource> providesListAdapter(
            Context appContext) {
        return new ListPostsArrayAdapter(appContext, R.layout.list_threads_row);
    }
    
    @Provides
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
