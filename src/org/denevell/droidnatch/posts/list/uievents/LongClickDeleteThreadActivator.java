package org.denevell.droidnatch.posts.list.uievents;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.di.DeleteThreadFromPostServicesMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class LongClickDeleteThreadActivator extends View implements Activator {

    private GenericUiObserver mCallback;
    @Inject @Named(DeleteThreadFromPostServicesMapper.DELETE_THREAD_FROM_VOLLEY_REQUEST) VolleyRequest<DeletePostResourceReturnData> mDeleteRequest;
    @Inject @Named(DeleteThreadFromPostServicesMapper.DELETE_THREAD_FROM_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> deleteThreadService;
    @Inject ScreenOpener screenOpener;

    public LongClickDeleteThreadActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ObjectGraph.create(
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                new CommonMapper((Activity) getContext()),
                new DeleteThreadFromPostServicesMapper()
        ).inject(this);
        UiEventThenServiceThenUiEvent deleteThreadFromPostController =
                new UiEventThenServiceThenUiEvent<DeletePostResourceReturnData>(
                        this,
                        deleteThreadService,
                        null,
                        new PreviousScreenReceiver(screenOpener));
        deleteThreadFromPostController.setup();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(ClickableListView.LongPressListViewEvent obj) {
        if(obj.ob instanceof PostResource) {
            PostResource tr = (PostResource) obj.ob;
            if(obj.index==0) {
                String url = Urls.getBasePath() + getContext().getString(R.string.url_del);
                mDeleteRequest.setUrl(url + tr.getId());
                mCallback.onUiEventActivated();
            }
        }
    }

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(Object result) {
    }

    @Override
    public void fail(FailureResult r) {
    }
}
