package org.denevell.droidnatch.threads.list.di;

import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.natch.android.R;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsControllerMapper.class}, complete=false, library=true)
public class ListThreadsServiceMapper {
    
    @SuppressWarnings("unused")
    private static final String TAG = ListThreadsServiceMapper.class.getSimpleName();

    public ListThreadsServiceMapper() {
    }
    
    @Provides 
    public ServiceFetcher<ListThreadsResource> provideService(
            ObjectToStringConverter responseConverter, 
            FailureResultFactory failureFactory, 
            Context appContext, 
            ProgressIndicator progress) {
        return new BaseService<ListThreadsResource>(
                appContext, 
                providesRequest(appContext),
                progress, 
                responseConverter,
                failureFactory,
                ListThreadsResource.class);
    }

    private VolleyRequest<ListThreadsResource> providesRequest(Context appContext) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_threads);
        VolleyRequestGET<ListThreadsResource> v = 
                new VolleyRequestGET<ListThreadsResource>();
        v.setUrl(url);
        return v;
    }

}
