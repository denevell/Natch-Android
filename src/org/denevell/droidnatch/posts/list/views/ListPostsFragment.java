package org.denevell.droidnatch.posts.list.views;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.post.add.AddPostMapper;
import org.denevell.droidnatch.post.delete.DeletePostMapper;
import org.denevell.droidnatch.posts.list.ListPostsMapper;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dagger.ObjectGraph;

public class ListPostsFragment extends ObservableFragment {
    
    public static final String BUNDLE_KEY_THREAD_ID = "thread_id";
    private static final String TAG = ListPostsFragment.class.getSimpleName();
    @Inject @Named(ListPostsMapper.PROVIDES_LIST_POSTS) Controller mControllerListPosts;
    @Inject @Named(AddPostMapper.PROVIDES_ADD_POST) Controller mControllerAddPost;
    @Inject @Named(DeletePostMapper.PROVIDES_DELETE_POST) Controller mControllerDeletePost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.list_posts_fragment, container, false);
        return v;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            ObjectGraph.create(
                    new CommonMapper(getActivity()),
                    new ListPostsMapper(this),
                    new DeletePostMapper(this),
                    new AddPostMapper(this)
                    )
                    .inject(this);
            mControllerListPosts.setup().go();
            mControllerAddPost.setup().go();
            mControllerDeletePost.setup().go();
        } catch (Exception e) {
            Log.e(TAG, "Failed to start mapper", e);
            return;
        }            
    }

}
