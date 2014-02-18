package org.denevell.droidnatch.threads.list.di;

import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListThreadsPaginationObject;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl;
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

import android.content.Context;

import com.android.volley.Request;

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
            ProgressIndicator progress,
            VolleyRequest<ListThreadsResource> request) {
        return new BaseService<ListThreadsResource>(
                request,
                progress,
                responseConverter,
                failureFactory,
                ListThreadsResource.class);
    }

    @Provides @Singleton
    public VolleyRequest<ListThreadsResource> providesRequest(Context appContext, 
    		ListThreadsPaginationObject pagination) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_threads) + "" + pagination.start + "/" + pagination.range;
        VolleyRequestImpl<ListThreadsResource> v = new VolleyRequestImpl<ListThreadsResource>(
        		null, null, Request.Method.GET);
        v.setUrl(url);
        return v;
    }

}
