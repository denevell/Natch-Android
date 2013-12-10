package org.denevell.droidnatch.threads.list.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;

public class ListThreadsListView extends ListView implements OnCreateContextMenuListener {

    public ListThreadsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnCreateContextMenuListener(this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 0, 0, "Delete");
    }

}
