package org.denevell.droidnatch.app.views;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView.InitialiseView;
import org.denevell.droidnatch.posts.list.ViewThatListensOnEventBus;
import org.denevell.natch.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

/**
 * Required the ObservableFragment is active on our fragment so it sends 
 * out ObservableFragment.OptionMenuItemHolder event.
 */
public class DialoguePopupOnOptionMenuActivator extends ViewThatListensOnEventBus {
	@SuppressWarnings("unused")
	private static final String TAG = DialoguePopupOnOptionMenuActivator.class.getSimpleName();
	private FragmentActivity mActivity;
	private int mLayout;
	private int mOptionId;

	public DialoguePopupOnOptionMenuActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DialogueOpener, 0, 0);
		try {
			mLayout = a.getResourceId(R.styleable.DialogueOpener_dialogue_layout, -1);
			mOptionId = a.getResourceId(R.styleable.DialogueOpener_option_menu_item, -1);
			if (mLayout == -1 || mOptionId == -1) {
				throw new RuntimeException("Didn't specify layout and/or option id.");
			}
		} finally {
			a.recycle();
		}
		mActivity = (FragmentActivity) getContext();
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		if (menu.item.getItemId() != mOptionId) return;
		InitialiseView viewInit = new InitialiseView() {
			@Override
			public void intialise(View v, final DialogFragment df) {
				dismissDialogueOnViewFinished(v, df);
			}
		};
		final DialogueFragmentWithView df = DialogueFragmentWithView
				.getInstance(mLayout, viewInit);
		df.show(mActivity.getSupportFragmentManager(), String.valueOf(mLayout));
	}

	private void dismissDialogueOnViewFinished(View v, final DialogFragment df) {
		if (v instanceof Finishable) {
			Finishable finishable = (Finishable) v;
            finishable.setFinishedCallback(new Runnable() {
            	@Override
                public void run() {
            		df.dismiss();
                }
            });
		}
	}

}