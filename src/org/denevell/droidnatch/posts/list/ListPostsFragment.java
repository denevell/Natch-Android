package org.denevell.droidnatch.posts.list;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.posts.list.uievents.AddPostViewActivator;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListPostsFragment extends ObservableFragment {
    
    public static final String BUNDLE_KEY_THREAD_ID = "thread_id";
    public static final String BUNDLE_KEY_THREAD_NAME = "thread_name";
    private static final String TAG = ListPostsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        String threadName = getArguments().getString(BUNDLE_KEY_THREAD_NAME);
        getActivity().setTitle(threadName);
        View v = inflater.inflate(R.layout.posts_lists_fragment, container, false);
        return v;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
        try {
            AddPostViewActivator addPost = (AddPostViewActivator) getActivity().findViewById(R.id.list_posts_addpost_edittext);
            addPost.setup(getArguments());
            ListPostsViewStarter listPosts = (ListPostsViewStarter) getActivity().findViewById(R.id.list_posts_listpostsview_holder);
            listPosts.setup(getArguments());
        } catch (Exception e) {
            Log.e(TAG, "Setup views", e);
            return;
        }            
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	if(getArguments()!=null && getActivity().getTitle()!=null) {
    		getArguments().putString(BUNDLE_KEY_THREAD_NAME, getActivity().getTitle().toString());
    	}
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	new ListPostsOptionsMenu().create(menu, inflater);
    }
    
}
