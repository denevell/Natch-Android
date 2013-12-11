package org.denevell.droidnatch.thread.delete;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedHolder;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DeleteThreadMapper {
    
    private ContextItemSelectedHolder mActivity;

    public DeleteThreadMapper(ContextItemSelectedHolder activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named("deletethread")
    public Controller providesController(
            ServiceFetcher<DeletePostResourceReturnData> service, 
            Context appContext, 
            VolleyRequest<DeletePostResourceReturnData> deleteRequest, 
            @Named("listthreads") Controller listThreadsController, 
            ResultsDisplayer<ListThreadsResource> listThreadsResultsDisplayable,
            OnLongPressObserver<ThreadResource> longPressObserver) {
        DeleteThreadController controller = 
                new DeleteThreadController(
                        appContext,
                        deleteRequest,
                        service,
                        listThreadsController,
                        listThreadsResultsDisplayable,
                        longPressObserver);
        return controller;
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
    
    @Provides
    public ContextItemSelectedHolder providesContextSelectedHolder() {
        return mActivity;
    }
}
