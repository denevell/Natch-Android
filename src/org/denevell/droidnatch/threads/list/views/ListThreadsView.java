package org.denevell.droidnatch.threads.list.views;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.di.ListThreadsServiceMapper;
import org.denevell.droidnatch.threads.list.di.ListThreadsUiEventMapper;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class ListThreadsView extends View {

    @Inject ServiceFetcher<ListThreadsResource> listThreadsService;
    @Inject ReceivingUiObject<ListThreadsResource> listViewReceivingUiObject;

    public ListThreadsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void createObjectGraph() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                new ListThreadsServiceMapper(),
                new ListThreadsUiEventMapper(((Activity) getContext()), new ContextItemSelectedObserver() {
                    @Override
                    public void addContextItemSelectedCallback(ContextItemSelected contextItem) {

                    }
                })
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createObjectGraph();
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
            public void callController(ListThreadsFragment.CallControllerListThreads ob) {
                onUiEventActivated();
            }
        }.setup();
    }
}
