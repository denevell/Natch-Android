package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView.InitialiseView;
import org.denevell.droidnatch.posts.list.ViewThatListensOnEventBus;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

public class LongClickEditThreadActivator extends ViewThatListensOnEventBus {

	@SuppressWarnings("unused")
	private static final String TAG = LongClickEditThreadActivator.class
			.getSimpleName();
	private final FragmentActivity mActivity;

	public LongClickEditThreadActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (FragmentActivity) getContext();
	}

	@Subscribe
	public void onLongPress(final ClickableListView.LongPressListViewEvent obj) {
		if (obj.ob instanceof PostResource 
				&& obj.menuItem.getTitle().toString().equals("Edit thread")) {

			InitialiseView initView = new InitialiseView() {
				@Override
				public void intialise(View v, final DialogFragment df) {
					if (v instanceof EditThreadViewActivator) {
						EditThreadViewActivator view = (EditThreadViewActivator) v;
						view.setPost((PostResource) obj.ob);
						view.setSuccessCallback(new Runnable() {
							@Override
							public void run() {
								df.getDialog().dismiss();
							}
						});
					}
				}
			};

			final DialogueFragmentWithView df = DialogueFragmentWithView.getInstance(
							R.layout.edit_thread_dialogue_layout, 
							initView);
			df.setArguments(new Bundle());

			mActivity.getSupportFragmentManager().beginTransaction().add(df, "editthread_dialogue").commit();
		}
	}

}