package org.denevell.droidnatch.post.delete_thread;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.post.delete.uievents.LongClickDeleteThreadEvent;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DeleteThreadFromPostMapper {
    
    public static final String PROVIDES_DELETE_THREAD_FROM_POST = "delete_thread_from_post";
    public static final String DELETE_THREAD_FROM_POST_UI_EVENT = "delete_thread_from_post_ui_event";

    public DeleteThreadFromPostMapper() {
    }
    
    @Provides @Singleton @Named(PROVIDES_DELETE_THREAD_FROM_POST)
    public Controller providesController(
            ServiceFetcher<DeletePostResourceReturnData> service, 
            @Named(DELETE_THREAD_FROM_POST_UI_EVENT) GenericUiObservable uiEvent) {
        UiEventThenServiceCallController controller = 
                new UiEventThenServiceCallController(
                        uiEvent,
                        service,
                        null,
                        new UiEventThenServiceCallController.NextControllerNeverHalter(),
                        null);
        return controller;
    }
    
    @Provides @Named(DELETE_THREAD_FROM_POST_UI_EVENT) @Singleton
    public GenericUiObservable providesEditTextUiEvent(
            OnLongPressObserver<PostResource> onLongPressObserver,
            final Context appContext,
            final VolleyRequest<DeletePostResourceReturnData> deleteRequest) {
        LongClickDeleteThreadEvent event = new LongClickDeleteThreadEvent(
                appContext, 
                onLongPressObserver, 
                deleteRequest);
        return event.getUiEvent();
    }    

}
