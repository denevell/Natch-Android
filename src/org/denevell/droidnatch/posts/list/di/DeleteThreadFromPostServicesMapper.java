package org.denevell.droidnatch.posts.list.di;

import android.content.Context;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = ListPostsFragment.class, complete = false, library=true)
public class DeleteThreadFromPostServicesMapper {

    public static final String DELETE_THREAD_FROM_POST_SERVICE = "delete thread from post service";
    public static final String DELETE_THREAD_FROM_VOLLEY_REQUEST = "delete thread from volley request";

    public DeleteThreadFromPostServicesMapper() {
    }
  
    @Provides @Named(DELETE_THREAD_FROM_POST_SERVICE)
    public ServiceFetcher<DeletePostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory,
            @Named(DELETE_THREAD_FROM_VOLLEY_REQUEST) VolleyRequest<DeletePostResourceReturnData> volleyRequest) {
        return new BaseService<DeletePostResourceReturnData>(
                appContext, 
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                DeletePostResourceReturnData.class);
    }

    @Provides @Singleton @Named(DELETE_THREAD_FROM_VOLLEY_REQUEST)
    public VolleyRequest<DeletePostResourceReturnData> providesVolleyRequestDelete(
            ObjectToStringConverter reponseConverter,
            Context appContext) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_del);
        VolleyRequestDELETE<DeletePostResourceReturnData> vollyRequest = 
                new VolleyRequestDELETE<DeletePostResourceReturnData>();
        vollyRequest.setUrl(url);
        vollyRequest.addHeader("AuthKey", Urls.getAuthKey());
        return vollyRequest;
    } 

}