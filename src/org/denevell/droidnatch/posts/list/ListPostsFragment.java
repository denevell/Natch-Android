package org.denevell.droidnatch.posts.list;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.post.add.AddPostControllerMapper;
import org.denevell.droidnatch.post.delete.DeletePostControllerMapper;
import org.denevell.droidnatch.post.delete.DeletePostServicesMapper;
import org.denevell.droidnatch.post.deletethread.DeleteThreadFromPostControllerMapper;
import org.denevell.droidnatch.post.deletethread.DeleteThreadFromPostServicesMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsControllerMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsResultsDisplayableMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsServiceMapper;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dagger.ObjectGraph;

public class ListPostsFragment extends ObservableFragment {
    
    public static final String BUNDLE_KEY_THREAD_ID = "thread_id";
    public static final String BUNDLE_KEY_THREAD_NAME = "thread_name";
    private static final String TAG = ListPostsFragment.class.getSimpleName();
    @Inject @Named(ListPostsControllerMapper.PROVIDES_LIST_POSTS) Controller mControllerListPosts;
    @Inject @Named(AddPostControllerMapper.PROVIDES_ADD_POST) Controller mControllerAddPost;
    @Inject @Named(DeletePostControllerMapper.PROVIDES_DELETE_POST) Controller mControllerDeletePost;
    @Inject @Named(DeleteThreadFromPostControllerMapper.PROVIDES_DELETE_THREAD_FROM_POST) Controller mControllerDeleteThreadFromPostController;

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

                    new AddPostControllerMapper(this),

                    new DeleteThreadFromPostServicesMapper(),
                    new DeleteThreadFromPostControllerMapper()
                    )
                    .inject(this);
            mControllerListPosts.setup().go();
            mControllerAddPost.setup().go();
            mControllerDeletePost.setup().go();
            mControllerDeleteThreadFromPostController.setup().go();            
        } catch (Exception e) {
            Log.e(TAG, "Failed to start mapper", e);
            return;
        }            
    }

}
