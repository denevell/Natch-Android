package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.posts.list.ViewThatListensOnEventBus;
import org.denevell.droidnatch.posts.list.entities.PostResource;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;

import com.squareup.otto.Subscribe;

public class LongClickEditThreadActivator extends ViewThatListensOnEventBus {

    @SuppressWarnings("unused")
    private static final String TAG = LongClickEditThreadActivator.class.getSimpleName();
    private final FragmentActivity mActivity;

    public LongClickEditThreadActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (FragmentActivity)getContext();
    }

    @Subscribe
    public void onLongPress(ClickableListView.LongPressListViewEvent obj) {
        if(obj.index==0 && obj.ob instanceof PostResource && obj.title.equals("Edit thread")) {
            EditThreadViewActivator view = new EditThreadViewActivator(mActivity, null);
            view.setPost((PostResource) obj.ob);

            final DialogueFragmentWithView df = DialogueFragmentWithView.getInstance(view);
            df.setArguments(new Bundle());

            view.setSuccessCallback(new Runnable() {
                @Override
                public void run() {
                    df.getDialog().dismiss();
                }
            });

            mActivity.getSupportFragmentManager().beginTransaction().add(df, "editthread_dialogue").commit();
        }
    }

}