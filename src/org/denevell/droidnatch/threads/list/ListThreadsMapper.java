package org.denevell.droidnatch.threads.list;

import java.util.HashMap;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.ListViewResultDisplayer;
import org.denevell.droidnatch.app.baseclasses.ServiceCallThenDisplayController;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.adapters.ListThreadsArrayAdapter;
import org.denevell.droidnatch.threads.list.adapters.ListThreadsResourceToListAdapter;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.uievents.ThreadsListPressEvent;
import org.denevell.droidnatch.threads.list.views.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.views.ListThreadsContextMenu;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class}, complete=false, library=true)
public class ListThreadsMapper {
    
    public static final String PROVIDES_LIST_THREADS_LIST_CLICK = "list_threads_list_click";
    public static final String PROVIDES_LIST_THREADS_LOADING = "listthreads_loading";
    public static final String PROVIDES_LIST_THREADS = "list_threads";
    private Activity mActivity;

    public ListThreadsMapper(Activity activity) {
        mActivity = activity;
    }
    
    @Provides @Singleton @Named(PROVIDES_LIST_THREADS)
    public Controller providesController(
            ServiceFetcher<ListThreadsResource> listThreadsService, 
            ResultsDisplayer<List<ThreadResource>> resultsPane, 
            @Named(PROVIDES_LIST_THREADS_LIST_CLICK) Runnable listClickListener) {
        ServiceCallThenDisplayController<ListThreadsResource, List<ThreadResource>> controller = 
                new ServiceCallThenDisplayController<ListThreadsResource, List<ThreadResource>>(
                    listThreadsService, 
                    resultsPane,
                    new ListThreadsResourceToListAdapter(),
                    listClickListener);
        return controller;
    }
    
    @Provides @Singleton @Named(PROVIDES_LIST_THREADS_LIST_CLICK)
    public Runnable providesOnListClickAction(
            final OnPressObserver<ThreadResource> onPressObserver, 
            final ScreenOpener screenOpener) {
        return new ThreadsListPressEvent(
                screenOpener, 
                onPressObserver,
                new HashMap<String, String>());
    }

    @Provides @Singleton 
    public OnLongPressObserver<ThreadResource> providesOnLongPressObserver(
            ClickableListView<ThreadResource> observer) {
        return observer;
    }

    @Provides @Singleton 
    public OnPressObserver<ThreadResource> providesOnPressObserver(
            ClickableListView<ThreadResource> observer) {
        return observer;
    }

    @Provides @Singleton @Named(PROVIDES_LIST_THREADS_LOADING)
    public View providesLoadingListView() {
        View v = mActivity.findViewById(R.id.list_threads_loading);
        return v;
    }

    @Provides @Singleton 
    public ClickableListView<ThreadResource> providesListView(
            ContextItemSelectedObserver contextSelectedObserver) {
        ListView listView = (ListView) mActivity.findViewById(R.id.listView1);
        ClickableListView<ThreadResource> ltlv = 
                new ClickableListView<ThreadResource>(
                        listView, 
                        contextSelectedObserver,
                        new ListThreadsContextMenu());
        return ltlv;
    }

    @Provides @Singleton
    public ArrayAdapter<ThreadResource> providesListAdapter(Context appContext) {
        return new ListThreadsArrayAdapter(appContext, R.layout.list_threads_row);
    }

    @Provides @Singleton
    public ResultsDisplayer<List<ThreadResource>> provideResultsDisplayer(
            Context appContext, 
            ArrayAdapter<ThreadResource> arrayAdapter, 
            ClickableListView<ThreadResource> listView, 
            @Named(PROVIDES_LIST_THREADS_LOADING) View listViewLoading) {
        ListViewResultDisplayer<ThreadResource, List<ThreadResource>> displayer = 
                new ListViewResultDisplayer<ThreadResource, List<ThreadResource>>(
                        listView.getListView(), 
                        arrayAdapter, 
                        listViewLoading,
                        appContext);
        return displayer;
    }

    @Provides
    public ServiceFetcher<ListThreadsResource> provideService(
            ObjectStringConverter responseConverter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<ListThreadsResource> volleyRequest, 
            Context appContext, 
            ProgressIndicator progress) {
        return new BaseService<ListThreadsResource>(
                appContext, 
                volleyRequest,
                progress, 
                responseConverter,
                failureFactory,
                ListThreadsResource.class);
    }

    @Provides
    public VolleyRequest<ListThreadsResource> providesRequest (
            Context appContext) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_threads);
        VolleyRequestGET<ListThreadsResource> v = 
                new VolleyRequestGET<ListThreadsResource>();
        v.setUrl(url);
        return v;
    }

}
