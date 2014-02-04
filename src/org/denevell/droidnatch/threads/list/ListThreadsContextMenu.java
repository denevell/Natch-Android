package org.denevell.droidnatch.threads.list;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;

public class ListThreadsContextMenu implements OnCreateContextMenuListener {
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 0, 0, "Delete thread");
    }
}