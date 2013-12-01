package org.denevell.droidnatch.listthreads;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.baseclasses.ProgressBarIndicator;
import org.denevell.droidnatch.interfaces.Controller;
import org.denevell.droidnatch.interfaces.FailureResultFactory;
import org.denevell.droidnatch.interfaces.ResponseConverter;
import org.denevell.droidnatch.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.interfaces.ServiceFetcher;
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

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsActivity.class})
public class ListThreadsMapper {
    
    private Activity mActivity;

    public ListThreadsMapper(Activity activity) {
        mActivity = activity;
    }
    
    // controllers
    
    @SuppressWarnings("serial")
    @Provides
    public List<Controller> provideControllers() {
        return new ArrayList<Controller>() {{
            add(providesLoginController());
        }};
    }

    public Controller providesLoginController() {
        ListThreadsController controller = new ListThreadsController(
                provideLoginService(), 
                provideLoginResultPane());
        return controller;
    }
    
    // others

    public ResponseConverter providesResponseConverter() {
        return new ResponseConverter() {
            @Override
            public <T> T convert(String s, Class<T> t) {
                return new Gson().fromJson(s, t);
            }
        };
    }

    public FailureResultFactory providesFailureResultFactory() {
        return new FailureResultFactory() {
            @Override
            public FailureResult newInstance(int statusCode, String errorMessage, String errorCode) {
                return new FailureResult(errorCode, errorMessage, statusCode);
            }
        };
    }
    
    public ListView providesList() {
        return (ListView) mActivity.findViewById(R.id.listView1);
    }

    public ArrayAdapter<ThreadResource> providesListAdapter() {
        return new ArrayAdapter<ThreadResource>(mActivity, android.R.layout.simple_list_item_1) {
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
                        mActivity.getApplicationContext(), 
                        providesList(), 
                        providesListAdapter());
        return displayer;
    }

    public ServiceFetcher<ListThreadsResource> provideLoginService() {
        ProgressBarIndicator progress = new ProgressBarIndicator(mActivity);
        Context context = mActivity.getApplicationContext();
        String url = context.getString(R.string.url_baseurl) + context.getString(R.string.url_threads);
        ResponseConverter responseConverter = providesResponseConverter();
        return new ListThreadsService(
                context, 
                url, 
                progress, 
                responseConverter,
                providesFailureResultFactory());
    }

}
