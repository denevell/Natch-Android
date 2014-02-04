package org.denevell.droidnatch.app.baseclasses;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;

import java.util.ArrayList;

public class ClickableListView<T> extends ListView implements
               OnPressObserver<T>,
               OnItemClickListener {

    private static final String TAG = ClickableListView.class.getSimpleName();
    public static class LongPressListViewEvent {
        public final Object ob;
        public final long id;
        public final String title;
        public final int index;
        public LongPressListViewEvent(Object ob, long id,String title, int index) {
            this.ob = ob;
            this.id = id;
            this.title = title;
            this.index = index;
        }
    }
    private HideKeyboard mHideKeyboard;
    private ArrayList<OnPress<T>> mPressListeners = new ArrayList<OnPressObserver.OnPress<T>>();

    public ClickableListView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        mHideKeyboard = new HideKeyboard();
        setOnItemClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Override
    public void addOnPressListener(OnPress<T> callback) {
        mPressListeners.add(callback);
    }

    @Subscribe
    public void onContextItemSelected(ObservableFragment.MenuItemHolder item) {
        try {
            Log.v(TAG, "Long press issued");
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.item.getMenuInfo();
            int index = info.position;
            @SuppressWarnings("unchecked")
            T tr = (T) getAdapter().getItem(index);
            EventBus.getBus().post(new LongPressListViewEvent(tr, item.item.getItemId(), item.item.getTitle().toString(), index));
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process oncontextitemselected event.", e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if(mHideKeyboard!=null && parent!=null) mHideKeyboard.hideKeyboard(parent.getContext(), view);
            Log.v(TAG, "Press issued");
            @SuppressWarnings("unchecked")
            T tr = (T) getAdapter().getItem(position);
            for (OnPress<T> listener: mPressListeners) {
                listener.onPress(tr);
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process onitemclick event.", e);
        }
    }

}
