package org.denevell.droidnatch.threads.list.uievents;

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
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.di.ListThreadsServiceMapper;
import org.denevell.droidnatch.threads.list.di.ListThreadsUiEventMapper;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class ListThreadsViewStarter extends View {

    @Inject ServiceFetcher<ListThreadsResource> listThreadsService;
    @Inject Receiver<ListThreadsResource> listViewReceivingUiObject;
    private UiEventThenServiceThenUiEvent controller;

    public static class CallControllerListThreads {}

    public ListThreadsViewStarter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void createObjectGraph() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                new ListThreadsServiceMapper(),
                new ListThreadsUiEventMapper((Activity) getContext())
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createObjectGraph();
        controller = new UiEventThenServiceThenUiEvent<ListThreadsResource>(
                null,
                listThreadsService,
                null,
                listViewReceivingUiObject).setup();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void getServiceRestartEvent(CallControllerListThreads event) {
        if(controller!=null)  {
            controller.onUiEventActivated();
        }
    }
}
