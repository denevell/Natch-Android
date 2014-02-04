package org.denevell.droidnatch.app.baseclasses;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;

import java.util.ArrayList;

public class ClickableListView<T> implements
               ContextItemSelected,
               OnPressObserver<T>,
               OnItemClickListener {

    private static final String TAG = ClickableListView.class.getSimpleName();
    public static class LongPressListViewEvent<T> {
        public final T ob;
        public final long id;
        public final String title;
        public final int index;
        public LongPressListViewEvent(T ob, long id,String title, int index) {
            this.ob = ob;
            this.id = id;
            this.title = title;
            this.index = index;
        }
    }
    private HideKeyboard mHideKeyboard;
    private ListView mListView;
    private ArrayList<OnPress<T>> mPressListeners = new ArrayList<OnPressObserver.OnPress<T>>();

    public ClickableListView(ListView listView, 
            ContextItemSelectedObserver contextSelectedObservable,
            HideKeyboard hideKeyboard,
            OnCreateContextMenuListener onCreateContextMenu) {
        mListView = listView;
        mHideKeyboard = hideKeyboard;
        mListView.setOnCreateContextMenuListener(onCreateContextMenu);
        mListView.setOnItemClickListener(this);
        contextSelectedObservable.addContextItemSelectedCallback(this);
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
            EventBus.getBus().post(new LongPressListViewEvent<T>(tr, item.getItemId(), item.getTitle().toString(), index));
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process oncontextitemselected event.", e);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if(mHideKeyboard!=null && parent!=null) mHideKeyboard.hideKeyboard(parent.getContext(), view);
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
