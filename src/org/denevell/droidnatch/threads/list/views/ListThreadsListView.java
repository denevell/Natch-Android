package org.denevell.droidnatch.threads.list.views;

import java.util.ArrayList;

import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListThreadsListView implements 
        OnCreateContextMenuListener, 
               ContextItemSelected,
               OnLongPressObserver<ThreadResource>, 
               OnPressObserver<ThreadResource>, 
               OnItemClickListener {

    private static final String TAG = ListThreadsListView.class.getSimpleName();
    private ListView mListView;
    private ArrayList<OnLongPress<ThreadResource>> mLongPressListeners = new ArrayList<OnLongPress<ThreadResource>>();
    private ArrayList<OnPress<ThreadResource>> mPressListeners = new ArrayList<OnPressObserver.OnPress<ThreadResource>>();

    public ListThreadsListView(ListView listView, 
            ContextItemSelectedObserver contextSelectedObservable) {
        mListView = listView;
        mListView.setOnCreateContextMenuListener(this);
        mListView.setOnItemClickListener(this);
        contextSelectedObservable.addContextItemSelectedCallback(this);
    }

    @Override
    public void addOnLongClickListener(OnLongPress<ThreadResource> callback) {
        mLongPressListeners.add(callback);
    }

    @Override
    public void addOnPressListener(OnPress<ThreadResource> callback) {
        mPressListeners.add(callback);
    }

    public ListView getListView() {
        return mListView;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            ThreadResource tr = (ThreadResource) mListView.getAdapter().getItem(position);
            for (OnPress<ThreadResource> listener: mPressListeners) {
                listener.onPress(tr);
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process onitemclick event.", e);
        }
    }

}
