package org.denevell.droidnatch.notifications;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.newfivefour.android.manchester.R;
import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class AnnouncementsViewStarter extends View {

    @Inject @Named("announce") ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> mListViewReceivingUiObject;
	@Inject @Named("announce") ServiceFetcher<Void, ListThreadsResource> mListThreadsService;
	@SuppressWarnings("rawtypes")
	private UiEventThenServiceThenUiEvent controller;

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

        controller = new UiEventThenServiceThenUiEvent<ListThreadsResource>(
                mListThreadsService,
                mListViewReceivingUiObject)
                	.setup();

        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		if(R.id.threads_option_menu_refresh!= menu.item.getItemId()) return;
        if(controller!=null)  {
            controller.onUiEventActivated();
        }
	}    
    

}
