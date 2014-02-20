package org.denevell.droidnatch.posts.list.di;

import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false, library=true)
public class ListPostsServiceMapper {
   
    private Bundle mBundle;

    public ListPostsServiceMapper(Bundle bundl3) {
        mBundle = bundl3;
    }
    
    @Provides @Singleton
    public ServiceFetcher<ThreadResource> provideService(
            ObjectToStringConverter responseConverter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<Void, ThreadResource> volleyRequest, 
            ProgressIndicator progress) {
        return new BaseService<ThreadResource>(
                volleyRequest,
                progress, 
                responseConverter,
                failureFactory,
                ThreadResource.class);
    }    
    
    @Provides @Singleton
    public VolleyRequest<Void, ThreadResource> providesRequest (
            Context appContext,
            ListPostsPaginationObject pagination) {
        String url = Urls.getBasePath()
                + appContext.getString(R.string.url_posts);
        url = url.replace("{thread_id}", (CharSequence) mBundle.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID));
        url += pagination.start+"/"+pagination.range;
        VolleyRequestImpl<Void, ThreadResource> v = new VolleyRequestImpl<Void, ThreadResource>(
        		null, null, Request.Method.GET);
        v.setUrl(url);
        return v;
    } 
    
}
