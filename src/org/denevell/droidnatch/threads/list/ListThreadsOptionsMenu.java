package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.Urls;
import org.denevell.natch.android.R;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ListThreadsOptionsMenu {
	
	public void create(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_threads_options_menu, menu);
        String username = Urls.getUsername();
		if(username!=null && username.trim().length()>0) {
        	MenuItem item = menu.findItem(R.id.threads_option_menu_login);
        	item.setTitle(username);
        	item = menu.findItem(R.id.threads_option_menu_register);
        	item.setVisible(false);
        }
	}
	
}
