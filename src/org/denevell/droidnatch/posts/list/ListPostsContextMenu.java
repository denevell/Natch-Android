package org.denevell.droidnatch.posts.list;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;

public class ListPostsContextMenu implements OnCreateContextMenuListener {
	private static final String TAG = ListPostsContextMenu.class
			.getSimpleName();
	private ListAdapter mListAdapter;

	public ListPostsContextMenu(ListAdapter listAdapter) {
		mListAdapter = listAdapter;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		try {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			if (doWeOwnThePost(info, mListAdapter)) {
				if (info.position == 0) {
					menu.add(Menu.NONE, R.id.posts_context_menu_delete_thread,
							0, "Delete thread");
					menu.add(Menu.NONE, R.id.posts_context_menu_edit_thread, 0,
							"Edit thread");
				} else if (info.position != 0) {
					menu.add(Menu.NONE, R.id.posts_context_menu_delete_post, 0,
							"Delete post");
					menu.add(Menu.NONE, R.id.posts_context_menu_edit_post, 0,
							"Edit post");
				}
			} else if(Urls.getUsername()==null || Urls.getUsername().isEmpty()){
				menu.add(Menu.NONE, 0, 0, "Please logon");
			}
		} catch (Exception e) {
			Log.d(TAG, "Problem setting context menu", e);
		}
	}

	private boolean doWeOwnThePost(AdapterContextMenuInfo menuInfo, ListAdapter listAdapter) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		int pos = info.position;
		PostResource ob = (PostResource) listAdapter.getItem(pos);
		String author = ob.getUsername();
		String username = Urls.getUsername();
		return author.equals(username);
	}
}