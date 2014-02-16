package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.posts.list.entities.PostResource;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

public class LongClickEditPostActivator extends View {

    @SuppressWarnings("unused")
    private static final String TAG = LongClickEditPostActivator.class.getSimpleName();
    private final FragmentActivity mActivity;

    public LongClickEditPostActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (FragmentActivity)getContext();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(ClickableListView.LongPressListViewEvent obj) {
        if(obj.index!=0 && obj.ob instanceof PostResource && obj.title.equals("Edit post")) {
            EditPostViewActivator view = new EditPostViewActivator(mActivity, null);
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