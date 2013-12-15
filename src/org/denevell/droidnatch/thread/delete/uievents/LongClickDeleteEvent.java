package org.denevell.droidnatch.thread.delete.uievents;

import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver.OnLongPress;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.content.Context;

public class LongClickDeleteEvent implements OnLongPress<ThreadResource> {
    
    private GenericUiObject mGenericObject;
    private Context mAppContext;
    private VolleyRequest<?> mDeleteRequest;

    public LongClickDeleteEvent(
            final Context appContext,
            OnLongPressObserver<ThreadResource> onLongPressObserver,
            final VolleyRequest<?> deleteRequest) {
        mAppContext = appContext;
        mDeleteRequest = deleteRequest;
        mGenericObject = new GenericUiObject();
        onLongPressObserver.addOnLongClickListener(this);
    }

    public GenericUiObservable getUiEvent() {
        return mGenericObject;
    }

    @Override
    public void onLongPress(ThreadResource obj, int itemId, String optionName, int index) {
        String url = mAppContext.getString(R.string.url_baseurl) + mAppContext.getString(R.string.url_del); 
        mDeleteRequest.setUrl(url+obj.getRootPostId());
        mGenericObject.submit();
    }

}
