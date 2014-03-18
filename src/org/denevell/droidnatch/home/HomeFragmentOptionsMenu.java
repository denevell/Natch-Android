package org.denevell.droidnatch.home;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.DummyMenuItem;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import com.newfivefour.android.manchester.R;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class HomeFragmentOptionsMenu {
	
	public void create(Menu menu, MenuInflater inflater, Context appContext) {
        inflater.inflate(R.menu.list_threads_options_menu, menu);
        String username = ShamefulStatics.getUsername(appContext);
		if(username!=null && username.trim().length()>0) {
        	MenuItem item = menu.findItem(R.id.threads_option_menu_login);
        	item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					DummyMenuItem item1 = new DummyMenuItem(R.id.logout_context_menu);
					EventBus.getBus().post(new ObservableFragment.OptionMenuItemHolder(item1));
					return true;
				}
			});
        	item.setTitle(username);
        	item = menu.findItem(R.id.threads_option_menu_register);
        	item.setVisible(false);
        }
	}
	
}
