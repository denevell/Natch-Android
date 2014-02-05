package org.denevell.droidnatch.posts.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.di.DeletePostServicesMapper;
import org.denevell.droidnatch.posts.list.di.DeleteThreadFromPostServicesMapper;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeletePostUiEvent;
import org.denevell.droidnatch.posts.list.views.AddPostTextEditGenericUiEvent;
import org.denevell.droidnatch.posts.list.views.ListPostsView;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class ListPostsFragment extends ObservableFragment {
    
    public static final String BUNDLE_KEY_THREAD_ID = "thread_id";
    public static final String BUNDLE_KEY_THREAD_NAME = "thread_name";
    private static final String TAG = ListPostsFragment.class.getSimpleName();

    @Inject @Named(DeleteThreadFromPostServicesMapper.DELETE_THREAD_FROM_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> deleteThreadService;
    @Inject @Named(DeleteThreadFromPostServicesMapper.DELETE_THREAD_FROM_VOLLEY_REQUEST) VolleyRequest<DeletePostResourceReturnData> deleteThreadVolleyRequest;
    @Inject @Named(DeletePostServicesMapper.DELETE_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> deletePostService;
    @Inject @Named(DeletePostServicesMapper.DELETE_POST_VOLLEY_REQUEST) VolleyRequest<DeletePostResourceReturnData> deletePostVolleyRequest;

    @Inject ScreenOpener screenOpener;

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
                    new DeletePostServicesMapper(),
                    new DeleteThreadFromPostServicesMapper())
                    .inject(this);

            AddPostTextEditGenericUiEvent addPost = (AddPostTextEditGenericUiEvent) getActivity().findViewById(R.id.list_posts_addpost_edittext);
            addPost.setup(getArguments());
            ListPostsView listPosts = (ListPostsView) getActivity().findViewById(R.id.list_posts_listpostsview_holder);
            listPosts.setup(getArguments());

            Receiver listPostControllerConverter = new Receiver() {
                @Override
                public void success(Object result) {
                    EventBus.getBus().post(new ListPostsView.CallControllerListPosts());
                }
                @Override public void fail(FailureResult r) {  }
            };
            UiEventThenServiceThenUiEvent deletePostController =
                    new UiEventThenServiceThenUiEvent (
                            providesDeletePostClickActivator(),
                            deletePostService,
                            null,
                            listPostControllerConverter);
            deletePostController.setup().go();

        } catch (Exception e) {
            Log.e(TAG, "Failed to start mapper", e);
            return;
        }            
    }
    
    private Activator providesDeletePostClickActivator() {
        Activator event = new LongClickDeletePostUiEvent(
                getActivity(), 
                deletePostVolleyRequest);
        return event;
    }    

}
