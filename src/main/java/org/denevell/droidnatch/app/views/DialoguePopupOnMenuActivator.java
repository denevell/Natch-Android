package org.denevell.droidnatch.app.views;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.CanSetEntity;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView.InitialiseView;
import com.newfivefour.android.manchester.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Subscribe;

/**
 * Required the ObservableFragment is active on our fragment so it sends 
 * out ObservableFragment.OptionMenuItemHolder event.
 * 
 * Also listens for LongPressListViewEvent from the ClickableListView.
 * 
 * The layout which is passed in must extend CanSetEntity and Finishable if you
 * want the LongPressListViewEvent to pass in it's row item and if you want the
 * View to tell the dialogue when to dismiss itself (which you do).
 */
public class DialoguePopupOnMenuActivator extends ViewThatListensOnEventBus {
	@SuppressWarnings("unused")
	private static final String TAG = DialoguePopupOnMenuActivator.class.getSimpleName();
	private FragmentActivity mActivity;
	private int mLayout;
	private int mOptionId;

	public DialoguePopupOnMenuActivator(Context context, AttributeSet attrs) {
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

	public DialoguePopupOnMenuActivator(Context context) {
		super(context);
		mActivity = (FragmentActivity) getContext();
	}
	
	public void setLayout(int mLayout) {
		this.mLayout = mLayout;
	}
	
	@Subscribe
	public void onListViewLongClick(ReceivingClickingAutopaginatingListView.LongPressListViewEvent event) {
		createDialogue(event.menuItem, event.ob);
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		createDialogue(menu.item, null);
	}

	public void createDialogue(final MenuItem menu, final Object entity) {
		if (menu.getItemId() != mOptionId) return;
		createDialogue(entity);
	}

	public void createDialogue(final Object entity) {
		InitialiseView viewInit = new InitialiseView() {
			@Override
			public void intialise(View v, final DialogFragment df) {
				dismissDialogueOnViewFinished(v, df);
				setEntityIfAvailable(entity, v);
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

	private void setEntityIfAvailable(final Object entity, View v) {
		if(v instanceof CanSetEntity && entity != null) {
			CanSetEntity cse = (CanSetEntity) v;
			cse.setEntity(entity);
		}
	}

}