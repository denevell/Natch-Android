package org.denevell.droidnatch.threads.list.uievents;

import android.util.Log;
import android.widget.EditText;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;

import java.util.HashMap;

public class OpenNewThreadReceiver implements
        Receiver<AddPostResourceReturnData> {

    private static final String TAG = OpenNewThreadReceiver.class.getSimpleName();
    private EditText mEditText;
    private AddPostResourceInput mResourceInput;
    private ScreenOpener mScreenOpener;

    public OpenNewThreadReceiver(ScreenOpener screenOpener) {
        mScreenOpener = screenOpener;
    }

    @Override
    public void success(AddPostResourceReturnData obj) {
        Log.v(TAG, "Opening new list posts fragment");
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getThread().getId());
        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, obj.getThread().getSubject());
        mScreenOpener.openScreen(ListPostsFragment.class, hm);
    }

    @Override
    public void fail(FailureResult r) {

    }

}
