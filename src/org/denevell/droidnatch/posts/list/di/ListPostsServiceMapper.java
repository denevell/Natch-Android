package org.denevell.droidnatch.posts.list.di;

import android.content.Context;
import android.os.Bundle;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.natch.android.R;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false, library=true)
public class ListPostsServiceMapper {
   
    private Bundle mBundle;

    public ListPostsServiceMapper(ObservableFragment listPostsFragment) {
        mBundle = listPostsFragment.getArguments();
    }
    
    @Provides
    public ServiceFetcher<ListPostsResource> provideService(
            ObjectToStringConverter responseConverter, 
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
        String url = Urls.getBasePath()
                + appContext.getString(R.string.url_posts);
        url = url.replace("{thread_id}", (CharSequence) mBundle.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID));
        VolleyRequestGET<ListPostsResource> v = new VolleyRequestGET<ListPostsResource>();
        v.setUrl(url);
        return v;
    } 
    
}
