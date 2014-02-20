package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.AppWideMapper;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.ListThreadsUiEventMapper;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class ListThreadsViewStarter extends View {

    @Inject Receiver<ListThreadsResource> mListViewReceivingUiObject;
    private UiEventThenServiceThenUiEvent<ListThreadsResource> controller;
	@Inject ServiceFetcher<Void, ListThreadsResource> mListThreadsService;

    public static class CallControllerListThreads {}

    public ListThreadsViewStarter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void createObjectGraph() {
        ObjectGraph.create(
        		AppWideMapper.getInstance(),
                new CommonMapper((Activity) getContext()),
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                new ListThreadsUiEventMapper((Activity) getContext())
        ).inject(this);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createObjectGraph();

        controller = new UiEventThenServiceThenUiEvent<ListThreadsResource>(
                null,
                mListThreadsService,
                null,
                mListViewReceivingUiObject).setup();

        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void getServiceRestartEvent(CallControllerListThreads event) {
        if(controller!=null)  {
            controller.onUiEventActivated();
        }
    }
}
