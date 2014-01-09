package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver.OnLongPress;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.content.Context;

public class LongClickDeletePostUiEvent extends GenericUiObject implements OnLongPress<PostResource> {
    
    @SuppressWarnings("unused")
    private static final String TAG = LongClickDeletePostUiEvent.class.getSimpleName();
    private Context mAppContext;
    private VolleyRequest<?> mDeleteRequest;

    public LongClickDeletePostUiEvent(
            final Context appContext,
            OnLongPressObserver<PostResource> onLongPressObserver,
            final VolleyRequest<?> deleteRequest) {
        mAppContext = appContext;
        mDeleteRequest = deleteRequest;
        onLongPressObserver.addOnLongClickListener(this);
    }

    @Override
    public void onLongPress(PostResource obj, int itemId, String optionName, int position) {
        if(position!=0) {
            String url = mAppContext.getString(R.string.url_baseurl) + mAppContext.getString(R.string.url_del); 
            mDeleteRequest.setUrl(url+obj.getId());
            submit();
        }
    }

}