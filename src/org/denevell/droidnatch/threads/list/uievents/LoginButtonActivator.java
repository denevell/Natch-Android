package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView.InitialiseView;
import org.denevell.droidnatch.posts.list.ViewThatListensOnEventBus;
import org.denevell.natch.android.R;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

public class LoginButtonActivator extends ViewThatListensOnEventBus {

	@SuppressWarnings("unused")
	private static final String TAG = LoginButtonActivator.class.getSimpleName();
	private FragmentActivity mActivity;

	public LoginButtonActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (FragmentActivity) getContext();
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		if(!menu.item.getTitle().equals("Login")) return;
		InitialiseView viewInit = new InitialiseView() {
			@Override
			public void intialise(View v, final DialogFragment df) {
				if (v instanceof LoginViewActivator) {
					LoginViewActivator loginView = (LoginViewActivator) v;
					loginView.setSuccessCallback(new Runnable() {
						@Override
						public void run() {
							df.dismiss();
						}
					});
				}
			}
		};
		DialogueFragmentWithView.getInstance(R.layout.login_dialogue_layout, viewInit)
			.show(mActivity.getSupportFragmentManager(), "login popup");
	}

}