package org.denevell.droidnatch.threads.list.uievents;

import java.util.List;

import javax.inject.Inject;

import org.denevell.droidnatch.AppWideMapper;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class ListThreadsViewStarter extends View {

    @Inject ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> mListViewReceivingUiObject;
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
                new ListThreadsMapper((Activity) getContext())
        ).inject(this);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createObjectGraph();

        controller = new UiEventThenServiceThenUiEvent<ListThreadsResource>(
                mListThreadsService,
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
