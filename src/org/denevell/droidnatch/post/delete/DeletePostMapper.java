package org.denevell.droidnatch.post.delete;

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
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.post.delete.uievents.LongClickDeleteEvent;
import org.denevell.droidnatch.posts.list.ListPostsMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects= ListPostsFragment.class, complete = false)
public class DeletePostMapper {
    
    public static final String PROVIDES_DELETE_POST = "delete_post";
    private static final String DELETE_POST_UI_EVENT = "delete_thread_ui_event";
    @SuppressWarnings("unused")
    private ContextItemSelectedObserver mActivity;

    public DeletePostMapper(ContextItemSelectedObserver activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named(PROVIDES_DELETE_POST)
    public Controller providesController(
            ServiceFetcher<DeletePostResourceReturnData> service, 
            @Named(ListPostsMapper.PROVIDES_LIST_POSTS) Controller listPostsController, 
            @Named(DELETE_POST_UI_EVENT) GenericUiObservable uiEvent) {
        UiEventThenServiceCallController controller = 
                new UiEventThenServiceCallController(
                        uiEvent,
                        service,
                        null,
                        listPostsController);
        return controller;
    }
    
    @Provides @Named(DELETE_POST_UI_EVENT) @Singleton
    public GenericUiObservable providesEditTextUiEvent(
            OnLongPressObserver<PostResource> onLongPressObserver,
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
    
    @Provides @Singleton 
    public OnLongPressObserver<PostResource> providesOnLongPressObserver(
            @Named(ListPostsMapper.PROVIDES_LIST_POSTS_LISTVIEW) ClickableListView<PostResource> observer) {
        return observer;
    }    

}
