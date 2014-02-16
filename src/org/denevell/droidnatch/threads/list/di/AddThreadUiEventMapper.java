package org.denevell.droidnatch.threads.list.di;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.uievents.AddThreadViewActivator;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class, ListThreadsViewStarter.class, ListPostsViewStarter.class}, complete=false, library=true)
public class AddThreadUiEventMapper {

    public static final String PROVIDES_ADD_THREAD_POPUP= "add thread dialogue popup";
    @SuppressWarnings("unused")
	private static final String TAG = AddThreadUiEventMapper.class.getSimpleName();
    private FragmentActivity mActivity;

    public AddThreadUiEventMapper(FragmentActivity activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named(PROVIDES_ADD_THREAD_POPUP)
    public Receiver<Void> providesAddThreadReceiver() {
        return new Receiver<Void>() {
            @Override
            public void success(Void result) {
            	AddThreadDialogueFragment df = new AddThreadDialogueFragment();
                df.setArguments(new Bundle());
                mActivity.getSupportFragmentManager().beginTransaction().add(df, "tag").commit();
            }
            @Override public void fail(FailureResult r) { }
        };
    }

    public static class AddThreadDialogueFragment extends DialogFragment {
        public AddThreadViewActivator view;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setRetainInstance(true);
            view = new AddThreadViewActivator(getActivity(), null);
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
    }

}
