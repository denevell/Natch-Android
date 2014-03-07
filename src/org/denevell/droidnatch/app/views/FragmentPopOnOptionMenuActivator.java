package org.denevell.droidnatch.app.views;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.natch.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;

import com.squareup.otto.Subscribe;

public class FragmentPopOnOptionMenuActivator extends ViewThatListensOnEventBus {
	private static final String TAG = FragmentPopOnOptionMenuActivator.class.getSimpleName();
	private int mOptionId;

	public FragmentPopOnOptionMenuActivator(Context context, AttributeSet attrs) throws Exception {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FragementPopMenuActivator, 0, 0);
		try {
			mOptionId = a.getResourceId(R.styleable.FragementPopMenuActivator_pop_option_menu_item, -1);
			if (mOptionId == -1) {
				throw new RuntimeException("Didn't specify a fragment name or option id.");
			}
		} finally {
			a.recycle();
		}
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
        try {
	    if(menu.item.getItemId()!=mOptionId) return;
            Log.v(TAG, "Poping the stack");
            FragmentManager f = ((FragmentActivity)getContext()).getSupportFragmentManager();
            f.popBackStack();
        } catch (Exception e) {
            Log.e(TAG, "Couldn't pop the backstacck", e);
        }		
	}


}
