package org.denevell.droidnatch.threads.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ActivatingUiObject;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.di.AddThreadServicesMapper;
import org.denevell.droidnatch.threads.list.di.DeleteThreadServicesMapper;
import org.denevell.droidnatch.threads.list.di.ListThreadsServiceMapper;
import org.denevell.droidnatch.threads.list.di.ListThreadsUiEventMapper;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.uievents.LongClickDeleteUiEvent;
import org.denevell.natch.android.R;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class ListThreadsFragment extends ObservableFragment {
    private static final String TAG = ListThreadsFragment.class.getSimpleName();

    @Inject ServiceFetcher<ListThreadsResource> listThreadsService;
    @Inject ServiceFetcher<AddPostResourceReturnData> addPostService;
    @Inject ServiceFetcher<DeletePostResourceReturnData> deleteThreadService;
    @Inject ReceivingUiObject<ListThreadsResource> listViewReceivingUiObject;
    @Inject AddPostResourceInput addPostResourceInput;
    @Inject ScreenOpener screenOpener;
    @Inject ClickableListView<ThreadResource> onLongPressObserver;
    @Inject VolleyRequest<DeletePostResourceReturnData> deleteRequest;

    private void inject() {
        ObjectGraph.create(
                new ScreenOpenerMapper(getActivity()),
                new CommonMapper(getActivity()),
                new ListThreadsServiceMapper(),
                new ListThreadsUiEventMapper(getActivity(), this),
                new DeleteThreadServicesMapper(),
                new AddThreadServicesMapper()
        ).inject(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle(R.string.page_title_threads);
        View v = inflater.inflate(R.layout.list_threads_fragment, container, false);
        return v;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            inject();

            final Controller listThreadController =
                new UiEventThenServiceThenUiEvent<ListThreadsResource>(
                    null,
                    listThreadsService,
                    (ProgressIndicator) listViewReceivingUiObject,
                    listViewReceivingUiObject);
            listThreadController.setup();

            Controller deleteThreadController =
                new UiEventThenServiceThenUiEvent(
                    providesDeleteThreadUiActivator(),
                    deleteThreadService,
                    (ProgressIndicator) listViewReceivingUiObject,
                    new ReceivingUiObject() {
                        @Override
                        public void success(Object v) {
                            listThreadController.go();
                        }
                        @Override
                        public void fail(FailureResult r) { }
                    });
            deleteThreadController.setup().go();

        } catch (Exception e) {
            Log.e(TAG, "Failed to start di mapper", e);
            return;
        }    
    }

    private ActivatingUiObject providesDeleteThreadUiActivator() {
        ActivatingUiObject event = new LongClickDeleteUiEvent(
                getActivity(), 
                onLongPressObserver, 
                deleteRequest);
        return event;
    }      

}