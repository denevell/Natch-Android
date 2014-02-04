package org.denevell.droidnatch.posts.list.uievents;

import android.content.Context;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

public class LongClickDeleteThreadUiEvent implements Activator {
    
    @SuppressWarnings("unused")
    private static final String TAG = LongClickDeleteThreadUiEvent.class.getSimpleName();
    private Context mAppContext;
    private VolleyRequest<?> mDeleteRequest;
    private GenericUiObserver mCallback;

    public LongClickDeleteThreadUiEvent(
            final Context appContext,
            final VolleyRequest<?> deleteRequest) {
        mAppContext = appContext;
        mDeleteRequest = deleteRequest;
        EventBus.getBus().register(this);
    }

    @Subscribe
    public void onLongPress(ClickableListView.LongPressListViewEvent obj) {
        if(obj.ob instanceof PostResource) {
            PostResource tr = (PostResource) obj.ob;
            if(obj.index==0) {
                String url = Urls.getBasePath() + mAppContext.getString(R.string.url_del);
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