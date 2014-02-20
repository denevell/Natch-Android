package org.denevell.droidnatch.threads.list.di;

import javax.inject.Singleton;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.RegisterResourceInput;
import org.denevell.droidnatch.threads.list.entities.RegisterResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class RegisterServicesMapper {
    
    public RegisterServicesMapper() {
    }

    @Provides @Singleton
    public ServiceFetcher<RegisterResourceInput, RegisterResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<RegisterResourceInput, RegisterResourceReturnData> volleyRequest) {
        return new BaseService<RegisterResourceInput, RegisterResourceReturnData>(
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                RegisterResourceReturnData.class);
    }

    @Provides @Singleton
    public VolleyRequest<RegisterResourceInput, RegisterResourceReturnData> providesVolleyRequestDelete(
            ObjectToStringConverter reponseConverter,
            RegisterResourceInput input,
            Context appContext) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_register);
        VolleyRequestImpl<RegisterResourceInput, RegisterResourceReturnData> vollyRequest = 
                new VolleyRequestImpl<RegisterResourceInput, RegisterResourceReturnData>(
                		reponseConverter, 
                		input,
                		Request.Method.PUT);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

    @Provides @Singleton
    public RegisterResourceInput providesInput() {
        return new RegisterResourceInput();
    }
    
}
