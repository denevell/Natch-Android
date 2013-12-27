package org.denevell.droidnatch.post.add;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestPUTImpl;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.post.add.uievents.AddPostTextEditGenericUiEvent;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.ListPostsMapper;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false)
public class AddPostMapper {
    
    public static final String PROVIDES_ADD_POST = "add_post";
    private Activity mActivity;
    private Bundle mBundle;

    public AddPostMapper(Fragment activity) {
        mActivity = activity.getActivity();
        mBundle = activity.getArguments();        
    }

    @Provides @Singleton @Named(PROVIDES_ADD_POST)
    public Controller providesLoginController(
            ServiceFetcher<AddPostResourceReturnData> service, 
            GenericUiObservable uiEvent, 
            @Named(ListPostsMapper.PROVIDES_LIST_POSTS) Controller nextController,
            final AddPostResourceInput resourceInput) {
        UiEventThenServiceCallController controller = 
                new UiEventThenServiceCallController(
                        uiEvent, 
                        service,
                        null,
                        nextController);
        return controller;
    }

    @Provides @Singleton
    public ServiceFetcher<AddPostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<AddPostResourceReturnData> volleyRequest) {
        return new BaseService<AddPostResourceReturnData>(
                appContext, 
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                AddPostResourceReturnData.class);
    }

    @Provides @Singleton
    public AddPostResourceInput providesThreadInput() {
        String threadId = mBundle.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID);
        AddPostResourceInput addPostResourceInput = new AddPostResourceInput();
        addPostResourceInput.setThreadId(threadId);
        return addPostResourceInput;
    }
    
    @Provides @Singleton 
    public VolleyRequest<AddPostResourceReturnData> providesRequest(
            ObjectStringConverter reponseConverter,
            AddPostResourceInput body,
            Context appContext) {
        VolleyRequestPUTImpl<AddPostResourceReturnData> vollyRequest = 
                new VolleyRequestPUTImpl<AddPostResourceReturnData>(
                    reponseConverter, 
                    body);
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_add_post);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

    @Provides @Singleton
    public GenericUiObservable providesEditTextUiEvent(
            EditText textInput, 
            AddPostResourceInput resourceInput) {
        GenericUiObservable genericUiEvent = 
                new AddPostTextEditGenericUiEvent(
                        textInput, 
                        resourceInput)
            .getGenericUiEvent();
        return genericUiEvent;
    }
    
    @Provides @Singleton
    public EditText providesEditText() {
        final EditText editText = (EditText) mActivity.findViewById(R.id.editText1);
        return editText;
    }

}
