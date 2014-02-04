package org.denevell.droidnatch.threads.list.di;

import android.content.Context;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestPUTImpl;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.views.AddThreadEditText;
import org.denevell.natch.android.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class, AddThreadEditText.class}, complete = false, library=true)
public class AddThreadServicesMapper {
    
    public AddThreadServicesMapper() {
    }

    @Provides @Singleton
    public ServiceFetcher<AddPostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<AddPostResourceReturnData> volleyRequest) {
        return new BaseService<AddPostResourceReturnData>(
                appContext, 
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                AddPostResourceReturnData.class);
    }

    @Provides @Singleton 
    public VolleyRequest<AddPostResourceReturnData> providesRequest(
            ObjectToStringConverter reponseConverter,
            AddPostResourceInput body,
            Context appContext) {
        VolleyRequestPUTImpl<AddPostResourceReturnData> vollyRequest = 
                new VolleyRequestPUTImpl<AddPostResourceReturnData>(
                    reponseConverter, 
                    body);
        vollyRequest.addHeader("AuthKey", Urls.getAuthKey());

        String url = Urls.getBasePath() + appContext.getString(R.string.url_addthread);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

    @Provides @Singleton
    public AddPostResourceInput providesThreadInput() {
        return new AddPostResourceInput();
    }

}
