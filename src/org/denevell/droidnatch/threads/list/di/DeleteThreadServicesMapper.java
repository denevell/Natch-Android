package org.denevell.droidnatch.threads.list.di;

import android.content.Context;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.views.LongClickDeleteUiEvent;
import org.denevell.natch.android.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {LongClickDeleteUiEvent.class}, complete = false, library = true)
public class DeleteThreadServicesMapper {
    
    public DeleteThreadServicesMapper() {
    }

    @Provides @Singleton
    public ServiceFetcher<DeletePostResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<DeletePostResourceReturnData> volleyRequest) {
        return new BaseService<DeletePostResourceReturnData>(
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                DeletePostResourceReturnData.class);
    }

    @Provides @Singleton
    public VolleyRequest<DeletePostResourceReturnData> providesVolleyRequestDelete(
            ObjectToStringConverter reponseConverter,
            Context appContext) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_del);
        VolleyRequestDELETE<DeletePostResourceReturnData> vollyRequest = 
                new VolleyRequestDELETE<DeletePostResourceReturnData>();
        vollyRequest.addHeader("AuthKey", Urls.getAuthKey());
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 
    
}
