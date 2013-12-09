package org.denevell.droidnatch.listthreads;

import javax.inject.Named;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.denevell.droidnatch.listthreads.entities.ThreadResource;
import org.denevell.droidnatch.listthreads.views.ListThreadsResultDisplayer;
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

@Module(injects = {MainPageActivity.class}, 
        complete=false)
public class ListThreadsMapper {
    
    private Activity mActivity;

    public ListThreadsMapper(Activity activity) {
        mActivity = activity;
    }
    
    @Provides @Named("listthreads")
    public Controller providesLoginController(
            ServiceFetcher<ListThreadsResource> loginService, 
            ResultsDisplayer<ListThreadsResource> resultsPane) {
        ListThreadsController controller = new ListThreadsController(
                loginService, 
                resultsPane);
        return controller;
    }

    @Provides @Named("listthreads_listview")
    public ListView providesListView() {
        return (ListView) mActivity.findViewById(R.id.listView1);
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
                ThreadResource o = getItem(position);
                v.setText(o.getSubject());
                return v;
            }};
    }

    @Provides
    public ResultsDisplayer<ListThreadsResource> provideLoginResultPane(
            Context appContext, 
            ArrayAdapter<ThreadResource> arrayAdapter, 
            @Named("listthreads_listview") ListView listView, 
            @Named("listthreads_loading") View listViewLoading) {
        ListThreadsResultDisplayer displayer = 
                new ListThreadsResultDisplayer(
                        listView, 
                        arrayAdapter, 
                        listViewLoading,
                        appContext);
        return displayer;
    }

    @Provides
    public ServiceFetcher<ListThreadsResource> provideLoginService(
            ObjectStringConverter responseConverter, 
            FailureResultFactory failureFactory, 
            VolleyRequest volleyRequest, 
            Context appContext, 
            ProgressIndicator progress) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_threads);
        return new BaseService<ListThreadsResource>(
                appContext, 
                url, 
                volleyRequest,
                progress, 
                responseConverter,
                failureFactory,
                ListThreadsResource.class);
    }

}
