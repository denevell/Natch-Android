package org.denevell.droidnatch.threads.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.di.ListThreadsServiceMapper;
import org.denevell.droidnatch.threads.list.di.ListThreadsUiEventMapper;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.natch.android.R;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class ListThreadsFragment extends ObservableFragment {
    public static class CallControllerListThreads {}
    private static final String TAG = ListThreadsFragment.class.getSimpleName();

    @Inject ServiceFetcher<ListThreadsResource> listThreadsService;
    @Inject ReceivingUiObject<ListThreadsResource> listViewReceivingUiObject;

    private void inject() {
        ObjectGraph.create(
                new CommonMapper(getActivity()),
                new ScreenOpenerMapper(getActivity()),
                new ListThreadsServiceMapper(),
                new ListThreadsUiEventMapper(getActivity(), this)
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
                    null,
                    listViewReceivingUiObject) {
                        @Override
                        public UiEventThenServiceThenUiEvent setup() {
                            EventBus.getBus().register(this);
                            return super.setup();
                        }

                        @Subscribe
                        public void callController(CallControllerListThreads ob) {
                            onUiEventActivated();
                        }
                };
            listThreadController.setup();

        } catch (Exception e) {
            Log.e(TAG, "Failed to start di mapper", e);
            return;
        }    
    }

}