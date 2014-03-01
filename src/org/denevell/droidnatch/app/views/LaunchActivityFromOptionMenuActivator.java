package org.denevell.droidnatch.app.views;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.squareup.otto.Subscribe;

public class LaunchActivityFromOptionMenuActivator extends ViewThatListensOnEventBus {
	@SuppressWarnings("unused")
	private static final String TAG = LaunchActivityFromOptionMenuActivator.class.getSimpleName();
	private int mOptionId;
	private Class<Activity> mActivityClass;

	@SuppressWarnings("unchecked")
	public LaunchActivityFromOptionMenuActivator(Context context, AttributeSet attrs) throws Exception {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FragmentOpener, 0, 0);
		try {
			String fragmentClass = a.getString(R.styleable.FragmentOpener_fragment_class);
			mOptionId = a.getResourceId(R.styleable.FragmentOpener_option_menu_item_for_fragment_opener, -1);
			if (mOptionId == -1) {
				throw new RuntimeException("Didn't specify a fragment name or option id.");
			}
			mActivityClass = (Class<Activity>) Class.forName(fragmentClass);
		} finally {
			a.recycle();
		}
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		if(mOptionId != menu.item.getItemId()) return;
		getContext().startActivity(new Intent(getContext(), mActivityClass));
	}


}