package org.denevell.droidnatch.listthreads;

import javax.inject.Named;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.baseclasses.ProgressBarIndicator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.denevell.droidnatch.listthreads.entities.ThreadResource;
import org.denevell.droidnatch.listthreads.service.ListThreadsService;
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
    
    // controllers

    @Provides @Named("listthreads")
    public Controller providesLoginController(
            ServiceFetcher<ListThreadsResource> loginService) {
        ListThreadsController controller = new ListThreadsController(
                loginService, 
                provideLoginResultPane());
        return controller;
    }
    
    // others

    public ListView providesList() {
        return (ListView) mActivity.findViewById(R.id.listView1);
    }

    public View providesLoadingListView() {
        View v = mActivity.findViewById(R.id.list_threads_loading);
        return v;
    }

    public ArrayAdapter<ThreadResource> providesListAdapter() {
        return new ArrayAdapter<ThreadResource>(mActivity, R.layout.list_threads_row) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                ThreadResource o = getItem(position);
                v.setText(o.getSubject());
                return v;
            }};
    }

    public ResultsDisplayer<ListThreadsResource> provideLoginResultPane() {
        ListThreadsResultDisplayer displayer = 
                new ListThreadsResultDisplayer(
                        providesList(), 
                        providesListAdapter(), 
                        providesLoadingListView(),
                        mActivity.getApplicationContext());
        return displayer;
    }

    @Provides
    public ServiceFetcher<ListThreadsResource> provideLoginService(
            ResponseConverter responseConverter, 
            FailureResultFactory failureFactory, 
            VolleyRequest volleyRequest) {
        ProgressBarIndicator progress = new ProgressBarIndicator(mActivity);
        Context context = mActivity.getApplicationContext();
        String url = context.getString(R.string.url_baseurl) + context.getString(R.string.url_threads);
        return new ListThreadsService(
                context, 
                url, 
                progress, 
                responseConverter,
                failureFactory,
                volleyRequest);
    }

}
