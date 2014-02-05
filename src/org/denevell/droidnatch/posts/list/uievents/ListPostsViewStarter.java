package org.denevell.droidnatch.posts.list.uievents;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.list.di.ListPostsServiceMapper;
import org.denevell.droidnatch.posts.list.di.ListPostsUiEventMapper;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class ListPostsViewStarter extends View {

    private UiEventThenServiceThenUiEvent controller;
    public static class CallControllerListPosts {}
    @Inject ServiceFetcher<ListPostsResource> listPostsService;
    @Inject Receiver<ListPostsResource> listViewReceivingUiObject;

    public ListPostsViewStarter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
    }

    public void setup(Bundle bundle) {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new ListPostsServiceMapper(bundle),
                new ListPostsUiEventMapper((Activity) getContext())
        ).inject(this);
        controller =
                new UiEventThenServiceThenUiEvent<ListPostsResource>(
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
