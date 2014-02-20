package org.denevell.droidnatch.posts.list;

import org.denevell.natch.android.R;

import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListPostsContextMenu implements OnCreateContextMenuListener {
    private static final String TAG = ListPostsContextMenu.class.getSimpleName();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        try {
        	
            AdapterContextMenuInfo adapter = (AdapterContextMenuInfo) menuInfo;
            if(adapter.position==0) {
                menu.add(Menu.NONE, R.id.posts_context_menu_delete_thread, 0, "Delete thread");
                menu.add(Menu.NONE, R.id.posts_context_menu_edit_thread, 0, "Edit thread");
            } else if(adapter.position!=0) {
                menu.add(Menu.NONE, R.id.posts_context_menu_delete_post, 0, "Delete post");
                menu.add(Menu.NONE, R.id.posts_context_menu_edit_post, 0, "Edit post");
            }
        } catch (Exception e) {
            Log.d(TAG, "Problem setting context menu", e);
        }
    }
}