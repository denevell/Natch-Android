package org.denevell.droidnatch.threads.list;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.baseclasses.BaseService;
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
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.views.ListThreadsListView;
import org.denevell.droidnatch.threads.list.views.ListViewResultDisplayer;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class}, complete=false, library=true)
public class ListThreadsMapper {
    
    private Activity mActivity;

    public ListThreadsMapper(Activity activity) {
        mActivity = activity;
    }
    
    @Provides @Named("listthreads")
    public Controller providesController(
            ServiceFetcher<ListThreadsResource> listThreadsService, 
            ResultsDisplayer<List<ThreadResource>> resultsPane, 
            OnPressObserver<ThreadResource> onPressObserver, 
            ScreenOpener screenOpener) {
        ListThreadsController controller = new ListThreadsController(
                listThreadsService, 
                resultsPane,
                onPressObserver,
                screenOpener);
        return controller;
    }

    @Provides @Singleton 
    public ListThreadsListView providesListView(
            ContextItemSelectedObserver contextSelectedHolder) {
        ListView listView = (ListView) mActivity.findViewById(R.id.listView1);
        ListThreadsListView ltlv = new ListThreadsListView(listView, contextSelectedHolder);
        return ltlv;
    }

    @Provides @Singleton 
    public OnLongPressObserver<ThreadResource> providesOnLongPressObserver(
            ListThreadsListView observer) {
        return observer;
    }

    @Provides @Singleton 
    public OnPressObserver<ThreadResource> providesOnPressObserver(
            ListThreadsListView observer) {
        return observer;
    }

    @Provides @Named("listthreads_loading")
    public View providesLoadingListView() {
        View v = mActivity.findViewById(R.id.list_threads_loading);
        return v;
    }

    @Provides
    public ArrayAdapter<ThreadResource> providesListAdapter(
            Context appContext) {
        return new ArrayAdapter<ThreadResource>(appContext, R.layout.list_threads_row) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setContentDescription(v.getContentDescription()+String.valueOf(position));
                ThreadResource o = getItem(position);
                v.setText(o.getSubject());
                return v;
            }};
    }

    @Provides
    public ResultsDisplayer<List<ThreadResource>> provideLoginResultPane(
            Context appContext, 
            ArrayAdapter<ThreadResource> arrayAdapter, 
            ListThreadsListView listView, 
            @Named("listthreads_loading") View listViewLoading) {
        ListViewResultDisplayer<ThreadResource, List<ThreadResource>> displayer = 
                new ListViewResultDisplayer<ThreadResource, List<ThreadResource>>(
                        listView.getListView(), 
                        arrayAdapter, 
                        listViewLoading,
                        appContext);
        return displayer;
    }

    @Provides
    public ServiceFetcher<ListThreadsResource> provideLoginService(
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
    public VolleyRequest<ListThreadsResource> providesListThreadsService(
            Context appContext) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_threads);
        VolleyRequestGET<ListThreadsResource> v = new VolleyRequestGET<ListThreadsResource>();
        v.setUrl(url);
        return v;
    }

}
