package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.di.DeleteThreadServicesMapper;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class LongClickDeleteActivator extends View implements Activator<DeletePostResourceReturnData> {
    
    @Inject VolleyRequest<DeletePostResourceReturnData> mDeleteRequest;
    private GenericUiObserver mCallback;
    @Inject ServiceFetcher<DeletePostResourceReturnData> deleteThreadService;

    public LongClickDeleteActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        ObjectGraph.create(
                new CommonMapper((Activity) context),
                new DeleteThreadServicesMapper()
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        @SuppressWarnings("unchecked")
		Controller deleteThreadController =
            new UiEventThenServiceThenUiEvent<DeletePostResourceReturnData>(
                this,
                deleteThreadService,
                null,
                new Receiver<DeletePostResourceReturnData>() {
                    @Override
                    public void success(DeletePostResourceReturnData v) {
                        EventBus.getBus().post(new ListThreadsViewStarter.CallControllerListThreads());
                    }
                    @Override
                    public void fail(FailureResult r) { }
                });
        deleteThreadController.setup();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(ClickableListView.LongPressListViewEvent obj) {
        if(obj.ob instanceof ThreadResource) {
            ThreadResource pr = (ThreadResource) obj.ob;
            String url = Urls.getBasePath() + getContext().getString(R.string.url_del);
            mDeleteRequest.setUrl(url+pr.getRootPostId());
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
