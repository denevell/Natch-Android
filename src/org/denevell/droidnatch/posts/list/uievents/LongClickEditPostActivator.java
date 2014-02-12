package org.denevell.droidnatch.posts.list.uievents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.posts.list.entities.PostResource;

public class LongClickEditPostActivator extends View
        implements Activator {

    @SuppressWarnings("unused")
    private static final String TAG = LongClickEditPostActivator.class.getSimpleName();
    private final FragmentActivity mActivity;
    private GenericUiObserver mCallback;

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
            Fragment old = mActivity.getSupportFragmentManager().findFragmentByTag("editpost_dialogue");
            if(old!=null && old instanceof DialogFragment) {
                ((DialogFragment)old).show(mActivity.getSupportFragmentManager(), "editpost_dialogue");
            } else {
                EditPostDialogueFragment df = new EditPostDialogueFragment();
                df.setPostId(((PostResource) obj.ob).getId());
                df.setArguments(new Bundle());
                mActivity.getSupportFragmentManager().beginTransaction().addToBackStack("editpost_dialogue").add(df, "editpost_dialogue").commit();
            }
        }
    }

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(Object result) {  }

    @Override
    public void fail(FailureResult r) { }

    public static class EditPostDialogueFragment extends DialogFragment {
        public EditPostViewActivator view;
        private long mPostId;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setRetainInstance(true);
            view = new EditPostViewActivator(getActivity(), null);
            view.setPostId(mPostId);
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

        @Override
        public void onDismiss(DialogInterface dialog) {
            getArguments().putParcelable("view_state", view.getInstanceState());
            super.onDismiss(dialog);
        }

        @Override
        public void onDestroyView() {
            if (getDialog() != null && getRetainInstance())
                getDialog().setDismissMessage(null);
            super.onDestroyView();
        }

        public void setPostId(long id) {
            mPostId = id;
        }
    }
}