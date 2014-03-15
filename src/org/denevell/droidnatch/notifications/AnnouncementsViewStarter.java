package org.denevell.droidnatch.notifications;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.uievents.StoreReferenceToLatestPostReceiver;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import dagger.ObjectGraph;

public class AnnouncementsViewStarter extends View {

    @Inject @Named("announce") ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> mListViewReceivingUiObject;
	@Inject @Named("announce") ServiceFetcher<Void, ListThreadsResource> mListThreadsService;

    public static class CallControllerListThreads {}

    public AnnouncementsViewStarter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void createObjectGraph() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                new AnnouncementsMapper((Activity) getContext())
        ).inject(this);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createObjectGraph();

        new UiEventThenServiceThenUiEvent<ListThreadsResource>(
                mListThreadsService,
                mListViewReceivingUiObject,
                new StoreReferenceToLatestPostReceiver(getContext().getApplicationContext()))
                	.setup();

        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

}
