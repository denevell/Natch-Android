package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver.OnLongPress;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;

public class LongClickDeleteUiEvent extends GenericUiObject implements OnLongPress<ThreadResource> {
    
    private Context mAppContext;
    private VolleyRequest<?> mDeleteRequest;

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
        String url = mAppContext.getString(R.string.url_baseurl) + mAppContext.getString(R.string.url_del); 
        mDeleteRequest.setUrl(url+obj.getRootPostId());
        submit();
    }

}
