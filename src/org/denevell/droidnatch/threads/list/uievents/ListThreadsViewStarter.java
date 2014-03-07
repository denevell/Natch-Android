package org.denevell.droidnatch.threads.list.uievents;

import java.util.List;

import javax.inject.Inject;

import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

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
        		PaginationMapper.getInstance(),
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

    @Subscribe
    public void getServiceRestartEvent(CallControllerListThreads event) {
        if(controller!=null)  {
            controller.onUiEventActivated();
        }
    }

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		if(R.id.threads_option_menu_refresh!= menu.item.getItemId()) return;
        if(controller!=null)  {
            controller.onUiEventActivated();
        }
	}    
    
}
