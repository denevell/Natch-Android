package org.denevell.droidnatch.thread.delete.di;

import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DeleteThreadServicesMapper {
    
    public DeleteThreadServicesMapper() {
    }

    @Provides @Singleton
    public ServiceFetcher<DeletePostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<DeletePostResourceReturnData> volleyRequest) {
        return new BaseService<DeletePostResourceReturnData>(
                appContext, 
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                DeletePostResourceReturnData.class);
    }

    @Provides @Singleton
    public VolleyRequest<DeletePostResourceReturnData> providesVolleyRequestDelete(
            ObjectStringConverter reponseConverter,
            Context appContext) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_del); 
        VolleyRequestDELETE<DeletePostResourceReturnData> vollyRequest = 
                new VolleyRequestDELETE<DeletePostResourceReturnData>();
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 
    
}
