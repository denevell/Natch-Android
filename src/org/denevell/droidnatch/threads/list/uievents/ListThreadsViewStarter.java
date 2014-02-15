package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.di.ListThreadsServiceMapper;
import org.denevell.droidnatch.threads.list.di.ListThreadsServiceMapper.PaginationObject;
import org.denevell.droidnatch.threads.list.di.ListThreadsServiceMapper.PaginationUpdater;
import org.denevell.droidnatch.threads.list.di.ListThreadsUiEventMapper;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class ListThreadsViewStarter extends View {

    @Inject ServiceFetcher<ListThreadsResource> mListThreadsService;
    @Inject Receiver<ListThreadsResource> mListViewReceivingUiObject;
    @Inject PaginationObject mPaginationObject;
    @Inject PaginationUpdater mPaginationUpdater;
    private UiEventThenServiceThenUiEvent<ListThreadsResource> controller;

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

    @SuppressWarnings("unchecked")
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createObjectGraph();
        controller = new UiEventThenServiceThenUiEvent<ListThreadsResource>(
                null,
                mListThreadsService,
                null,
                mListViewReceivingUiObject, mPaginationUpdater).setup();
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
