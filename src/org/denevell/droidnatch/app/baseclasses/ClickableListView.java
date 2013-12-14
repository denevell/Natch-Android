package org.denevell.droidnatch.app.baseclasses;

import java.util.ArrayList;

import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ClickableListView<T> implements 
               ContextItemSelected,
               OnLongPressObserver<T>, 
               OnPressObserver<T>, 
               OnItemClickListener {

    private static final String TAG = ClickableListView.class.getSimpleName();
    private ListView mListView;
    private ArrayList<OnLongPress<T>> mLongPressListeners = new ArrayList<OnLongPress<T>>();
    private ArrayList<OnPress<T>> mPressListeners = new ArrayList<OnPressObserver.OnPress<T>>();

    public ClickableListView(ListView listView, 
            ContextItemSelectedObserver contextSelectedObservable, 
            OnCreateContextMenuListener onCreateContextMenu) {
        mListView = listView;
        mListView.setOnCreateContextMenuListener(onCreateContextMenu);
        mListView.setOnItemClickListener(this);
        contextSelectedObservable.addContextItemSelectedCallback(this);
    }

    @Override
    public void addOnLongClickListener(OnLongPress<T> callback) {
        mLongPressListeners.add(callback);
    }

    @Override
    public void addOnPressListener(OnPress<T> callback) {
        mPressListeners.add(callback);
    }

    public ListView getListView() {
        return mListView;
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            Log.v(TAG, "Long press issued");
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            @SuppressWarnings("unchecked")
            T tr = (T) mListView.getAdapter().getItem(index);
            for (OnLongPress<T> listener: mLongPressListeners) {
                listener.onLongPress(tr, item.getItemId(), item.getTitle().toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process oncontextitemselected event.", e);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Log.v(TAG, "Press issued");
            @SuppressWarnings("unchecked")
            T tr = (T) mListView.getAdapter().getItem(position);
            for (OnPress<T> listener: mPressListeners) {
                listener.onPress(tr);
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process onitemclick event.", e);
        }
    }

}