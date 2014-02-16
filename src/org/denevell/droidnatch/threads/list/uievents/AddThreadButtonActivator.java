package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.posts.list.ViewThatListensOnEventBus;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;

import com.squareup.otto.Subscribe;

public class AddThreadButtonActivator extends ViewThatListensOnEventBus {

	@SuppressWarnings("unused")
	private static final String TAG = AddThreadButtonActivator.class.getSimpleName();
	private FragmentActivity mActivity;

	public AddThreadButtonActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (FragmentActivity) getContext();
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		AddThreadViewActivator view = new AddThreadViewActivator(mActivity, null);
		final DialogueFragmentWithView df = DialogueFragmentWithView.getInstance(view);
		view.setSuccessCallback(new Runnable() {
			@Override
			public void run() {
				df.getDialog().dismiss();
			}
		});
		df.setArguments(new Bundle());
		df.show(mActivity.getSupportFragmentManager(), "add thread popup");
	}

}