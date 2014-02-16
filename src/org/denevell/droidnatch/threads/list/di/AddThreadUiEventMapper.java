package org.denevell.droidnatch.threads.list.di;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.uievents.AddThreadViewActivator;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;

import android.os.Bundle;
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
            	AddThreadViewActivator view = new AddThreadViewActivator(mActivity, null);
            	final DialogueFragmentWithView df = DialogueFragmentWithView.getInstance(view);
            	view.setSuccessCallback(new Runnable() {
					@Override
					public void run() {
						df.getDialog().dismiss();
					}
				});
                df.setArguments(new Bundle());
                df.show(mActivity.getSupportFragmentManager(), "tag");
                //mActivity.getSupportFragmentManager().beginTransaction().add(df, "tag").commit();
            }
            @Override public void fail(FailureResult r) { }
        };
    }


}
