package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.app.views.DialogFragmentWithRotationFix;
import org.denevell.droidnatch.posts.list.ViewThatListensOnEventBus;
import org.denevell.droidnatch.posts.list.entities.PostResource;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
            EditPostDialogueFragment df = new EditPostDialogueFragment();
            df.setPost((PostResource) obj.ob);
            df.setArguments(new Bundle());
            mActivity.getSupportFragmentManager().beginTransaction().add(df, "editthread_dialogue").commit();
        }
    }

    public static class EditPostDialogueFragment extends DialogFragmentWithRotationFix {
        public EditThreadViewActivator view;
        private PostResource mPost;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setRetainInstance(true);
            view = new EditThreadViewActivator(getActivity(), null);
            view.setPost(mPost);
            Parcelable args = getArguments().getParcelable("view_state");
            if(args!=null) view.setInstanceState(args);
            view.setSuccessCallback(new Runnable() {
                @Override
                public void run() {
                    getDialog().dismiss();
                }
            });
            return new AlertDialog.Builder(getActivity()).setView(view).create();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            getArguments().putParcelable("view_state", view.getInstanceState());
            super.onSaveInstanceState(outState);
        }
        
        public void setPost(PostResource ob) {
           mPost = ob;
        }
    }
}