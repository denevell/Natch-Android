package org.denevell.droidnatch.threads.list.di;

import android.content.Context;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;
import org.denevell.natch.android.R;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class, ListThreadsViewStarter.class, ListPostsViewStarter.class}, complete=false, library=true)
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
                providesRequest(appContext),
                progress, 
                responseConverter,
                failureFactory,
                ListThreadsResource.class);
    }

    private VolleyRequest<ListThreadsResource> providesRequest(Context appContext) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_threads);
        VolleyRequestGET<ListThreadsResource> v = 
                new VolleyRequestGET<ListThreadsResource>();
        v.setUrl(url);
        return v;
    }

}
