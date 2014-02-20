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
import org.denevell.droidnatch.threads.list.entities.LoginResourceInput;
import org.denevell.droidnatch.threads.list.entities.LoginResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class LoginServicesMapper {
    
    public LoginServicesMapper() {
    }

    @Provides @Singleton
    public ServiceFetcher<LoginResourceInput, LoginResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<LoginResourceInput, LoginResourceReturnData> volleyRequest) {
        return new BaseService<LoginResourceInput, LoginResourceReturnData>(
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                LoginResourceReturnData.class);
    }

    @Provides @Singleton
    public VolleyRequest<LoginResourceInput, LoginResourceReturnData> providesVolleyRequestDelete(
            ObjectToStringConverter reponseConverter,
            LoginResourceInput input,
            Context appContext) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_login);
        VolleyRequestImpl<LoginResourceInput, LoginResourceReturnData> vollyRequest = 
                new VolleyRequestImpl<LoginResourceInput, LoginResourceReturnData>(reponseConverter, input,
                		Request.Method.POST);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

    @Provides @Singleton
    public LoginResourceInput providesThreadInput() {
        return new LoginResourceInput();
    }
    
}
