package org.denevell.droidnatch.post.delete;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.post.delete.uievents.LongClickDeletePostUiEvent;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.di.ListPostsControllerMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = ListPostsFragment.class, complete = false)
public class DeletePostControllerMapper {
    
    public static final String PROVIDES_DELETE_POST = "delete_post";
    private static final String PROVIDES_DELETE_POST_UI_EVENT = "delete_post_ui_event";
    @SuppressWarnings("unused") private ContextItemSelectedObserver mActivity;

    public DeletePostControllerMapper(ContextItemSelectedObserver activity) {
        mActivity = activity;
    }
  
    @Provides @Singleton @Named(PROVIDES_DELETE_POST)
    public Controller providesController(
            ServiceFetcher<DeletePostResourceReturnData> service, 
            @Named(ListPostsControllerMapper.PROVIDES_LIST_POSTS) Controller listPostsController, 
            @Named(PROVIDES_DELETE_POST_UI_EVENT) GenericUiObservable uiEvent) {
        UiEventThenServiceCallController controller = 
                new UiEventThenServiceCallController(
                        uiEvent,
                        service,
                        null,
                        listPostsController);
        return controller;
    }
    
    @Provides @Named(PROVIDES_DELETE_POST_UI_EVENT) @Singleton
    public GenericUiObservable providesDeletePostEvent(
            ClickableListView<PostResource> longPressObserver,
            Context appContext,
            VolleyRequest<DeletePostResourceReturnData> deleteRequest) {
        LongClickDeletePostUiEvent event = new LongClickDeletePostUiEvent(
                appContext, 
                longPressObserver, 
                deleteRequest);
        return event;
    }

    
}