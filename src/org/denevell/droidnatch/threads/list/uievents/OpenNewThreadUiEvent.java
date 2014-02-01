package org.denevell.droidnatch.threads.list.uievents;

import android.util.Log;
import android.widget.EditText;

import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;

import java.util.HashMap;

import static org.denevell.droidnatch.app.interfaces.GenericUiObservable.GenericUiObserver;

public class OpenNewThreadUiEvent extends GenericUiObject
        implements GenericUiObserver<AddPostResourceReturnData> {

    private static final String TAG = OpenNewThreadUiEvent.class.getSimpleName();
    private EditText mEditText;
    private AddPostResourceInput mResourceInput;
    private ScreenOpener mScreenOpener;

    public OpenNewThreadUiEvent(ScreenOpener screenOpener) {
        setOnSubmitObserver(this);
        mScreenOpener = screenOpener;
    }

    @Override
    public void onGenericUiEvent(AddPostResourceReturnData obj) {
        Log.v(TAG, "Opening new list posts fragment");
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getThread().getId());
        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, obj.getThread().getSubject());
        mScreenOpener.openScreen(ListPostsFragment.class, hm);
    }
}
