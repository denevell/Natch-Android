package org.denevell.droidnatch.posts.list;

import java.util.List;

import javax.inject.Named;

import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.ListViewResultDisplayer;
import org.denevell.droidnatch.app.baseclasses.ServiceDisplayResultsController;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.entities.ListPostsResource;
import org.denevell.droidnatch.posts.entities.PostResource;
import org.denevell.droidnatch.posts.list.adapters.ListPostsResourceToListAdapter;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false)
public class ListPostsMapper {
    
    private Activity mActivity;
    private Bundle mBundle;

    public ListPostsMapper(ListPostsFragment listPostsFragment) {
        mActivity = listPostsFragment.getActivity();
        mBundle = listPostsFragment.getArguments();
    }
    
    @Provides @Named("listposts")
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
    public ServiceFetcher<ListPostsResource> provideLoginService(
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
    public VolleyRequest<ListPostsResource> providesListThreadsService(
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
        return new ArrayAdapter<PostResource>(appContext, R.layout.list_threads_row) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setContentDescription(v.getContentDescription()+String.valueOf(position));
                PostResource o = getItem(position);
                v.setText(o.getContent());
                return v;
            }};
    }
    
    @Provides @Named("list_posts_listview")
    public ListView provideListPostsListView() {
        return (ListView) mActivity.findViewById(R.id.list_posts_listview);
    }        

    @Provides
    public ResultsDisplayer<List<PostResource>> provideListPostsResultPane(
            Context appContext, 
            ArrayAdapter<PostResource> arrayAdapter, 
            @Named("list_posts_listview") ListView listView) {
        ListViewResultDisplayer<PostResource, List<PostResource>> displayer = 
                new ListViewResultDisplayer<PostResource, List<PostResource>>(
                        listView, 
                        arrayAdapter, 
                        null,
                        appContext);
        return displayer;
    } 

}
