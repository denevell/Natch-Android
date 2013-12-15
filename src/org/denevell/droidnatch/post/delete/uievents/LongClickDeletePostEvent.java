package org.denevell.droidnatch.post.delete.uievents;

import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable.GenericUiSuccess;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver.OnLongPress;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.post.delete.HaltOnDeleteThread;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.content.Context;
import android.util.Log;

public class LongClickDeletePostEvent implements OnLongPress<PostResource>, GenericUiSuccess {
    
    private static final String TAG = LongClickDeletePostEvent.class.getSimpleName();
    private GenericUiObject mGenericObject;
    private Context mAppContext;
    private VolleyRequest<?> mDeleteRequest;
    private int mPositionClicked;
    private ScreenOpener mScreenOpener;
    private HaltOnDeleteThread mNextControllerHalter;

    public LongClickDeletePostEvent(
            final Context appContext,
            ScreenOpener screenOpener,
            OnLongPressObserver<PostResource> onLongPressObserver,
            final VolleyRequest<?> deleteRequest, 
            HaltOnDeleteThread nextControllerHalter) {
        mAppContext = appContext;
        mScreenOpener = screenOpener;
        mDeleteRequest = deleteRequest;
        mNextControllerHalter = nextControllerHalter;
        mGenericObject = new GenericUiObject();
        mGenericObject.setOnSuccess(this);
        onLongPressObserver.addOnLongClickListener(this);
    }

    public GenericUiObservable getUiEvent() {
        return mGenericObject;
    }

    @Override
    public void onLongPress(PostResource obj, int itemId, String optionName, int position) {
        mNextControllerHalter.setShouldHalt(false);
        mPositionClicked = position;
        String url = mAppContext.getString(R.string.url_baseurl) + mAppContext.getString(R.string.url_del); 
        mDeleteRequest.setUrl(url+obj.getId());
        mGenericObject.submit();
    }

    @Override
    public void onGenericUiSuccess() {
        try {
            if(mPositionClicked==0) {
                mNextControllerHalter.setShouldHalt(true);
                mScreenOpener.gotoPreviousScreen();
            }
        } catch(Exception e) {
            Log.e(TAG, "Couldn't goto previous screen", e);
        }
        mPositionClicked = 0;
    }

}