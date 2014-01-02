package org.denevell.droidnatch.post.deletethread;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.post.deletethread.uievents.LongClickDeleteThreadUiEvent;
import org.denevell.droidnatch.post.deletethread.uievents.PreviousScreenUiEvent;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DeleteThreadFromPostControllerMapper {
    
    public static final String PROVIDES_DELETE_THREAD_FROM_POST = "delete_thread_from_post";
    public static final String DELETE_THREAD_FROM_POST_UI_EVENT = "delete_thread_from_post_ui_event";
    private static final String DELETE_THREAD_GOTO_PREVIOUSSCREEN_UI_EVENT = "delete_thread_goto_previous_screen";
    private static final String DELETE_THREAD_FROM_POST_REQUEST = "delete_thread_from_post_request";
    private static final String DELETE_THREAD_FROM_POST_SERVICE = "delete_thread_from_post_service";

    public DeleteThreadFromPostControllerMapper() {
    }
    
    @Provides @Singleton @Named(PROVIDES_DELETE_THREAD_FROM_POST)
    public Controller providesController(
            @Named(DELETE_THREAD_FROM_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> service, 
            @Named(DELETE_THREAD_FROM_POST_UI_EVENT) GenericUiObservable uiEvent,
            @Named(DELETE_THREAD_GOTO_PREVIOUSSCREEN_UI_EVENT) GenericUiObservable uiPreviousScreenEvent) {
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
            ClickableListView<PostResource> onLongPressObserver,
            final Context appContext,
            @Named(DELETE_THREAD_FROM_POST_REQUEST) final VolleyRequest<DeletePostResourceReturnData> deleteRequest) {
        LongClickDeleteThreadUiEvent event = new LongClickDeleteThreadUiEvent(
                appContext, 
                onLongPressObserver, 
                deleteRequest);
        return event;
    }    

    @Provides @Named(DELETE_THREAD_GOTO_PREVIOUSSCREEN_UI_EVENT) @Singleton
    public GenericUiObservable providesGotoPreviousScreenUiEvent(
            ScreenOpener screenOpener) {
        PreviousScreenUiEvent pse = new PreviousScreenUiEvent(screenOpener);
        return pse;
    }    

}
