package org.denevell.droidnatch.posts.list.uievents;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.posts.list.di.DeletePostServicesMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class LongClickDeletePostActivator extends View
        implements Activator<DeletePostResourceReturnData> {
    
    @SuppressWarnings("unused")
    private static final String TAG = LongClickDeletePostActivator.class.getSimpleName();
    private GenericUiObserver mCallback;
    @Inject @Named(DeletePostServicesMapper.DELETE_POST_VOLLEY_REQUEST) VolleyRequest<Void, DeletePostResourceReturnData> mDeleteRequest;
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
        @SuppressWarnings("unchecked")
		UiEventThenServiceThenUiEvent<DeletePostResourceReturnData> deletePostController =
                new UiEventThenServiceThenUiEvent<DeletePostResourceReturnData> (
                        this,
                        deletePostService,
                        null,
                        new Receiver<DeletePostResourceReturnData>() {
                            @Override
                            public void success(DeletePostResourceReturnData result) {
                                EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
                            }
                            @Override public void fail(FailureResult r) { }
                        });
        deletePostController.setup();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(ClickableListView.LongPressListViewEvent obj) {
        if(obj.index!=0 && obj.ob instanceof PostResource && obj.title.equals("Delete post")) {
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
    public void success(DeletePostResourceReturnData result) {

    }

    @Override
    public void fail(FailureResult r) {

    }
}