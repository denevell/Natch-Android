package org.denevell.droidnatch.posts.list.uievents;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.di.DeletePostServicesMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class LongClickDeletePostActivator extends View
        implements Activator {
    
    @SuppressWarnings("unused")
    private static final String TAG = LongClickDeletePostActivator.class.getSimpleName();
    private GenericUiObserver mCallback;
    @Inject @Named(DeletePostServicesMapper.DELETE_POST_VOLLEY_REQUEST) VolleyRequest<DeletePostResourceReturnData> mDeleteRequest;
    @Inject @Named(DeletePostServicesMapper.DELETE_POST_SERVICE) ServiceFetcher<DeletePostResourceReturnData> deletePostService;

    public LongClickDeletePostActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
                ObjectGraph.create(
                        new CommonMapper((Activity) getContext()),
                        new DeletePostServicesMapper()
                ).inject(this);
        UiEventThenServiceThenUiEvent deletePostController =
                new UiEventThenServiceThenUiEvent (
                        this,
                        deletePostService,
                        null,
                        new Receiver() {
                            @Override
                            public void success(Object result) {
                                EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
                            }
                            @Override public void fail(FailureResult r) { }
                        });
        deletePostController.setup().go();
        EventBus.getBus().register(this);
    }

    @Subscribe
    public void onLongPress(ClickableListView.LongPressListViewEvent obj) {
        if(obj.index!=0 && obj.ob instanceof PostResource) {
            PostResource pr = (PostResource) obj.ob;
            String url = Urls.getBasePath() + getContext().getString(R.string.url_del);
            mDeleteRequest.setUrl(url + pr.getId());
            mCallback.onUiEventActivated();
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