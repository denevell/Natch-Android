package org.denevell.droidnatch.posts.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.AppWideMapper;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.di.ListPostsServiceMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsUiEventMapper;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class ListPostsViewStarter extends View {

    private UiEventThenServiceThenUiEvent<ThreadResource> controller;
    public static class CallControllerListPosts {}
    @Inject ServiceFetcher<ThreadResource> listPostsService;
    @Inject Receiver<ThreadResource> listViewReceivingUiObject;

    public ListPostsViewStarter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
    }

    @SuppressWarnings("unchecked")
	public void setup(Bundle bundle) {
        String threadId = bundle.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID);
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                AppWideMapper.getInstance(),
                new ListPostsServiceMapper(bundle),
                new ListPostsUiEventMapper((Activity) getContext(), threadId)
        ).inject(this);
        controller =
                new UiEventThenServiceThenUiEvent<ThreadResource>(
                        null,
                        listPostsService,
                        (ProgressIndicator) listViewReceivingUiObject,
                        listViewReceivingUiObject);
        controller.setup();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void listItYeah(CallControllerListPosts events) {
        if(controller!=null) {
            controller.onUiEventActivated();
        }
    }
}
