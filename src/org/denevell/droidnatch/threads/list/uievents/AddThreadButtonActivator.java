package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView.InitialiseView;
import org.denevell.droidnatch.posts.list.ViewThatListensOnEventBus;
import org.denevell.natch.android.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

public class AddThreadButtonActivator extends ViewThatListensOnEventBus {

	@SuppressWarnings("unused")
	private static final String TAG = AddThreadButtonActivator.class
			.getSimpleName();
	private FragmentActivity mActivity;

	public AddThreadButtonActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (FragmentActivity) getContext();
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		InitialiseView viewInit = new InitialiseView() {
			@Override
			public void intialise(View v, final DialogFragment df) {
				if (v instanceof AddThreadViewActivator) {
					AddThreadViewActivator addThread = (AddThreadViewActivator) v;
					addThread.setSuccessCallback(new Runnable() {
						@Override
						public void run() {
							df.dismiss();
						}
					});
				}
			}
		};
		final DialogueFragmentWithView df = DialogueFragmentWithView
				.getInstance(R.layout.add_thread_dialogue_layout, viewInit);
		df.setArguments(new Bundle());
		df.show(mActivity.getSupportFragmentManager(), "add thread popup");
	}

}