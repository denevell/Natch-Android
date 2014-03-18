package org.denevell.droidnatch.home.uievents;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.views.ViewThatListensOnEventBus;
import com.newfivefour.android.manchester.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

import com.squareup.otto.Subscribe;

public class WebsiteFromOptionMenuActivator extends ViewThatListensOnEventBus {
	@SuppressWarnings("unused")
	private static final String TAG = WebsiteFromOptionMenuActivator.class.getSimpleName();

	public WebsiteFromOptionMenuActivator(Context context, AttributeSet attrs) throws Exception {
		super(context, attrs);
	}

	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		if(R.id.threads_option_menu_website!= menu.item.getItemId()) return;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(getContext().getString(R.string.url_website)));
		getContext().startActivity(i);
	}


}