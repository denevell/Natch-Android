package org.denevell.droidnatch.posts.list;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.post.add.AddPostServicesMapper;
import org.denevell.droidnatch.post.add.uievents.AddPostTextEditGenericUiEvent;
import org.denevell.droidnatch.post.delete.DeletePostControllerMapper;
import org.denevell.droidnatch.post.delete.DeletePostServicesMapper;
import org.denevell.droidnatch.post.deletethread.DeleteThreadFromPostControllerMapper;
import org.denevell.droidnatch.post.deletethread.DeleteThreadFromPostServicesMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsControllerMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsResultsDisplayableMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsServiceMapper;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import dagger.ObjectGraph;

public class ListPostsFragment extends ObservableFragment {
    
    public static final String BUNDLE_KEY_THREAD_ID = "thread_id";
    public static final String BUNDLE_KEY_THREAD_NAME = "thread_name";
    private static final String TAG = ListPostsFragment.class.getSimpleName();
    @Inject @Named(ListPostsControllerMapper.PROVIDES_LIST_POSTS) Controller mControllerListPosts;
    @Inject @Named(DeletePostControllerMapper.PROVIDES_DELETE_POST) Controller mControllerDeletePost;
    @Inject @Named(DeleteThreadFromPostControllerMapper.PROVIDES_DELETE_THREAD_FROM_POST) Controller mControllerDeleteThreadFromPostController;
    @Inject ServiceFetcher<AddPostResourceReturnData> addPostService; 
    @Inject AddPostResourceInput addPostResourceInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String threadName = getArguments().getString(BUNDLE_KEY_THREAD_NAME);
        getActivity().setTitle(threadName);
        View v = inflater.inflate(R.layout.list_posts_fragment, container, false);
        return v;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            ObjectGraph.create(
                    new ScreenOpenerMapper(getActivity()),
                    new CommonMapper(getActivity()),

                    new ListPostsControllerMapper(),
                    new ListPostsResultsDisplayableMapper(this),
                    new ListPostsServiceMapper(this),

                    new DeletePostControllerMapper(this),
                    new DeletePostServicesMapper(),

                    new AddPostServicesMapper(this),

                    new DeleteThreadFromPostServicesMapper(),
                    new DeleteThreadFromPostControllerMapper()
                    )
                    .inject(this);
            mControllerListPosts.setup().go();
            mControllerDeletePost.setup().go();
            mControllerDeleteThreadFromPostController.setup().go();            

            UiEventThenServiceCallController addPostController = 
                    new UiEventThenServiceCallController(
                            providesAddPostTextUiEvent(addPostResourceInput), 
                            addPostService,
                            null,
                            mControllerListPosts);
            addPostController.setup().go();

        } catch (Exception e) {
            Log.e(TAG, "Failed to start mapper", e);
            return;
        }            
    }
    
    private GenericUiObservable providesAddPostTextUiEvent(AddPostResourceInput resourceInput) {
        EditText editText = (EditText) getActivity().findViewById(R.id.editText1);
        GenericUiObservable genericUiEvent = 
                new AddPostTextEditGenericUiEvent(
                        editText, 
                        resourceInput);
        return genericUiEvent;
    }    

}
