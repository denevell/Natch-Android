package org.denevell.droidnatch.thread.delete;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class}, complete = false)
public class DeleteThreadMapper {
    
    @SuppressWarnings("unused")
    private Activity mActivity;

    public DeleteThreadMapper(Activity activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named("deletethread")
    public DeleteThreadController providesController(ServiceFetcher<DeletePostResourceReturnData> service) {
        DeleteThreadController controller = 
                new DeleteThreadController(
                        service);
        return controller;
    }

    @Provides @Singleton
    public ServiceFetcher<DeletePostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectStringConverter converter, 
            FailureResultFactory failureFactory, 
            @Named("deletethread_service_request") VolleyRequest volleyRequest) {
        return new BaseService<DeletePostResourceReturnData>(
                appContext, 
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                DeletePostResourceReturnData.class);
    }

    @Provides @Singleton @Named("deletethread_service_request")
    public VolleyRequest providesVolleyRequestDelete(
            ObjectStringConverter reponseConverter,
            Context appContext) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_del); 
        VolleyRequestDELETE vollyRequest = new VolleyRequestDELETE();
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

}
