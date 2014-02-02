package org.denevell.droidnatch.posts.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.ServiceCallThenDisplayController;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ActivatingUiObject;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.di.AddPostServicesMapper;
import org.denevell.droidnatch.posts.list.di.DeletePostServicesMapper;
import org.denevell.droidnatch.posts.list.di.DeleteThreadFromPostServicesMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsServiceMapper;
import org.denevell.droidnatch.posts.list.di.resultsdisplayable.ListPostsResultsDisplayableMapper;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.droidnatch.posts.list.entities.ListPostsResourceToArrayList;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.posts.list.uievents.AddPostTextEditGenericUiEvent;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeletePostUiEvent;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeleteThreadUiEvent;
import org.denevell.droidnatch.posts.list.uievents.PreviousScreenUiEvent;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class ListPostsFragment extends ObservableFragment {
    
    public static final String BUNDLE_KEY_THREAD_ID = "thread_id";
    public static final String BUNDLE_KEY_THREAD_NAME = "thread_name";
    private static final String TAG = ListPostsFragment.class.getSimpleName();
    @Inject ServiceFetcher<AddPostResourceReturnData> addPostService;

    @Inject @Named(DeleteThreadFromPostServicesMapper.DELETE_THREAD_FROM_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> deleteThreadService;
    @Inject @Named(DeleteThreadFromPostServicesMapper.DELETE_THREAD_FROM_VOLLEY_REQUEST) VolleyRequest<DeletePostResourceReturnData> deleteThreadVolleyRequest;
    @Inject @Named(DeletePostServicesMapper.DELETE_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> deletePostService;
    @Inject @Named(DeletePostServicesMapper.DELETE_POST_VOLLEY_REQUEST) VolleyRequest<DeletePostResourceReturnData> deletePostVolleyRequest;

    @Inject ServiceFetcher<ListPostsResource> listPostsService;
    @Inject ResultsDisplayer<List<PostResource>> resultsDisplayable;
    @Inject AddPostResourceInput addPostResourceInput;
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

                    new ListPostsResultsDisplayableMapper(this),
                    new ListPostsServiceMapper(this),

                    new DeletePostServicesMapper(),
                    new DeleteThreadFromPostServicesMapper(),

                    new AddPostServicesMapper(this))
                    .inject(this);

            ServiceCallThenDisplayController<ListPostsResource, List<PostResource>> listPostController = 
                    new ServiceCallThenDisplayController<ListPostsResource, List<PostResource>>(
                    listPostsService, 
                    resultsDisplayable,
                    new ListPostsResourceToArrayList());
            listPostController.setup().go();

            UiEventThenServiceCallController addPostController =
                    new UiEventThenServiceCallController(
                            providesAddPostTextUiEvent(addPostResourceInput),
                            addPostService,
                            null,
                            listPostController);
            addPostController.setup().go();

            UiEventThenServiceCallController deletePostController =
                    new UiEventThenServiceCallController(
                            providesDeletePostClickEvent(),
                            deletePostService,
                            null,
                            listPostController);
            deletePostController.setup().go();

            UiEventThenServiceThenUiEvent deleteThreadFromPostController =
                    new UiEventThenServiceThenUiEvent(
                            providesLongClickDeleteThreadUiEvent(),
                            deleteThreadService,
                            null,
                            providesGotoPreviousScreenUiEvent());
            deleteThreadFromPostController.setup().go();

        } catch (Exception e) {
            Log.e(TAG, "Failed to start mapper", e);
            return;
        }            
    }
    
    private ActivatingUiObject providesAddPostTextUiEvent(AddPostResourceInput resourceInput) {
        EditText editText = (EditText) getActivity().findViewById(R.id.editText1);
        ActivatingUiObject genericUiEvent =
                new AddPostTextEditGenericUiEvent(
                        editText, 
                        resourceInput);
        return genericUiEvent;
    }    
    
    private ActivatingUiObject providesDeletePostClickEvent() {
        ActivatingUiObject event = new LongClickDeletePostUiEvent(
                getActivity(), 
                listView, 
                deletePostVolleyRequest);
        return event;
    }    
    
    private ActivatingUiObject providesLongClickDeleteThreadUiEvent() {
        ActivatingUiObject event = new LongClickDeleteThreadUiEvent(
                getActivity(),
                listView,
                deleteThreadVolleyRequest);
        return event;
    }

    private ReceivingUiObject providesGotoPreviousScreenUiEvent() {
        PreviousScreenUiEvent pse = new PreviousScreenUiEvent(screenOpener);
        return pse;
    }        

}
