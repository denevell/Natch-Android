package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;

public class ListThreadsContextMenu implements OnCreateContextMenuListener {
	private static final String TAG = ListThreadsContextMenu.class
			.getSimpleName();
	private ListAdapter mListAdapter;

	public ListThreadsContextMenu(ListAdapter listAdapter) {
		mListAdapter = listAdapter;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		try {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			int pos = info.position;
			ThreadResource ob = (ThreadResource) mListAdapter.getItem(pos);
			String author = ob.getAuthor();
			String username = Urls.getUsername();
			if (author.equals(username)) {
				menu.add(Menu.NONE, R.id.posts_context_menu_delete_thread, 0, "Delete thread");
			} else if(Urls.getUsername()==null || Urls.getUsername().isEmpty()){
				menu.add(Menu.NONE, 0, 0, "Please login");
			}
		} catch (Exception e) {
			Log.e(TAG, "Couldn't set the context menus... Panic...", e);
		}
	}
}