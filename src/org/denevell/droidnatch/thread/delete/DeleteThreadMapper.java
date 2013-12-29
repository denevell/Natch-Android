package org.denevell.droidnatch.thread.delete;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.thread.delete.uievents.LongClickDeleteEvent;
import org.denevell.droidnatch.threads.list.di.ListThreadsControllerMapper;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DeleteThreadMapper {
    
    private static final String DELETE_THREAD_UI_EVENT = "delete_thread_ui_event";
    public static final String PROVIDES_DELETE_THREAD_CONTROLLER = "delete_thread_controller";
    private ContextItemSelectedObserver mActivity;

    public DeleteThreadMapper(ContextItemSelectedObserver activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named(PROVIDES_DELETE_THREAD_CONTROLLER)
    public Controller providesController(
            ServiceFetcher<DeletePostResourceReturnData> service, 
            @Named(ListThreadsControllerMapper.PROVIDES_LIST_THREADS) Controller listThreadsController, 
            ResultsDisplayer<List<ThreadResource>> listThreadsResultsDisplayable,
            @Named(DELETE_THREAD_UI_EVENT) GenericUiObservable uiEvent) {
        UiEventThenServiceCallController controller = 
                new UiEventThenServiceCallController(
                        uiEvent,
                        service,
                        listThreadsResultsDisplayable,
                        listThreadsController);
        return controller;
    }
    
    @Provides @Named(DELETE_THREAD_UI_EVENT) @Singleton
    public GenericUiObservable providesEditTextUiEvent(
            ClickableListView<ThreadResource> onLongPressObserver,
            final Context appContext,
            final VolleyRequest<DeletePostResourceReturnData> deleteRequest) {
        LongClickDeleteEvent event = new LongClickDeleteEvent(
                appContext, 
                onLongPressObserver, 
                deleteRequest);
        return event.getUiEvent();
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
    public ContextItemSelectedObserver providesContextSelectedHolder() {
        return mActivity;
    }
}
