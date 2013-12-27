package org.denevell.droidnatch.post.deletethread;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestDELETE;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.post.delete.uievents.LongClickDeleteThreadEvent;
import org.denevell.droidnatch.post.deletethread.uievents.PreviousScreenEvent;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DeleteThreadFromPostMapper {
    
    public static final String PROVIDES_DELETE_THREAD_FROM_POST = "delete_thread_from_post";
    public static final String DELETE_THREAD_FROM_POST_UI_EVENT = "delete_thread_from_post_ui_event";
    private static final String DELETE_THREAD_GOTO_PREVIOUSSCREEN_UI_EVENT = "delete_thread_goto_previous_screen";
    private static final String DELETE_THREAD_FROM_POST_REQUEST = "delete_thread_from_post_request";
    private static final String DELETE_THREAD_FROM_POST_SERVICE = "delete_thread_from_post_service";

    public DeleteThreadFromPostMapper() {
    }
    
    @Provides @Singleton @Named(PROVIDES_DELETE_THREAD_FROM_POST)
    public Controller providesController(
            @Named(DELETE_THREAD_FROM_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> service, 
            @Named(DELETE_THREAD_FROM_POST_UI_EVENT) GenericUiObservable uiEvent,
            @Named(DELETE_THREAD_GOTO_PREVIOUSSCREEN_UI_EVENT) GenericUiObservable uiPreviousScreenEvent
            ) {
        UiEventThenServiceThenUiEvent controller = 
                new UiEventThenServiceThenUiEvent(
                        uiEvent,
                        service,
                        null,
                        uiPreviousScreenEvent);
        return controller;
    }
    
    @Provides @Named(DELETE_THREAD_FROM_POST_UI_EVENT) @Singleton
    public GenericUiObservable providesLongClickDeleteThreadUiEvent(
            OnLongPressObserver<PostResource> onLongPressObserver,
            final Context appContext,
            @Named(DELETE_THREAD_FROM_POST_REQUEST) final VolleyRequest<DeletePostResourceReturnData> deleteRequest
            ) {
        LongClickDeleteThreadEvent event = new LongClickDeleteThreadEvent(
                appContext, 
                onLongPressObserver, 
                deleteRequest);
        return event.getUiEvent();
    }    

    @Provides @Named(DELETE_THREAD_GOTO_PREVIOUSSCREEN_UI_EVENT) @Singleton
    public GenericUiObservable providesGotoPreviousScreenUiEvent(
            ScreenOpener screenOpener) {
        PreviousScreenEvent pse = new PreviousScreenEvent(screenOpener);
        return pse.getUiEvent();
    }    

    @Provides @Singleton @Named(DELETE_THREAD_FROM_POST_SERVICE)
    public ServiceFetcher<DeletePostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectStringConverter converter, 
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
            ObjectStringConverter reponseConverter,
            Context appContext
            ) {
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_del); 
        VolleyRequestDELETE<DeletePostResourceReturnData> vollyRequest = 
                new VolleyRequestDELETE<DeletePostResourceReturnData>();
        vollyRequest.setUrl(url);
        return vollyRequest;
    }     

}
