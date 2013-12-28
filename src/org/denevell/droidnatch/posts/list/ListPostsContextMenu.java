package org.denevell.droidnatch.posts.list;

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
                menu.add(Menu.NONE, 1, 0, "Delete thread");
            } else if(adapter.position!=0) {
                menu.add(Menu.NONE, 2, 0, "Delete post");
            }
        } catch (Exception e) {
            Log.d(TAG, "Problem setting context menu", e);
        }
    }
}