package org.denevell.droidnatch.posts.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.di.DeletePostServicesMapper;
import org.denevell.droidnatch.posts.list.di.DeleteThreadFromPostServicesMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsServiceMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsUiEventMapper;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeletePostUiEvent;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeleteThreadUiEvent;
import org.denevell.droidnatch.posts.list.uievents.PreviousScreenUiEvent;
import org.denevell.droidnatch.posts.list.views.AddPostTextEditGenericUiEvent;
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

    @Inject ServiceFetcher<ListPostsResource> listPostsService;
    @Inject Receiver<ListPostsResource> listViewReceivingUiObject;
    @Inject ClickableListView<PostResource> listView;
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
                    new ListPostsUiEventMapper(this),
                    new ListPostsServiceMapper(this),
                    new DeletePostServicesMapper(),
                    new DeleteThreadFromPostServicesMapper())
                    .inject(this);

            AddPostTextEditGenericUiEvent addPost = (AddPostTextEditGenericUiEvent) getActivity().findViewById(R.id.list_posts_addpost_edittext);
            addPost.setup(getArguments());

            final UiEventThenServiceThenUiEvent<ListPostsResource> listPostController =
                    new UiEventThenServiceThenUiEvent<ListPostsResource>(
                    null,
                    listPostsService,
                    (ProgressIndicator) listViewReceivingUiObject,
                            listViewReceivingUiObject) {
                        @Override
                        public UiEventThenServiceThenUiEvent setup() {
                            EventBus.getBus().register(this);
                            return super.setup();
                        }
                        @Subscribe
                        public void listItYeah(CallControllerListPosts events) {
                            onUiEventActivated();
                        }
                    };
            listPostController.setup();

            Receiver listPostControllerConverter = new Receiver() {
                @Override
                public void success(Object result) {
                    listPostController.go();
                }

                @Override
                public void fail(FailureResult r) {

                }
            };
            UiEventThenServiceThenUiEvent deletePostController =
                    new UiEventThenServiceThenUiEvent (
                            providesDeletePostClickActivator(),
                            deletePostService,
                            null,
                            listPostControllerConverter);
            deletePostController.setup().go();

            UiEventThenServiceThenUiEvent deleteThreadFromPostController =
                    new UiEventThenServiceThenUiEvent<DeletePostResourceReturnData>(
                            providesLongClickDeleteThreadUiActivator(),
                            deleteThreadService,
                            null,
                            providesGotoPreviousScreenUiReceiver());
            deleteThreadFromPostController.setup();

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
    
    private Activator providesLongClickDeleteThreadUiActivator() {
        Activator event = new LongClickDeleteThreadUiEvent(
                getActivity(),
                deleteThreadVolleyRequest);
        return event;
    }

    private Receiver providesGotoPreviousScreenUiReceiver() {
        PreviousScreenUiEvent pse = new PreviousScreenUiEvent(screenOpener);
        return pse;
    }

    public static class CallControllerListPosts {}
}
