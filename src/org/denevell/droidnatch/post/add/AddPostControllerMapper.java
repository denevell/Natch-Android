package org.denevell.droidnatch.post.add;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.post.add.uievents.AddPostTextEditGenericUiEvent;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.di.ListPostsControllerMapper;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false)
public class AddPostControllerMapper {
    
    public static final String PROVIDES_ADD_POST = "add_post";
    private Activity mActivity;

    public AddPostControllerMapper(Fragment activity) {
        mActivity = activity.getActivity();
    }

    @Provides @Singleton @Named(PROVIDES_ADD_POST)
    public Controller providesLoginController(
            ServiceFetcher<AddPostResourceReturnData> service, 
            GenericUiObservable uiEvent, 
            @Named(ListPostsControllerMapper.PROVIDES_LIST_POSTS) Controller nextController,
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
    public GenericUiObservable providesEditTextUiEvent(
            AddPostResourceInput resourceInput) {
        EditText editText = (EditText) mActivity.findViewById(R.id.editText1);
        GenericUiObservable genericUiEvent = 
                new AddPostTextEditGenericUiEvent(
                        editText, 
                        resourceInput);
        return genericUiEvent;
    }
    
}
