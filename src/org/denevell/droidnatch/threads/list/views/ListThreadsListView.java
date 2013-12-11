package org.denevell.droidnatch.threads.list.views;

import java.util.ArrayList;

import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedHolder;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class ListThreadsListView implements 
        OnCreateContextMenuListener, 
               ContextItemSelected,
               OnLongPressObserver<ThreadResource> {

    private static final String TAG = ListThreadsListView.class.getSimpleName();
    private ListView mListView;
    private ArrayList<OnLongPress<ThreadResource>> mLongPressListeners = new ArrayList<OnLongPress<ThreadResource>>();

    public ListThreadsListView(ListView listView, 
            ContextItemSelectedHolder contextSelectedHolder) {
        mListView = listView;
        mListView.setOnCreateContextMenuListener(this);
        contextSelectedHolder.addContextItemSelectedCallback(this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 0, 0, "Delete");
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            ThreadResource tr = (ThreadResource) mListView.getAdapter().getItem(index);
            for (OnLongPress<ThreadResource> listener: mLongPressListeners) {
                listener.onLongPress(tr);
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process oncontextitemselected event.", e);
        }
        return true;
    }

    @Override
    public void addOnLongClickListener(OnLongPress<ThreadResource> callback) {
        mLongPressListeners.add(callback);
    }

    public ListView getListView() {
        return mListView;
    }

}
