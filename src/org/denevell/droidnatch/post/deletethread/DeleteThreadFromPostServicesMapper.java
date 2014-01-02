package org.denevell.droidnatch.post.deletethread;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DeleteThreadFromPostServicesMapper {
    
    private static final String DELETE_THREAD_FROM_POST_REQUEST = "delete_thread_from_post_request";
    private static final String DELETE_THREAD_FROM_POST_SERVICE = "delete_thread_from_post_service";

    public DeleteThreadFromPostServicesMapper() {
    }
    
    @Provides @Singleton @Named(DELETE_THREAD_FROM_POST_SERVICE)
    public ServiceFetcher<DeletePostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            @Named(DELETE_THREAD_FROM_POST_REQUEST) VolleyRequest<DeletePostResourceReturnData> volleyRequest
            ) {
        return new BaseService<DeletePostResourceReturnData>(
                appContext, 
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                DeletePostResourceReturnData.class);
    }

    @Provides @Singleton @Named(DELETE_THREAD_FROM_POST_REQUEST)
    public VolleyRequest<DeletePostResourceReturnData> providesVolleyRequestDelete(
            ObjectToStringConverter reponseConverter,
            Context appContext
            ) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_del); 
        VolleyRequestDELETE<DeletePostResourceReturnData> vollyRequest = 
                new VolleyRequestDELETE<DeletePostResourceReturnData>();
        vollyRequest.setUrl(url);
        return vollyRequest;
    }     

}
