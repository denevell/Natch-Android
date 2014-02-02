package org.denevell.droidnatch.threads.list.uievents;

import android.content.Context;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ActivatingUiObject;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver.OnLongPress;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

public class LongClickDeleteUiEvent implements ActivatingUiObject, OnLongPress<ThreadResource> {
    
    private Context mAppContext;
    private VolleyRequest<?> mDeleteRequest;
    private GenericUiObserver mCallback;

    public LongClickDeleteUiEvent(
            final Context appContext,
            final OnLongPressObserver<ThreadResource> onLongPressObserver,
            final VolleyRequest<?> deleteRequest) {
        mAppContext = appContext;
        mDeleteRequest = deleteRequest;
        onLongPressObserver.addOnLongClickListener(this);
    }

    @Override
    public void onLongPress(ThreadResource obj, int itemId, String optionName, int index) {
        String url = Urls.getBasePath() + mAppContext.getString(R.string.url_del);
        mDeleteRequest.setUrl(url+obj.getRootPostId());
        mCallback.onUiEventActivated();
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
